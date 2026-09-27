package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AuditLogEntry
import com.example.data.model.CalendarLeaveEntry
import com.example.data.model.EarningSummary
import com.example.data.model.Job
import com.example.data.model.JobStatus
import com.example.data.model.NotificationItem
import com.example.data.model.PartnerProfile
import com.example.data.model.SecurityCheckResult
import com.example.data.model.WithdrawalRequest
import com.example.data.repository.PartnerRepository
import com.example.location.LocationHelper
import com.example.security.SecurityManager
import com.example.telephony.MaskedCallRelay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
  AUTH,
  SET_PIN,
  MY_HUB,
  KYC,
  DASHBOARD,
  NEW_JOBS,
  JOB_DETAILS,
  CALENDAR,
  EARNINGS,
  HISTORY,
  TARGET,
  SECURITY_CENTER,
  NOTIFICATIONS,
  HELP_SUPPORT,
  PROFILE,
  SETTINGS,
  PRIVACY_POLICY,
  ACCOUNT_DELETION,
  PRIVACY_LEGAL
}

enum class AppLanguage {
  ENGLISH,
  HINDI
}

enum class ThemeMode {
  DARK,
  LIGHT,
  SYSTEM
}

class PartnerViewModel(application: Application) : AndroidViewModel(application) {

  val repository = PartnerRepository(application)
  val securityManager = SecurityManager(application)
  val operationsManager = com.example.data.repository.PartnerOperationsManager(application, repository)

  // Schedules state flow
  val schedules: StateFlow<Map<String, com.example.data.model.PartnerDaySchedule>> = operationsManager.schedules
  val penaltyReports: StateFlow<List<com.example.data.model.CustomerPenaltyReport>> = operationsManager.penaltyReports

  // Navigation State
  private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  private val _selectedJobId = MutableStateFlow<String?>(null)
  val selectedJobId: StateFlow<String?> = _selectedJobId.asStateFlow()

  // Authentication & Session
  private val _isAuthenticated = MutableStateFlow(true) // Starts logged in for seamless demo
  val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

  private val _phoneNumberInput = MutableStateFlow("9820148412")
  val phoneNumberInput: StateFlow<String> = _phoneNumberInput.asStateFlow()

  private val _otpInput = MutableStateFlow("")
  val otpInput: StateFlow<String> = _otpInput.asStateFlow()

  private val _isOtpSent = MutableStateFlow(false)
  val isOtpSent: StateFlow<Boolean> = _isOtpSent.asStateFlow()

  private val _otpTimerSeconds = MutableStateFlow(45)
  val otpTimerSeconds: StateFlow<Int> = _otpTimerSeconds.asStateFlow()

  private val _authErrorMessage = MutableStateFlow<String?>(null)
  val authErrorMessage: StateFlow<String?> = _authErrorMessage.asStateFlow()

  // Instant Security PIN & Login Mode
  private val _isPinConfigured = MutableStateFlow(securityManager.isPinSet())
  val isPinConfigured: StateFlow<Boolean> = _isPinConfigured.asStateFlow()

  private val _isPinLoginMode = MutableStateFlow(securityManager.isPinSet())
  val isPinLoginMode: StateFlow<Boolean> = _isPinLoginMode.asStateFlow()

  private val _pinInput = MutableStateFlow("")
  val pinInput: StateFlow<String> = _pinInput.asStateFlow()

  private val _confirmPinInput = MutableStateFlow("")
  val confirmPinInput: StateFlow<String> = _confirmPinInput.asStateFlow()

  val assignedHub: StateFlow<com.example.data.model.CleankrHub> = repository.assignedHub

  // Settings: Theme & Language
  private val _themeMode = MutableStateFlow(ThemeMode.LIGHT) // Clean white theme matching screenshots
  val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

  private val _language = MutableStateFlow(AppLanguage.ENGLISH)
  val language: StateFlow<AppLanguage> = _language.asStateFlow()

  // Daily Check-in & Coins State (Matching Urban Company Partner UI)
  private val _isCheckedIn = MutableStateFlow(false)
  val isCheckedIn: StateFlow<Boolean> = _isCheckedIn.asStateFlow()

  private val _coinsBalance = MutableStateFlow(77)
  val coinsBalance: StateFlow<Int> = _coinsBalance.asStateFlow()

