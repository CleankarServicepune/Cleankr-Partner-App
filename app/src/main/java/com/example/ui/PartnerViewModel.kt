package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CalendarLeaveEntry
import com.example.data.model.Job
import com.example.data.model.JobStatus
import com.example.data.model.PartnerDaySchedule
import com.example.data.model.PartnerProfile
import com.example.data.repository.PartnerOperationsManager
import com.example.data.repository.PartnerRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PartnerViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = PartnerRepository(application)
  private val operationsManager = PartnerOperationsManager(repository)

  val allJobs: StateFlow<List<Job>> = repository.jobs
  val profile: StateFlow<PartnerProfile> = repository.profile
  val schedules: StateFlow<Map<String, PartnerDaySchedule>> = operationsManager.schedules
  val leaves: StateFlow<List<CalendarLeaveEntry>> = operationsManager.leaves

  private val _currentScreen = MutableStateFlow(AppScreen.HOME)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  private val _isCheckedIn = MutableStateFlow(false)
  val isCheckedIn: StateFlow<Boolean> = _isCheckedIn.asStateFlow()

  private val _dispatchStatus = MutableStateFlow<String?>(null) // null = pending, "ACCEPTED", "PASSED"
  val dispatchStatus: StateFlow<String?> = _dispatchStatus.asStateFlow()

  private val _countdownSeconds = MutableStateFlow(45)
  val countdownSeconds: StateFlow<Int> = _countdownSeconds.asStateFlow()

  private val _selectedOngoingTab = MutableStateFlow(0) // 0=Upcoming, 1=Pending, 2=Completed, 3=Cancelled
  val selectedOngoingTab: StateFlow<Int> = _selectedOngoingTab.asStateFlow()

  private val _selectedLanguage = MutableStateFlow("English")
  val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

  init {
    viewModelScope.launch {
      while (true) {
        delay(1000)
        if (_countdownSeconds.value > 0 && _dispatchStatus.value == null) {
          _countdownSeconds.value -= 1
        }
      }
    }
  }

  fun navigateTo(screen: AppScreen) {
    _currentScreen.value = screen
  }

  fun setSelectedOngoingTab(tabIndex: Int) {
    _selectedOngoingTab.value = tabIndex
  }

  fun setLanguage(lang: String) {
    _selectedLanguage.value = lang
  }

  fun toggleCheckIn() {
    _isCheckedIn.value = !_isCheckedIn.value
    repository.recordAuditLog(
      eventType = "CHECK_IN_TOGGLED",
      details = "Partner check-in status: ${_isCheckedIn.value}"
    )
  }

  fun acceptDispatchOrder() {
    _dispatchStatus.value = "ACCEPTED"
    repository.recordAuditLog("DISPATCH_ACCEPTED", "Partner accepted Full Deep Villa Cleaning")
  }

  fun passDispatchOrder() {
    _dispatchStatus.value = "PASSED"
    repository.recordAuditLog("DISPATCH_PASSED", "Partner passed dispatch order")
  }

  fun toggleSlotAvailability(date: String, slotId: String) {
    operationsManager.toggleSlot(date, slotId)
  }

  fun toggleLeaveForDate(date: String, reason: String) {
    operationsManager.toggleLeaveForDate(date, reason)
  }

  fun applyLeave(date: String, reason: String) {
    operationsManager.applyLeave(date, reason)
  }
}
