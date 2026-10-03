package com.example.data.model

enum class JobStatus {
  ASSIGNED,
  ON_THE_WAY,
  ARRIVED,
  IN_PROGRESS,
  COMPLETED,
  CANCELLED
}

enum class KycStatus {
  VERIFIED,
  PENDING,
  REJECTED
}

data class ServiceChangeRequest(
  val id: String,
  val description: String,
  val additionalCost: Double,
  val isApproved: Boolean = false
)

data class Job(
  val id: String,
  val customerName: String,
  val customerPhoneMasked: String = "+91 98*** **123",
  val serviceTitle: String,
  val packageType: String = "Deep Cleaning",
  val date: String,
  val timeSlot: String,
  val address: String,
  val latitude: Double = 19.1363,
  val longitude: Double = 72.8277,
  val instructions: String = "Please carry required cleaning supplies.",
  val status: JobStatus = JobStatus.ASSIGNED,
  val estimatedEarnings: Double = 850.0,
  val companyPrice: Double = 1200.0,
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
  val isOnline: Boolean = true,
  val isVerified: Boolean = true,
  val hubName: String = "Andheri West Hub"
)

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

data class CalendarLeaveEntry(
  val id: String,
  val date: String,
  val reason: String,
  val approved: Boolean = true
)

data class BankAccountDetails(
  val accountHolderName: String = "Sunil Sharma",
  val bankName: String = "HDFC Bank",
  val accountNumberMasked: String = "XXXX XXXX 8912",
  val ifscCode: String = "HDFC0001234",
  val upiId: String = "sunilsharma@okaxis"
)

data class NotificationItem(
  val id: String = java.util.UUID.randomUUID().toString(),
  val title: String,
  val body: String,
  val type: String = "GENERAL",
  val timestamp: Long = System.currentTimeMillis(),
  val isRead: Boolean = false
)

data class AuditLogEntry(
  val id: String = java.util.UUID.randomUUID().toString(),
  val eventType: String,
  val details: String,
  val timestamp: Long = System.currentTimeMillis()
)