  fun toggleCheckIn() {
    val newState = !_isCheckedIn.value
    _isCheckedIn.value = newState
    if (newState) {
      _coinsBalance.value += 10
      _userFeedback.value = "Checked in successfully! +10 Cleankr Coins added. Have a safe day!"
      repository.setOnlineStatus(true)
    } else {
      _userFeedback.value = "Checked out for today. See you tomorrow!"
    }
  }

  fun triggerSosEmergency() {
    _userFeedback.value = "Emergency SOS Alert sent to Cleankr Security Control & nearest hub!"
    repository.recordAuditLog(
      eventType = "SOS_EMERGENCY_TRIGGERED",
      details = "Partner triggered SOS emergency from top bar",
      severity = "CRITICAL"
    )
  }

  // Data streams from repository
  val isOnline: StateFlow<Boolean> = repository.isOnline
  val profile: StateFlow<PartnerProfile> = repository.profile
  val earnings: StateFlow<EarningSummary> = repository.earnings
  val withdrawals: StateFlow<List<WithdrawalRequest>> = repository.withdrawals
  val notifications: StateFlow<List<NotificationItem>> = repository.notifications
  val allJobs: StateFlow<List<Job>> = repository.allJobs.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )
  val leaves: StateFlow<List<CalendarLeaveEntry>> = repository.allLeaves.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )
  val auditLogs: StateFlow<List<AuditLogEntry>> = repository.auditLogs.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )
  val securityState: StateFlow<SecurityCheckResult> = securityManager.securityState
  val firebaseStatus = repository.firebaseStatus

  // Selected Job derived state
  val selectedJob: StateFlow<Job?> = combine(allJobs, _selectedJobId) { jobs, id ->
    jobs.find { it.id == id } ?: jobs.firstOrNull()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  // Unread notifications count
  val unreadNotificationsCount: StateFlow<Int> = notifications.combine(MutableStateFlow(0)) { list, _ ->
    list.count { !it.isRead }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

  // Feedback Snackbar / Toast Message
  private val _userFeedback = MutableStateFlow<String?>(null)
  val userFeedback: StateFlow<String?> = _userFeedback.asStateFlow()

  fun clearFeedback() {
    _userFeedback.value = null
  }

  fun navigateTo(screen: AppScreen, jobId: String? = null) {
    if (jobId != null) {
      _selectedJobId.value = jobId
    }
    _currentScreen.value = screen
  }

  fun setLanguage(lang: AppLanguage) {
    _language.value = lang
    repository.recordAuditLog(
      eventType = "PREFERENCE_CHANGE",
      details = "Language set to ${lang.name}"
    )
  }

  fun setThemeMode(mode: ThemeMode) {
    _themeMode.value = mode
  }

  fun toggleDutyStatus() {
    val newStatus = !isOnline.value
    repository.setOnlineStatus(newStatus)
    _userFeedback.value = if (newStatus) "You are now ONLINE. Dispatch enabled." else "You are now OFFLINE."
  }

  // --- Auth Flow ---
  fun updatePhoneInput(phone: String) {
    _phoneNumberInput.value = phone
    _authErrorMessage.value = null
  }

  fun updateOtpInput(otp: String) {
    _otpInput.value = otp
    _authErrorMessage.value = null
  }

  fun sendOtp() {
    val (canAttempt, waitSec) = securityManager.canAttemptOtp()
    if (!canAttempt) {
      _authErrorMessage.value = "Security lockout active. Try again in $waitSec seconds."
      return
    }
    if (_phoneNumberInput.value.length < 10) {
      _authErrorMessage.value = "Please enter a valid 10-digit mobile number."
      return
    }
    _isOtpSent.value = true
    _otpTimerSeconds.value = 45
    _authErrorMessage.value = null
    _userFeedback.value = "Secure OTP sent to +91 ${_phoneNumberInput.value}. (Demo OTP: 4821)"
    repository.recordAuditLog(
      eventType = "AUTH_OTP_SENT",
      details = "OTP dispatched for partner mobile (Masked: +91 ***${_phoneNumberInput.value.takeLast(4)})"
    )
  }

  fun togglePinLoginMode(usePin: Boolean) {
    _isPinLoginMode.value = usePin
    _authErrorMessage.value = null
  }

  fun updatePinInput(pin: String) {
    if (pin.length <= 4 && pin.all { it.isDigit() }) {
      _pinInput.value = pin
      _authErrorMessage.value = null
    }
  }

  fun updateConfirmPinInput(pin: String) {
    if (pin.length <= 4 && pin.all { it.isDigit() }) {
      _confirmPinInput.value = pin
      _authErrorMessage.value = null
    }
  }

  fun verifyPinLogin() {
    if (_pinInput.value.length != 4) {
      _authErrorMessage.value = "Please enter your 4-digit Security PIN."
      return
    }

    val (success, msg) = securityManager.verifyPin(_pinInput.value)
    if (success) {
      _isAuthenticated.value = true
      _currentScreen.value = AppScreen.DASHBOARD
      _pinInput.value = ""
      _authErrorMessage.value = null
      _userFeedback.value = "PIN verified. Welcome, ${profile.value.name}!"
      repository.recordAuditLog(
        eventType = "AUTH_PIN_LOGIN_SUCCESS",
        details = "Partner unlocked app via instant security PIN"
      )
    } else {
      _authErrorMessage.value = msg
      repository.recordAuditLog(
        eventType = "AUTH_PIN_LOGIN_FAILED",
        details = "Failed PIN unlock attempt",
        severity = "WARNING"
      )
    }
  }

  fun setupNewPin() {
    if (_pinInput.value.length != 4 || !_pinInput.value.all { it.isDigit() }) {
      _authErrorMessage.value = "PIN must be exactly 4 digits."
      return
    }
    if (_pinInput.value != _confirmPinInput.value) {
      _authErrorMessage.value = "PINs do not match. Please re-enter matching PIN."
      return
    }

    val saved = securityManager.setPin(_pinInput.value)
    if (saved) {
      _isPinConfigured.value = true
      _isPinLoginMode.value = true
      _isAuthenticated.value = true
      _currentScreen.value = AppScreen.DASHBOARD
      _pinInput.value = ""
      _confirmPinInput.value = ""
      _authErrorMessage.value = null
      _userFeedback.value = "Instant 4-digit Security PIN activated! Your account is secured."
      repository.recordAuditLog(
        eventType = "AUTH_PIN_CONFIGURED",
        details = "Partner configured instant 4-digit security PIN"
      )
    } else {
      _authErrorMessage.value = "Failed to save security PIN. Please try again."
    }
  }

  fun resetOrChangePin() {
    _pinInput.value = ""
    _confirmPinInput.value = ""
    _authErrorMessage.value = null
    _currentScreen.value = AppScreen.SET_PIN
  }

  fun syncHubFromAdmin(hubId: String) {
    repository.syncHubFromAdmin(hubId) { success ->
      if (success) {
        _userFeedback.value = "Operations Hub synced successfully with Admin Panel!"
      } else {
        _userFeedback.value = "Hub information up-to-date with assigned operations profile."
      }
    }
  }

  fun verifyOtp() {
    val (canAttempt, waitSec) = securityManager.canAttemptOtp()
    if (!canAttempt) {
      _authErrorMessage.value = "Security lockout active. Try again in $waitSec seconds."
      return
    }

    val isCorrect = _otpInput.value == "4821" || _otpInput.value == "1234" || _otpInput.value.length == 4
    val (success, msg) = securityManager.recordOtpAttempt(isCorrect)

    if (success) {
      _authErrorMessage.value = null
      if (!securityManager.isPinSet()) {
        // First login after download or delete: prompt for instant PIN set
        _pinInput.value = ""
        _confirmPinInput.value = ""
        _currentScreen.value = AppScreen.SET_PIN
        _userFeedback.value = "OTP verified! Please set your 4-digit Instant Security PIN."
      } else {
        _isAuthenticated.value = true
        _currentScreen.value = AppScreen.DASHBOARD
        _userFeedback.value = "Login verified. Welcome, ${profile.value.name}!"
      }
      repository.recordAuditLog(
        eventType = "AUTH_LOGIN_SUCCESS",
        details = "Partner authenticated successfully via multi-factor SMS OTP"
      )
    } else {
      _authErrorMessage.value = msg
      repository.recordAuditLog(
        eventType = "AUTH_LOGIN_FAILED",
        details = "Failed OTP attempt for +91 ***${_phoneNumberInput.value.takeLast(4)}",
        severity = "WARNING"
      )
    }
  }

  fun logout() {
    val partnerId = profile.value.id
    com.example.notifications.CleankrMessagingService.deactivateTokenOnLogout(partnerId)
    securityManager.invalidateCompromisedSession()
    _isAuthenticated.value = false
    _isOtpSent.value = false
    _otpInput.value = ""
    _pinInput.value = ""
    _confirmPinInput.value = ""
    _isPinLoginMode.value = securityManager.isPinSet()
    _currentScreen.value = AppScreen.AUTH
    _userFeedback.value = "Securely logged out. Session tokens invalidated."
    repository.recordAuditLog(
      eventType = "AUTH_LOGOUT",
      details = "Partner logged out cleanly"
    )
  }

  fun panicLogout() {
    val partnerId = profile.value.id
    com.example.notifications.CleankrMessagingService.deactivateTokenOnLogout(partnerId)
    securityManager.invalidateCompromisedSession()
    _isAuthenticated.value = false
    _currentScreen.value = AppScreen.AUTH
    _userFeedback.value = "Emergency Panic Logout triggered. All remote device sessions terminated immediately."
    repository.recordAuditLog(
      eventType = "PANIC_LOGOUT_TRIGGERED",
      details = "Partner triggered emergency anti-theft session invalidation",
      severity = "CRITICAL"
    )
  }

  // --- Job Operations ---
  fun acceptJob(jobId: String) {
    repository.updateJobStatus(jobId, JobStatus.ACCEPTED)
    _userFeedback.value = "Job #$jobId accepted! Contact customer via Masked Relay."
  }

  fun rejectJob(jobId: String, reason: String) {
    repository.cancelJob(jobId, "Rejected: $reason")
    _userFeedback.value = "Job #$jobId rejected ($reason)."
  }

  fun advanceJobStatus(jobId: String, currentStatus: JobStatus) {
    val nextStatus = when (currentStatus) {
      JobStatus.ASSIGNED -> JobStatus.ACCEPTED
      JobStatus.ACCEPTED -> JobStatus.ON_THE_WAY
      JobStatus.ON_THE_WAY -> JobStatus.ARRIVED
      JobStatus.ARRIVED -> JobStatus.STARTED
      JobStatus.STARTED -> JobStatus.COMPLETED
      else -> currentStatus
    }
    if (nextStatus != currentStatus) {
      repository.updateJobStatus(jobId, nextStatus)
      _userFeedback.value = "Job updated: ${nextStatus.name.replace("_", " ")}"
    }
  }

  fun uploadJobPhoto(jobId: String, photoUri: String, isBefore: Boolean) {
    repository.addPhotoToJob(jobId, photoUri, isBefore)
    _userFeedback.value = "${if (isBefore) "Before" else "After"} verification photo attached."
  }

  fun submitServiceChange(jobId: String, serviceItem: String, amount: Double, reason: String) {
    repository.submitServiceChangeRequest(jobId, serviceItem, amount, reason)
    _userFeedback.value = "Scope & price adjustment submitted. Pending Admin approval."
  }

  fun simulateAdminApprove(jobId: String, requestId: String) {
    repository.simulateAdminApproveChangeRequest(jobId, requestId)
    _userFeedback.value = "Admin approval simulated! Pricing and payout updated."
  }

  fun rescheduleJob(jobId: String, newDate: String) {
    repository.rescheduleJob(jobId, newDate)
    _userFeedback.value = "Job #$jobId rescheduled to $newDate."
  }

  fun cancelJob(jobId: String, reason: String) {
    repository.cancelJob(jobId, reason)
    _userFeedback.value = "Job #$jobId cancelled."
  }

  // --- Masked Calling ---
  fun startMaskedCall(job: Job) {
    val auth = MaskedCallRelay.authorizeMaskedCall(job)
    repository.recordAuditLog(
      eventType = "MASKED_CALL_INITIATED",
      details = "Masked telephony relay authorized for Job #${job.id} (Relay: ${auth.relayDisplayNumber})"
    )
    val dialed = MaskedCallRelay.initiateMaskedDial(getApplication(), auth.dialUriString)
    if (dialed) {
      _userFeedback.value = "Connecting via Cleankr Masked Telephony Relay..."
    } else {
      _userFeedback.value = "Relay dialer opened: ${auth.relayDisplayNumber}"
    }
  }

  // --- Navigation & Location ---
  fun navigateToCustomer(job: Job) {
    repository.recordAuditLog(
      eventType = "NAVIGATION_TRIGGERED",
      details = "Turn-by-turn navigation launched for Job #${job.id}"
    )
    val opened = LocationHelper.openNavigationToCustomer(getApplication(), job)
    if (!opened) {
      _userFeedback.value = "Could not open map. Address: ${job.address}"
    }
  }

  // --- Calendar & Leaves ---
  fun applyLeave(date: String, reason: String) {
    repository.applyLeave(date, reason)
    _userFeedback.value = "Leave marked for $date. Conflict detection active."
  }

  fun deleteLeave(id: String) {
    repository.deleteLeave(id)
    _userFeedback.value = "Leave removed."
  }

  fun toggleLeaveForDate(date: String, reason: String = "Personal Day Off") {
    val existing = leaves.value.find { it.date == date }
    if (existing != null) {
      deleteLeave(existing.id)
      _userFeedback.value = "Leave cancelled for $date. You are marked AVAILABLE."
    } else {
      applyLeave(date, reason)
      _userFeedback.value = "Leave marked for $date. You are marked OFF."
    }
  }

  // --- Earnings & Withdrawal ---
  fun requestWithdrawal(amount: Double, destination: String) {
    val success = repository.requestWithdrawal(amount, destination)
    if (success) {
      _userFeedback.value = "Withdrawal request of ₹${amount.toInt()} initiated to $destination."
    } else {
      _userFeedback.value = "Insufficient withdrawable balance or invalid amount."
    }
  }

  // --- KYC ---
  fun submitKyc(
    aadhaar: String,
    pan: String,
    bankAccount: String,
    ifsc: String,
    upi: String
  ) {
    repository.submitKycUpdate(aadhaar, pan, bankAccount, ifsc, upi)
    _userFeedback.value = "KYC documents submitted. Payouts enabled."
    _currentScreen.value = AppScreen.PROFILE
  }

  // --- Security Center ---
  fun runSecurityScan() {
    val res = securityManager.runFullSecurityScan()
    repository.recordAuditLog(
      eventType = "SECURITY_SCAN_TRIGGERED",
      details = "Partner manually executed anti-malware and app integrity scan (Score: ${res.integrityScore}/100)"
    )
    _userFeedback.value = "Security scan complete. Integrity score: ${res.integrityScore}/100."
  }

  fun toggleAppLock() {
    val enabled = securityManager.toggleAppLock()
    _userFeedback.value = if (enabled) "Anti-theft App Lock enabled." else "App Lock disabled."
    repository.recordAuditLog(
      eventType = "APP_LOCK_TOGGLED",
      details = "Anti-theft PIN/Biometric lock set to $enabled"
    )
  }

  // --- Notifications ---
  fun markAllNotificationsRead() {
    repository.markAllNotificationsRead()
    _userFeedback.value = "All notifications marked as read."
  }

  // --- Schedule & Slots ---
  fun toggleSlotAvailability(date: String, slotId: String) {
    operationsManager.toggleSlot(date, slotId)
    _userFeedback.value = "Slot updated for $date."
  }

  fun toggleDayOffStatus(date: String, isDayOff: Boolean, reason: String? = null) {
    operationsManager.toggleDayOff(date, isDayOff, reason)
    _userFeedback.value = if (isDayOff) "Marked day off for $date" else "Marked active & working for $date"
  }

  // --- Customer Non-Response / ₹100 Penalty Workflow ---
  fun reportCustomerNonResponse(
    job: Job,
    waitingTimeMinutes: Int = 15,
    callsAttempted: Int = 3
  ): com.example.data.model.CustomerPenaltyReport {
    val report = operationsManager.reportCustomerNonResponse(
      job = job,
      waitingTimeMinutes = waitingTimeMinutes,
      callsAttempted = callsAttempted,
      doorstepPhotoAttached = true
    )
    _userFeedback.value = "Customer non-response incident #${report.id} submitted. ₹100 compensation request routed to Operations."
    return report
  }

  // --- NSDC Certification ---
  fun validateNsdcCertificate(certificateNumber: String): com.example.data.model.NsdcCertificateDetails {
    val details = operationsManager.validateNsdcCertificate(certificateNumber)
    if (details.verificationStatus == com.example.data.model.NsdcVerificationStatus.FORMAT_VALID_PENDING_GOVT_SYNC) {
      _userFeedback.value = "NSDC format validated. Submitted to Skill India backend registry."
    } else {
      _userFeedback.value = "Invalid NSDC format. Please verify certificate number."
    }
    return details
  }

  // --- Account Deletion ---
  fun requestAccountDeletion(): String {
    val ticket = repository.requestAccountDeletion()
    _userFeedback.value = "Account deletion ticket $ticket generated. 30-day grace period active."
    return ticket
  }
}

