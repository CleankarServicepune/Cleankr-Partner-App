package com.example.data.repository

import android.content.Context
import com.example.data.firebase.CleankrFirebaseConfig
import com.example.data.firebase.CleankrFirebaseService
import com.example.data.local.AppDatabase
import com.example.data.local.AuditLogEntity
import com.example.data.local.JobEntity
import com.example.data.local.LeaveEntity
import com.example.data.model.ApprovalStatus
import com.example.data.model.AuditLogEntry
import com.example.data.model.CalendarLeaveEntry
import com.example.data.model.EarningSummary
import com.example.data.model.Job
import com.example.data.model.JobStatus
import com.example.data.model.KycStatus
import com.example.data.model.NotificationItem
import com.example.data.model.PartnerProfile
import com.example.data.model.ServiceChangeRequest
import com.example.data.model.WithdrawalRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PartnerRepository(private val context: Context) {

  private val database = AppDatabase.getDatabase(context)
  private val jobDao = database.jobDao()
  private val auditDao = database.auditDao()
  private val leaveDao = database.leaveDao()
  private val repositoryScope = CoroutineScope(Dispatchers.IO)

  val firebaseService = CleankrFirebaseService(context)
  private val _firebaseStatus = MutableStateFlow(CleankrFirebaseConfig.checkStatus(context))
  val firebaseStatus: StateFlow<CleankrFirebaseConfig.FirebaseConfigStatus> = _firebaseStatus.asStateFlow()

  // In-memory state for Partner Profile & Online Duty
  private val _isOnline = MutableStateFlow(true)
  val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

  private val _profile = MutableStateFlow(PartnerProfile())
  val profile: StateFlow<PartnerProfile> = _profile.asStateFlow()

  private val _earnings = MutableStateFlow(EarningSummary())
  val earnings: StateFlow<EarningSummary> = _earnings.asStateFlow()

  private val _withdrawals = MutableStateFlow<List<WithdrawalRequest>>(
    listOf(
      WithdrawalRequest(
        id = "W-101",
        amount = 5000.0,
        destination = "HDFC Bank (A/C: *******4921)",
        status = "Paid",
        requestedAt = System.currentTimeMillis() - 86400000L * 2,
        referenceNumber = "CK-WDL-984210"
      ),
      WithdrawalRequest(
        id = "W-102",
        amount = 3500.0,
        destination = "UPI: sunil.clean@upi",
        status = "Paid",
        requestedAt = System.currentTimeMillis() - 86400000L * 6,
        referenceNumber = "CK-WDL-841920"
      )
    )
  )
  val withdrawals: StateFlow<List<WithdrawalRequest>> = _withdrawals.asStateFlow()

  private val _notifications = MutableStateFlow<List<NotificationItem>>(
    listOf(
      NotificationItem(
        id = "N-1",
        title = "New Booking Assigned!",
        body = "Full Villa Deep Cleaning at Oberoi Springs. Please review and accept within 60s.",
        timestamp = System.currentTimeMillis() - 60000L * 5,
        type = "JOB_DISPATCH"
      ),
      NotificationItem(
        id = "N-2",
        title = "Weekly Payout Credited",
        body = "₹14,800 has been transferred to your HDFC Bank Account. Ref: CK-WDL-984210.",
        timestamp = System.currentTimeMillis() - 86400000L * 2,
        type = "EARNINGS"
      ),
      NotificationItem(
        id = "N-3",
        title = "Cyber Security Scan Passed",
        body = "Your partner app integrity score is 98/100. Anti-theft protection active.",
        timestamp = System.currentTimeMillis() - 86400000L * 1,
        type = "SECURITY"
      )
    )
  )
  val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

  init {
    seedInitialDataIfEmpty()
  }

  fun setOnlineStatus(online: Boolean) {
    _isOnline.value = online
    recordAuditLog(
      eventType = "AVAILABILITY_CHANGE",
      details = if (online) "Partner went ONLINE (Accepting jobs)" else "Partner went OFFLINE"
    )
  }

  // --- Jobs Stream ---
  val allJobs: Flow<List<Job>> = jobDao.getAllJobs().map { entities ->
    entities.map { entityToDomain(it) }
  }

  // --- Leaves Stream ---
  val allLeaves: Flow<List<CalendarLeaveEntry>> = leaveDao.getAllLeaves().map { entities ->
    entities.map {
      CalendarLeaveEntry(
        id = it.id,
        date = it.date,
        reason = it.reason,
        isFullDay = it.isFullDay
      )
    }
  }

  // --- Audit Logs Stream ---
  val auditLogs: Flow<List<AuditLogEntry>> = auditDao.getAuditLogs().map { entities ->
    entities.map {
      AuditLogEntry(
        id = it.id,
        timestamp = it.timestamp,
        eventType = it.eventType,
        details = it.details,
        severity = it.severity
      )
    }
  }

  fun updateJobStatus(jobId: String, newStatus: JobStatus) {
    repositoryScope.launch {
      val existing = jobDao.getJobById(jobId) ?: return@launch
      val updated = existing.copy(status = newStatus.name)
      jobDao.insertJob(updated)

      recordAuditLog(
        eventType = "JOB_STATUS_CHANGE",
        details = "Job #$jobId transitioned from ${existing.status} to ${newStatus.name}"
      )

      if (firebaseService.isAvailable()) {
        firebaseService.updateBookingStatus(jobId, _profile.value.id, newStatus)
      }

      if (newStatus == JobStatus.COMPLETED) {
        _earnings.value = _earnings.value.copy(
          todayEarnings = _earnings.value.todayEarnings + existing.estimatedEarnings,
          withdrawableBalance = _earnings.value.withdrawableBalance + existing.estimatedEarnings
        )
        addNotification(
          title = "Job Completed Successfully!",
          body = "You earned ₹${existing.estimatedEarnings.toInt()} for Job #${existing.id}.",
          type = "EARNINGS"
        )
      }
    }
  }

  fun addPhotoToJob(jobId: String, photoUri: String, isBefore: Boolean) {
    repositoryScope.launch {
      val existing = jobDao.getJobById(jobId) ?: return@launch
      val jobDomain = entityToDomain(existing)
      val updated = if (isBefore) {
        jobDomain.copy(beforePhotos = jobDomain.beforePhotos + photoUri)
      } else {
        jobDomain.copy(afterPhotos = jobDomain.afterPhotos + photoUri)
      }
      jobDao.insertJob(domainToEntity(updated))
      recordAuditLog(
        eventType = "JOB_PHOTO_UPLOAD",
        details = "Job #$jobId: Added ${if (isBefore) "Before" else "After"} verification photo"
      )
    }
  }

  fun submitServiceChangeRequest(
    jobId: String,
    serviceItem: String,
    requestedAmount: Double,
    partnerReason: String
  ) {
    repositoryScope.launch {
      val existing = jobDao.getJobById(jobId) ?: return@launch
      val jobDomain = entityToDomain(existing)

      val changeReq = ServiceChangeRequest(
        id = "SCR-" + UUID.randomUUID().toString().take(6),
        jobId = jobId,
        serviceItem = serviceItem,
        requestedAmount = requestedAmount,
        partnerReason = partnerReason,
        status = ApprovalStatus.PENDING_APPROVAL
      )

      val updated = jobDomain.copy(
        serviceChangeRequests = jobDomain.serviceChangeRequests + changeReq
      )
      jobDao.insertJob(domainToEntity(updated))

      recordAuditLog(
        eventType = "SERVICE_CHANGE_REQUESTED",
        details = "Job #$jobId: Requested extra ₹${requestedAmount.toInt()} for '$serviceItem'. Status: Pending Admin Approval"
      )

      if (firebaseService.isAvailable()) {
        firebaseService.submitServiceChangeRequest(
          bookingId = jobId,
          partnerId = _profile.value.id,
          serviceItem = serviceItem,
          requestedAmount = requestedAmount,
          partnerReason = partnerReason
        )
      }

      addNotification(
        title = "Service Change Submitted",
        body = "Request for '$serviceItem' (+₹${requestedAmount.toInt()}) submitted for Job #$jobId. Pending Admin Approval.",
        type = "JOB_DISPATCH"
      )
    }
  }

  fun simulateAdminApproveChangeRequest(jobId: String, requestId: String) {
    repositoryScope.launch {
      val existing = jobDao.getJobById(jobId) ?: return@launch
      val jobDomain = entityToDomain(existing)
      var extraEarnings = 0.0

      val updatedRequests = jobDomain.serviceChangeRequests.map { req ->
        if (req.id == requestId) {
          extraEarnings = req.requestedAmount * 0.70 // Partner gets 70% share
          req.copy(status = ApprovalStatus.APPROVED_BY_ADMIN, adminRemark = "Approved by Operations Team")
        } else req
      }

      val updated = jobDomain.copy(
        serviceChangeRequests = updatedRequests,
        companyPrice = jobDomain.companyPrice + (extraEarnings / 0.70),
        estimatedEarnings = jobDomain.estimatedEarnings + extraEarnings
      )
      jobDao.insertJob(domainToEntity(updated))

      recordAuditLog(
        eventType = "SERVICE_CHANGE_APPROVED",
        details = "Job #$jobId: Admin approved request #$requestId (+₹${extraEarnings.toInt()} earnings)"
      )
      addNotification(
        title = "Service Change Approved!",
        body = "Admin approved your service change request on Job #$jobId. Earnings updated.",
        type = "EARNINGS"
      )
    }
  }

  fun cancelJob(jobId: String, reason: String) {
    repositoryScope.launch {
      val existing = jobDao.getJobById(jobId) ?: return@launch
      val jobDomain = entityToDomain(existing)
      val updated = jobDomain.copy(
        status = JobStatus.CANCELLED,
        cancellationReason = reason
      )
      jobDao.insertJob(domainToEntity(updated))
      recordAuditLog(
        eventType = "JOB_CANCELLED",
        details = "Job #$jobId cancelled by partner. Reason: $reason"
      )
    }
  }

  fun rescheduleJob(jobId: String, newDate: String) {
    repositoryScope.launch {
      val existing = jobDao.getJobById(jobId) ?: return@launch
      val jobDomain = entityToDomain(existing)
      val updated = jobDomain.copy(
        status = JobStatus.RESCHEDULED,
        rescheduledDate = newDate
      )
      jobDao.insertJob(domainToEntity(updated))
      recordAuditLog(
        eventType = "JOB_RESCHEDULED",
        details = "Job #$jobId rescheduled to $newDate"
      )
    }
  }

  fun requestWithdrawal(amount: Double, destination: String): Boolean {
    if (amount <= 0 || amount > _earnings.value.withdrawableBalance) return false
    val newReq = WithdrawalRequest(
      id = "W-" + UUID.randomUUID().toString().take(6),
      amount = amount,
      destination = destination,
      status = "Processing",
      requestedAt = System.currentTimeMillis()
    )
    _withdrawals.value = listOf(newReq) + _withdrawals.value
    _earnings.value = _earnings.value.copy(
      withdrawableBalance = _earnings.value.withdrawableBalance - amount,
      pendingBalance = _earnings.value.pendingBalance + amount
    )
    recordAuditLog(
      eventType = "WITHDRAWAL_REQUEST",
      details = "Withdrawal request of ₹${amount.toInt()} to $destination. Ref: ${newReq.referenceNumber}"
    )
    addNotification(
      title = "Withdrawal Submitted",
      body = "Your request of ₹${amount.toInt()} is processing (Ref: ${newReq.referenceNumber}).",
      type = "EARNINGS"
    )
    return true
  }

  fun applyLeave(date: String, reason: String) {
    repositoryScope.launch {
      val id = "LV-" + UUID.randomUUID().toString().take(6)
      leaveDao.insertLeave(
        LeaveEntity(
          id = id,
          date = date,
          reason = reason,
          isFullDay = true
        )
      )
      recordAuditLog(
        eventType = "LEAVE_APPLIED",
        details = "Partner marked leave on $date: $reason"
      )
    }
  }

  fun deleteLeave(id: String) {
    repositoryScope.launch {
      leaveDao.deleteLeave(id)
    }
  }

  fun submitKycUpdate(
    aadhaarNumber: String,
    panNumber: String,
    bankAccount: String,
    ifsc: String,
    upi: String
  ) {
    _profile.value = _profile.value.copy(
      kycStatus = KycStatus.VERIFIED,
      aadhaarMasked = "XXXX-XXXX-" + aadhaarNumber.takeLast(4),
      panMasked = "XXXXX" + panNumber.takeLast(4),
      bankAccountMasked = "A/C: *******" + bankAccount.takeLast(4),
      ifscCode = ifsc,
      upiIdMasked = upi
    )
    recordAuditLog(
      eventType = "KYC_SUBMISSION",
      details = "KYC profile and payout credentials updated and verified."
    )
    addNotification(
      title = "KYC Verified!",
      body = "Your documents have been verified. Payouts are fully active.",
      type = "KYC"
    )
  }

  fun requestAccountDeletion(): String {
    val ticketId = "DEL-REQ-" + (100000..999999).random()
    recordAuditLog(
      eventType = "ACCOUNT_DELETION_REQUEST",
      details = "Partner initiated account and data deletion request (Ticket: $ticketId)"
    )
    if (firebaseService.isAvailable()) {
      repositoryScope.launch {
        firebaseService.submitAccountDeletionRequest(
          partnerId = _profile.value.id,
          reason = "Partner requested account deletion via App settings"
        )
      }
    }
    return ticketId
  }

  fun recordAuditLog(eventType: String, details: String, severity: String = "INFO") {
    repositoryScope.launch {
      val log = AuditLogEntity(
        id = "AUD-" + UUID.randomUUID().toString().take(8),
        timestamp = System.currentTimeMillis(),
        eventType = eventType,
        details = details,
        severity = severity
      )
      auditDao.insertLog(log)
    }
  }

  fun addNotification(title: String, body: String, type: String) {
    val item = NotificationItem(
      id = "N-" + UUID.randomUUID().toString().take(6),
      title = title,
      body = body,
      timestamp = System.currentTimeMillis(),
      type = type,
      isRead = false
    )
    _notifications.value = listOf(item) + _notifications.value
  }

  fun markAllNotificationsRead() {
    _notifications.value = _notifications.value.map { it.copy(isRead = true) }
  }

  private fun seedInitialDataIfEmpty() {
    repositoryScope.launch {
      val sampleJobs = listOf(
        Job(
          id = "CK-8421",
          customerName = "Ananya Sharma",
          customerPhoneMasked = "+91 1800 253 2657,,8421#",
          serviceTitle = "Full Deep Villa Cleaning",
          packageType = "4BHK Premium Sanitization + Balcony Jet Wash",
          date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
          timeSlot = "10:30 AM - 02:30 PM",
          address = "B-1402, Oberoi Springs, Off Link Road, Andheri West, Mumbai",
          latitude = 19.1412,
          longitude = 72.8335,
          instructions = "Please ring bell twice. We have two indoor cats; please use pet-safe non-toxic cleaners.",
          status = JobStatus.ASSIGNED,
          estimatedEarnings = 2850.0,
          companyPrice = 4200.0,
          paymentMode = "Prepaid Online (Safe Escrow)"
        ),
        Job(
          id = "CK-8419",
          customerName = "Rohit Verma",
          customerPhoneMasked = "+91 1800 253 2657,,8419#",
          serviceTitle = "Sofa & Carpet Shampooing",
          packageType = "5-Seater L-Shape Sofa + 2 Persian Rugs",
          date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
          timeSlot = "03:30 PM - 05:30 PM",
          address = "A-601, Raheja Classique, Lokhandwala, Andheri West, Mumbai",
          latitude = 19.1385,
          longitude = 72.8290,
          instructions = "Bring heavy suction vacuum. Dedicated parking available for partner vehicle on Basement 1.",
          status = JobStatus.ACCEPTED,
          estimatedEarnings = 1450.0,
          companyPrice = 2100.0,
          paymentMode = "Prepaid Online"
        ),
        Job(
          id = "CK-8402",
          customerName = "Kavita Rao",
          customerPhoneMasked = "+91 1800 253 2657,,8402#",
          serviceTitle = "Kitchen Degreasing & Chimney Cleaning",
          packageType = "Modular Kitchen Deep Scrub + Oil Trap Flush",
          date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
            Date(System.currentTimeMillis() - 86400000L)
          ),
          timeSlot = "11:00 AM - 01:00 PM",
          address = "Flat 304, Green Heights, Versova, Mumbai",
          latitude = 19.1311,
          longitude = 72.8155,
          instructions = "Special focus on gas stove tiles and chimney filters.",
          status = JobStatus.COMPLETED,
          estimatedEarnings = 1150.0,
          companyPrice = 1650.0,
          paymentMode = "Prepaid Online"
        )
      )

      sampleJobs.forEach { job ->
        val existing = jobDao.getJobById(job.id)
        if (existing == null) {
          jobDao.insertJob(domainToEntity(job))
        }
      }

      recordAuditLog(
        eventType = "APP_STARTUP",
        details = "Partner app started. Security sandbox and local database synchronized."
      )
    }
  }

  // --- Entity Mapping Helpers ---
  private fun domainToEntity(job: Job): JobEntity {
    val beforeArr = JSONArray()
    job.beforePhotos.forEach { beforeArr.put(it) }

    val afterArr = JSONArray()
    job.afterPhotos.forEach { afterArr.put(it) }

    val changesArr = JSONArray()
    job.serviceChangeRequests.forEach { req ->
      val obj = JSONObject().apply {
        put("id", req.id)
        put("jobId", req.jobId)
        put("serviceItem", req.serviceItem)
        put("requestedAmount", req.requestedAmount)
        put("partnerReason", req.partnerReason)
        put("status", req.status.name)
        put("adminRemark", req.adminRemark ?: "")
        put("requestTimestamp", req.requestTimestamp)
      }
      changesArr.put(obj)
    }

    return JobEntity(
      id = job.id,
      customerName = job.customerName,
      customerPhoneMasked = job.customerPhoneMasked,
      serviceTitle = job.serviceTitle,
      packageType = job.packageType,
      date = job.date,
      timeSlot = job.timeSlot,
      address = job.address,
      latitude = job.latitude,
      longitude = job.longitude,
      instructions = job.instructions,
      status = job.status.name,
      estimatedEarnings = job.estimatedEarnings,
      companyPrice = job.companyPrice,
      paymentMode = job.paymentMode,
      beforePhotosJson = beforeArr.toString(),
      afterPhotosJson = afterArr.toString(),
      serviceChangesJson = changesArr.toString(),
      cancellationReason = job.cancellationReason,
      rescheduledDate = job.rescheduledDate,
      assignedTimestamp = job.assignedTimestamp
    )
  }

  private fun entityToDomain(entity: JobEntity): Job {
    val beforeList = mutableListOf<String>()
    if (entity.beforePhotosJson.isNotEmpty()) {
      val arr = JSONArray(entity.beforePhotosJson)
      for (i in 0 until arr.length()) beforeList.add(arr.getString(i))
    }

    val afterList = mutableListOf<String>()
    if (entity.afterPhotosJson.isNotEmpty()) {
      val arr = JSONArray(entity.afterPhotosJson)
      for (i in 0 until arr.length()) afterList.add(arr.getString(i))
    }

    val changesList = mutableListOf<ServiceChangeRequest>()
    if (entity.serviceChangesJson.isNotEmpty()) {
      val arr = JSONArray(entity.serviceChangesJson)
      for (i in 0 until arr.length()) {
        val obj = arr.getJSONObject(i)
        changesList.add(
          ServiceChangeRequest(
            id = obj.getString("id"),
            jobId = obj.getString("jobId"),
            serviceItem = obj.getString("serviceItem"),
            requestedAmount = obj.getDouble("requestedAmount"),
            partnerReason = obj.getString("partnerReason"),
            status = ApprovalStatus.valueOf(obj.getString("status")),
            adminRemark = obj.optString("adminRemark").ifEmpty { null },
            requestTimestamp = obj.optLong("requestTimestamp", System.currentTimeMillis())
          )
        )
      }
    }

    return Job(
      id = entity.id,
      customerName = entity.customerName,
      customerPhoneMasked = entity.customerPhoneMasked,
      serviceTitle = entity.serviceTitle,
      packageType = entity.packageType,
      date = entity.date,
      timeSlot = entity.timeSlot,
      address = entity.address,
      latitude = entity.latitude,
      longitude = entity.longitude,
      instructions = entity.instructions,
      status = try { JobStatus.valueOf(entity.status) } catch (e: Exception) { JobStatus.ASSIGNED },
      estimatedEarnings = entity.estimatedEarnings,
      companyPrice = entity.companyPrice,
      paymentMode = entity.paymentMode,
      beforePhotos = beforeList,
      afterPhotos = afterList,
      serviceChangeRequests = changesList,
      cancellationReason = entity.cancellationReason,
      rescheduledDate = entity.rescheduledDate,
      assignedTimestamp = entity.assignedTimestamp
    )
  }
}
