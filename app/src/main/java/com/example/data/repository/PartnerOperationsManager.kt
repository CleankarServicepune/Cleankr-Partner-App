package com.example.data.repository

import com.example.data.model.CalendarLeaveEntry
import com.example.data.model.PartnerDaySchedule
import com.example.data.model.TimeSlot
import com.example.data.model.defaultUcSlots
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class PartnerOperationsManager(private val repository: PartnerRepository) {

  private val _schedules = MutableStateFlow<Map<String, PartnerDaySchedule>>(emptyMap())
  val schedules: StateFlow<Map<String, PartnerDaySchedule>> = _schedules.asStateFlow()

  private val _leaves = MutableStateFlow<List<CalendarLeaveEntry>>(emptyList())
  val leaves: StateFlow<List<CalendarLeaveEntry>> = _leaves.asStateFlow()

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

  fun toggleLeaveForDate(date: String, reason: String = "Personal Leave") {
    val existing = _leaves.value.find { it.date == date }
    if (existing != null) {
      _leaves.value = _leaves.value.filter { it.date != date }
      val schedule = getScheduleForDate(date).copy(isDayOff = false, leaveReason = null)
      _schedules.value = _schedules.value + (date to schedule)
      repository.recordAuditLog("LEAVE_CANCELLED", "Cancelled leave for date: $date")
    } else {
      val newLeave = CalendarLeaveEntry(
        id = UUID.randomUUID().toString(),
        date = date,
        reason = reason
      )
      _leaves.value = _leaves.value + newLeave
      val schedule = getScheduleForDate(date).copy(isDayOff = true, leaveReason = reason)
      _schedules.value = _schedules.value + (date to schedule)
      repository.recordAuditLog("LEAVE_APPLIED", "Applied leave for date: $date, reason: $reason")
    }
  }

  fun applyLeave(date: String, reason: String) {
    toggleLeaveForDate(date, reason)
  }

  private fun defaultSlots(): List<TimeSlot> {
    return defaultUcSlots()
  }

  private fun seedInitialSchedules() {
    val initial = mutableMapOf<String, PartnerDaySchedule>()
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
        slots = defaultUcSlots(date)
      )
    }
    _schedules.value = initial
  }
}
