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
  ),
  val bankAccountDetails: BankAccountDetails = BankAccountDetails(),
  val nsdcDetails: NsdcCertificateDetails = NsdcCertificateDetails(
    certificateNumber = "NSDC-HKS-2024-9412",
    skillDomain = "Domestic Housekeeping & Sanitization (NSDC / Skill India)",
    verificationStatus = NsdcVerificationStatus.FORMAT_VALID_PENDING_GOVT_SYNC,
    issuedDate = "2024-03-15",
    officialVerificationRemarks = "Format check passed (Skill India / NSDC). Awaiting official backend portal synchronization."
  ),
  val assignedHub: CleankrHub = CleankrHub()
)

data class CleankrHub(
  val hubId: String = "hub_mumbai_andheri_west",
  val hubName: String = "Cleankr Central Operations Hub - Andheri West",
  val hubCode: String = "CLK-HUB-01",
  val address: String = "Plot 42, Lotus Business Park, Off Link Road, Andheri West, Mumbai, Maharashtra 400053",
  val city: String = "Mumbai",
  val zone: String = "West Zone",
  val hubManagerName: String = "Rajesh Sharma",
  val hubManagerPhone: String = "+91 98200 11223",
  val hubTimings: String = "07:00 AM - 09:00 PM (Monday to Sunday)",
  val facilities: List<String> = listOf(
    "Heavy Cleaning Equipment Depot",
    "Chemicals & Consumables Restock",
    "Uniforms & Safety Gear Distribution",
    "Skill India / NSDC Certified Training Bay",
    "Machine Maintenance & Quick Repair Desk"
  ),
  val assignedEquipment: List<String> = listOf(
    "Commercial Wet & Dry Vacuum v2 (Serial: CK-VAC-882)",
    "High-Pressure Steam Machine (Serial: CK-STM-341)",
    "Cleankr Eco-Pro Concentrate Chemicals Kit",
    "Microfiber & Telescopic Extension Rods",
    "Certified Safety Harness & Protective Kit"
  ),
  val latitude: Double = 19.1363,
  val longitude: Double = 72.8277,
  val adminAssignedDate: String = "2024-01-10",
  val status: String = "Active Hub"
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

// --- Newly Integrated Availability, Banking & Operations Models ---

enum class SlotPeriod {
  MORNING,
  AFTERNOON,
  EVENING
}

data class TimeSlot(
  val id: String,
  val label: String, // e.g. "08:00 AM – 09:00 AM"
  val period: SlotPeriod = SlotPeriod.MORNING,
  val isEnabled: Boolean = true,
  val bookedJobId: String? = null,
  val isPeakHour: Boolean = false,
  val durationHours: Int = 1
)

fun defaultUcSlots(dateSuffix: String = ""): List<TimeSlot> = listOf(
  TimeSlot(
    id = "slot_1",
    label = "08:00 AM – 09:00 AM",
    period = SlotPeriod.MORNING,
    isEnabled = false,
    bookedJobId = null,
    isPeakHour = false,
    durationHours = 1
  ),
  TimeSlot(
    id = "slot_2",
    label = "09:00 AM – 12:00 PM",
    period = SlotPeriod.MORNING,
    isEnabled = true,
    bookedJobId = if (dateSuffix.endsWith("-24")) "CK-8421" else null,
    isPeakHour = true,
    durationHours = 3
  ),
  TimeSlot(
    id = "slot_3",
    label = "12:00 PM – 01:00 PM",
    period = SlotPeriod.AFTERNOON,
    isEnabled = true,
    bookedJobId = null,
    isPeakHour = false,
    durationHours = 1
  ),
  TimeSlot(
    id = "slot_4",
    label = "01:00 PM – 02:00 PM",
    period = SlotPeriod.AFTERNOON,
    isEnabled = true,
    bookedJobId = null,
    isPeakHour = false,
    durationHours = 1
  ),
  TimeSlot(
    id = "slot_5",
    label = "02:00 PM – 05:00 PM",
    period = SlotPeriod.AFTERNOON,
    isEnabled = true,
    bookedJobId = if (dateSuffix.endsWith("-24")) "CK-8419" else null,
    isPeakHour = false,
    durationHours = 3
  ),
  TimeSlot(
    id = "slot_6",
    label = "05:00 PM – 08:00 PM",
    period = SlotPeriod.EVENING,
    isEnabled = true,
    bookedJobId = null,
    isPeakHour = true,
    durationHours = 3
  )
)

data class PartnerDaySchedule(
  val date: String, // YYYY-MM-DD
  val isDayOff: Boolean = false,
  val leaveReason: String? = null,
  val slots: List<TimeSlot> = defaultUcSlots()
) {
  val totalAvailableHours: Int
    get() = if (isDayOff) 0 else slots.filter { it.isEnabled }.sumOf { it.durationHours }
}

data class BankAccountDetails(
  val accountHolderName: String = "Sunil Sharma",
  val bankName: String = "HDFC Bank",
  val accountNumberMasked: String = "*******4921",
  val ifscCode: String = "HDFC0001243",
  val upiId: String = "sunil.clean@upi",
  val isAccountVerified: Boolean = true
)

enum class NsdcVerificationStatus {
  UNVERIFIED,
  FORMAT_VALID_PENDING_GOVT_SYNC,
  VERIFIED_OFFICIAL,
  REJECTED
}

data class NsdcCertificateDetails(
  val certificateNumber: String = "",
  val skillDomain: String = "Domestic Housekeeping & Sanitization (NSDC / Skill India)",
  val verificationStatus: NsdcVerificationStatus = NsdcVerificationStatus.UNVERIFIED,
  val issuedDate: String? = null,
  val officialVerificationRemarks: String? = null
)

enum class PenaltyRequestStatus {
  SUBMITTED_TO_OPS,
  UNDER_REVIEW,
  APPROVED_CREDITED_TO_WALLET,
  REJECTED
}

data class CustomerPenaltyReport(
  val id: String,
  val jobId: String,
  val partnerId: String,
  val timestamp: Long = System.currentTimeMillis(),
  val waitingTimeMinutes: Int = 15,
  val callsAttemptedCount: Int = 3,
  val doorstepPhotoAttached: Boolean = true,
  val requestedCompensationAmount: Double = 100.0, // Fixed ₹100 policy compensation
  val status: PenaltyRequestStatus = PenaltyRequestStatus.SUBMITTED_TO_OPS,
  val opsRemarks: String? = null
)

