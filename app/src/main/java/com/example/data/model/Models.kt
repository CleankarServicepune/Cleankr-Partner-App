package com.example.data.model

enum class JobStatus {
  ASSIGNED,
  ACCEPTED,
  ON_THE_WAY,
  ARRIVED,
  STARTED,
  COMPLETED,
  CANCELLED,
  RESCHEDULED
}

enum class KycStatus {
  PENDING,
  SUBMITTED,
  VERIFIED,
  ACTION_REQUIRED
}

enum class ApprovalStatus {
  PENDING_APPROVAL,
  APPROVED_BY_ADMIN,
  REJECTED_BY_ADMIN
}

enum class ThreatLevel {
  LOW,
  MEDIUM,
  HIGH,
  CRITICAL
}

data class ServiceChangeRequest(
  val id: String,
  val jobId: String,
  val serviceItem: String,
  val requestedAmount: Double,
  val partnerReason: String,
  val status: ApprovalStatus = ApprovalStatus.PENDING_APPROVAL,
  val adminRemark: String? = null,
  val requestTimestamp: Long = System.currentTimeMillis()
)

data class Job(
  val id: String,
  val customerName: String,
  val customerPhoneMasked: String, // Virtual relay bridge, real number never exposed
  val serviceTitle: String,
  val packageType: String,
  val date: String,
  val timeSlot: String,
  val address: String,
  val latitude: Double = 19.1363,
  val longitude: Double = 72.8277,
  val instructions: String,
  val status: JobStatus,
  val estimatedEarnings: Double,
  val companyPrice: Double,
  val paymentMode: String = "Online Prepaid",
  val beforePhotos: List<String> = emptyList(),
  val afterPhotos: List<String> = emptyList(),
  val serviceChangeRequests: List<ServiceChangeRequest> = emptyList(),
  val cancellationReason: String? = null,
  val rescheduledDate: String? = null,
  val assignedTimestamp: Long = System.currentTimeMillis()
)

data class PartnerProfile(
  val id: String = "CK-PT-9041",
  val name: String = "Sunil Sharma",
  val phoneMasked: String = "+91 98*** **412",
  val email: String = "sunil.partner@cleankr.in",
  val rating: Float = 4.92f,
  val totalJobsCompleted: Int = 184,
  val acceptanceRate: Int = 98,
  val tier: String = "Diamond Partner",
  val kycStatus: KycStatus = KycStatus.VERIFIED,
  val aadhaarMasked: String = "XXXX-XXXX-4812",
  val panMasked: String = "XXXXX8912F",
  val policeVerificationStatus: String = "Verified & Approved",
  val bankAccountMasked: String = "HDFC Bank (A/C: *******4921)",
  val ifscCode: String = "HDFC0001243",
  val upiIdMasked: String = "sunil.clean@upi",
  val emergencyContact: String = "Pooja Sharma (Wife) +91 97*** **118",
  val vehicleType: String = "Two Wheeler (MH 02 CK 4821)",
  val serviceCategories: List<String> = listOf(
    "Deep Home Cleaning",
    "Sofa & Carpet Shampooing",
    "Kitchen Degreasing",
    "Bathroom Disinfection",
    "Water Tank Cleaning"
  )
)

data class EarningSummary(
  val todayEarnings: Double = 2450.0,
  val weekEarnings: Double = 14800.0,
  val monthEarnings: Double = 52400.0,
  val withdrawableBalance: Double = 8650.0,
  val pendingBalance: Double = 1850.0,
  val totalPaidOut: Double = 43750.0
)

data class WithdrawalRequest(
  val id: String,
  val amount: Double,
  val destination: String,
  val status: String = "Processing",
  val requestedAt: Long = System.currentTimeMillis(),
  val referenceNumber: String = "CK-WDL-" + (100000..999999).random()
)

data class CalendarLeaveEntry(
  val id: String,
  val date: String, // YYYY-MM-DD
  val reason: String,
  val isFullDay: Boolean = true
)

data class SecurityThreat(
  val id: String,
  val title: String,
  val description: String,
  val severity: ThreatLevel
)

data class SecurityCheckResult(
  val integrityScore: Int = 98,
  val isRootDetected: Boolean = false,
  val isTampered: Boolean = false,
  val isEmulator: Boolean = false,
  val tlsPinningActive: Boolean = true,
  val appLockEnabled: Boolean = true,
  val activeSessionsCount: Int = 1,
  val threatsFound: List<SecurityThreat> = emptyList(),
  val lastScanTime: Long = System.currentTimeMillis()
)

data class AuditLogEntry(
  val id: String,
  val timestamp: Long,
  val eventType: String,
  val details: String,
  val severity: String = "INFO"
)

data class NotificationItem(
  val id: String,
  val title: String,
  val body: String,
  val timestamp: Long,
  val type: String, // JOB_DISPATCH, EARNINGS, SECURITY, KYC
  val isRead: Boolean = false
)
