package com.example.data.repository

import android.content.Context
import com.example.data.model.AuditLogEntry
import com.example.data.model.Job
import com.example.data.model.JobStatus
import com.example.data.model.NotificationItem
import com.example.data.model.PartnerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PartnerRepository(private val context: Context) {

  private val _jobs = MutableStateFlow<List<Job>>(initialJobs())
  val jobs: StateFlow<List<Job>> = _jobs.asStateFlow()

  private val _profile = MutableStateFlow(PartnerProfile())
  val profile: StateFlow<PartnerProfile> = _profile.asStateFlow()

  private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
  val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

  private val _auditLogs = MutableStateFlow<List<AuditLogEntry>>(emptyList())
  val auditLogs: StateFlow<List<AuditLogEntry>> = _auditLogs.asStateFlow()

  fun updateJobStatus(jobId: String, newStatus: JobStatus) {
    _jobs.value = _jobs.value.map { job ->
      if (job.id == jobId) job.copy(status = newStatus) else job
    }
    recordAuditLog("JOB_STATUS_CHANGE", "Job $jobId status changed to ${newStatus.name}")
  }

  fun updateOnlineStatus(isOnline: Boolean) {
    _profile.value = _profile.value.copy(isOnline = isOnline)
    recordAuditLog("PARTNER_ONLINE_STATUS", "Online status changed to $isOnline")
  }

  fun addNotification(title: String, body: String, type: String = "GENERAL") {
    val item = NotificationItem(title = title, body = body, type = type)
    _notifications.value = listOf(item) + _notifications.value
  }

  fun recordAuditLog(eventType: String, details: String) {
    val entry = AuditLogEntry(eventType = eventType, details = details)
    _auditLogs.value = listOf(entry) + _auditLogs.value
  }

  private fun initialJobs(): List<Job> {
    return listOf(
      Job(
        id = "CK-8421",
        customerName = "Pooja Verma",
        customerPhoneMasked = "+91 98*** **456",
        serviceTitle = "Full Home Deep Cleaning (2 BHK)",
        packageType = "Premium Service",
        date = "2026-09-24",
        timeSlot = "09:00 AM - 12:00 PM",
        address = "A-402, Sea Green Heights, Versova, Andheri West, Mumbai",
        status = JobStatus.ASSIGNED,
        estimatedEarnings = 1450.0,
        companyPrice = 2200.0,
        instructions = "Customer requested special focus on kitchen tile grout and bathroom sanitation."
      ),
      Job(
        id = "CK-8419",
        customerName = "Rahul Mehta",
        customerPhoneMasked = "+91 97*** **890",
        serviceTitle = "Kitchen & Chimney Degreasing",
        packageType = "Standard Service",
        date = "2026-09-24",
        timeSlot = "02:00 PM - 05:00 PM",
        address = "Flat 12B, Sunshine Tower, Lokhandwala Complex, Andheri West",
        status = JobStatus.ASSIGNED,
        estimatedEarnings = 780.0,
        companyPrice = 1150.0,
        instructions = "Please bring extra degreaser solvent."
      ),
      Job(
        id = "CK-8430",
        customerName = "Ananya Desai",
        customerPhoneMasked = "+91 99*** **231",
        serviceTitle = "Sofa & Upholstery Shampooing",
        packageType = "Express Service",
        date = "2026-09-25",
        timeSlot = "10:30 AM - 01:30 PM",
        address = "B-101, Palm Beach Apt, Juhu Tara Road, Mumbai",
        status = JobStatus.ASSIGNED,
        estimatedEarnings = 920.0,
        companyPrice = 1350.0,
        instructions = "5-seater fabric sofa set."
      )
    )
  }
}
