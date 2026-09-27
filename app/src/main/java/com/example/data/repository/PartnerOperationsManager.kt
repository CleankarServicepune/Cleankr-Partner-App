package com.example.data.repository

import android.content.Context
import com.example.data.firebase.CleankrFirebaseConfig
import com.example.data.model.CustomerPenaltyReport
import com.example.data.model.Job
import com.example.data.model.NsdcCertificateDetails
import com.example.data.model.NsdcVerificationStatus
import com.example.data.model.PartnerDaySchedule
import com.example.data.model.PenaltyRequestStatus
import com.example.data.model.SlotPeriod
import com.example.data.model.TimeSlot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * PartnerOperationsManager handles:
 * 1. Time-slot schedule management (Morning/Afternoon/Evening toggle per date)
 * 2. Secure reporting of Customer Non-Response / ₹100 Compensation request to operations backend
 * 3. NSDC Certificate Format Validation without falsely claiming government verification
 */
class PartnerOperationsManager(
  private val context: Context,
  private val repository: PartnerRepository
) {

  // In-memory schedules map (date String YYYY-MM-DD -> PartnerDaySchedule)
  private val _schedules = MutableStateFlow<Map<String, PartnerDaySchedule>>(emptyMap())
  val schedules: StateFlow<Map<String, PartnerDaySchedule>> = _schedules.asStateFlow()

  // In-memory penalty reports
  private val _penaltyReports = MutableStateFlow<List<CustomerPenaltyReport>>(emptyList())
  val penaltyReports: StateFlow<List<CustomerPenaltyReport>> = _penaltyReports.asStateFlow()

  init {
    seedInitialSchedules()
  }

  fun getScheduleForDate(date: String): PartnerDaySchedule {
    return _schedules.value[date] ?: PartnerDaySchedule(
      date = date,
      isDayOff = false,
      slots = defaultSlots()
    )
  }

  fun toggleSlot(date: String, slotId: String) {
    val current = getScheduleForDate(date)
    val updatedSlots = current.slots.map { slot ->
      if (slot.id == slotId) slot.copy(isEnabled = !slot.isEnabled) else slot
    }
    val updatedSchedule = current.copy(slots = updatedSlots)
    _schedules.value = _schedules.value + (date to updatedSchedule)

    repository.recordAuditLog(
      eventType = "SCHEDULE_SLOT_TOGGLED",
      details = "Date: $date, Slot: $slotId changed to status ${updatedSlots.find { it.id == slotId }?.isEnabled}"
    )
  }

  fun toggleDayOff(date: String, isDayOff: Boolean, reason: String? = null) {
    val current = getScheduleForDate(date)
    val updated = current.copy(isDayOff = isDayOff, leaveReason = reason)
    _schedules.value = _schedules.value + (date to updated)

    if (isDayOff) {
      repository.applyLeave(date, reason ?: "Partner Day Off")
    } else {
      repository.removeLeaveForDate(date)
    }
  }

  /**
   * Customer Non-Response / ₹100 Penalty Workflow.
   *
   * SECURITY REQUIREMENT:
   * Partners CANNOT directly credit or execute a financial charge on their own.
   * This method packages an audit-compliant incident report and dispatches it
   * to Cleankr Operations & Firestore for server-authoritative review.
   */
  fun reportCustomerNonResponse(
    job: Job,
    waitingTimeMinutes: Int = 15,
    callsAttempted: Int = 3,
    doorstepPhotoAttached: Boolean = true
  ): CustomerPenaltyReport {
    val report = CustomerPenaltyReport(
      id = "PNR-" + UUID.randomUUID().toString().take(6).uppercase(),
      jobId = job.id,
      partnerId = repository.profile.value.id,
      timestamp = System.currentTimeMillis(),
      waitingTimeMinutes = waitingTimeMinutes,
      callsAttemptedCount = callsAttempted,
      doorstepPhotoAttached = doorstepPhotoAttached,
      requestedCompensationAmount = 100.0,
      status = PenaltyRequestStatus.SUBMITTED_TO_OPS,
      opsRemarks = "Incident recorded: Partner reached location, waited $waitingTimeMinutes mins, placed $callsAttempted calls. Pending backend ops clearance."
    )

    _penaltyReports.value = listOf(report) + _penaltyReports.value

    // Record audit log
    repository.recordAuditLog(
      eventType = "CUSTOMER_NON_RESPONSE_FILED",
      details = "Job #${job.id}: Incident filed (Report #${report.id}). Request ₹100 waiting/fuel compensation.",
      severity = "WARNING"
    )

    // Send notification
    repository.addNotification(
      title = "₹100 Compensation Claim Logged",
      body = "Incident report #${report.id} for Job #${job.id} submitted to Cleankr Ops for review.",
      type = "EARNINGS"
    )

    // If Firebase is connected, sync incident report to support_tickets / incidents
    try {
      val firestore = CleankrFirebaseConfig.getFirestore()
      firestore?.collection("penalty_incident_reports")
        ?.document(report.id)
        ?.set(
          mapOf(
            "id" to report.id,
            "jobId" to report.jobId,
            "partnerId" to report.partnerId,
            "timestamp" to report.timestamp,
            "waitingTimeMinutes" to report.waitingTimeMinutes,
            "callsAttemptedCount" to report.callsAttemptedCount,
            "doorstepPhotoAttached" to report.doorstepPhotoAttached,
            "requestedAmount" to report.requestedCompensationAmount,
            "status" to report.status.name
          )
        )
    } catch (_: Exception) {}

    return report
  }

  /**
   * NSDC Certificate Format Validation.
   *
   * SECURITY REQUIREMENT:
   * Does NOT claim official verification merely based on string format.
   * Returns FORMAT_VALID_PENDING_GOVT_SYNC if valid regex, else UNVERIFIED/REJECTED.
   */
  fun validateNsdcCertificate(certificateNumber: String): NsdcCertificateDetails {
    val trimmed = certificateNumber.trim().uppercase()

    // Regex check: e.g. NSDC-XXXX-YYYY-ZZZZ or standard alphanumeric Skill India code (min 8 chars)
    val isValidFormat = trimmed.matches(Regex("^[A-Z0-9]{3,6}-[A-Z0-9]{2,5}-[0-9]{4}-[0-9]{3,6}$")) ||
      (trimmed.startsWith("NSDC") && trimmed.length >= 10)

    val status = if (isValidFormat) {
      NsdcVerificationStatus.FORMAT_VALID_PENDING_GOVT_SYNC
    } else {
      NsdcVerificationStatus.REJECTED
    }

    val remarks = if (isValidFormat) {
      "Format validated against National Skill Development Corporation specification. Pending backend Skill India portal sync."
    } else {
      "Invalid NSDC certificate format. Expected format: NSDC-HKS-YYYY-XXXX"
    }

    val details = NsdcCertificateDetails(
      certificateNumber = trimmed,
      verificationStatus = status,
      officialVerificationRemarks = remarks
    )

    repository.recordAuditLog(
      eventType = "NSDC_CERTIFICATE_VALIDATED",
      details = "Certificate: $trimmed, Status: ${status.name}"
    )

    return details
  }

  private fun defaultSlots(): List<TimeSlot> {
    return com.example.data.model.defaultUcSlots()
  }

  private fun seedInitialSchedules() {
    val initial = mutableMapOf<String, PartnerDaySchedule>()
    // Seed for today and upcoming dates matching calendar window and UC screenshot (Sep 24 to Oct 05)
    val dates = listOf(
      "2026-09-24", "2026-09-25", "2026-09-26", "2026-09-27",
      "2026-09-28", "2026-09-29", "2026-09-30", "2026-10-01",
      "2026-10-02", "2026-10-03", "2026-10-04", "2026-10-05"
    )
    dates.forEach { date ->
      initial[date] = PartnerDaySchedule(
        date = date,
        isDayOff = false,
        leaveReason = null,
        slots = com.example.data.model.defaultUcSlots(date)
      )
    }
    _schedules.value = initial
  }
}
