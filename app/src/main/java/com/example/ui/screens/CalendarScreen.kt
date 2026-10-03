package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ViewDay
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Job
import com.example.data.model.PartnerDaySchedule
import com.example.data.model.defaultUcSlots
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.CleankrHelpFloatingButton
import com.example.ui.components.SlotToggleRow
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrRed
import com.example.ui.theme.CleankrViolet
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class CalendarDayItem(
  val dateStr: String,
  val dayOfWeekShort: String,
  val dayNumber: Int,
  val monthShort: String,
  val dayNameHindi: String,
  val dayNameEnglish: String
)

private fun getOrdinal(day: Int): String {
  return when {
    day in 11..13 -> "${day}th"
    day % 10 == 1 -> "${day}st"
    day % 10 == 2 -> "${day}nd"
    day % 10 == 3 -> "${day}rd"
    else -> "${day}th"
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: PartnerViewModel) {
  val allJobs by viewModel.allJobs.collectAsState()
  val leaves by viewModel.leaves.collectAsState()
  val schedules by viewModel.schedules.collectAsState()

  val daysList = remember {
    val list = mutableListOf<CalendarDayItem>()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val cal = Calendar.getInstance().apply {
      set(Calendar.YEAR, 2026)
      set(Calendar.MONTH, Calendar.SEPTEMBER)
      set(Calendar.DAY_OF_MONTH, 25)
    }
    for (i in 0 until 14) {
      val dateStr = sdf.format(cal.time)
      val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
      val dayNum = cal.get(Calendar.DAY_OF_MONTH)
      val monthShort = if (cal.get(Calendar.MONTH) == Calendar.SEPTEMBER) "Sep" else "Oct"

      val dayShort = when (dayOfWeek) {
        Calendar.SUNDAY -> "SUN"
        Calendar.MONDAY -> "MON"
        Calendar.TUESDAY -> "TUE"
        Calendar.WEDNESDAY -> "WED"
        Calendar.THURSDAY -> "THU"
        Calendar.FRIDAY -> "FRI"
        Calendar.SATURDAY -> "SAT"
        else -> ""
      }

      val hindiDay = when (dayOfWeek) {
        Calendar.SUNDAY -> "Ravivaar"
        Calendar.MONDAY -> "Somvaar"
        Calendar.TUESDAY -> "Mangalvaar"
        Calendar.WEDNESDAY -> "Budhvaar"
        Calendar.THURSDAY -> "Guruvaar"
        Calendar.FRIDAY -> "Shukravaar"
        Calendar.SATURDAY -> "Shanivaar"
        else -> ""
      }

      val engDay = when (dayOfWeek) {
        Calendar.SUNDAY -> "Sunday"
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        else -> ""
      }

      list.add(CalendarDayItem(dateStr, dayShort, dayNum, monthShort, hindiDay, engDay))
      cal.add(Calendar.DAY_OF_MONTH, 1)
    }
    list
  }

  var selectedDateStr by remember { mutableStateOf("2026-09-29") }
  val selectedDayItem = daysList.find { it.dateStr == selectedDateStr } ?: daysList.firstOrNull() ?: CalendarDayItem(
    "2026-09-29", "TUE", 29, "Sep", "Mangalvaar", "Tuesday"
  )

  var showMonthView by remember { mutableStateOf(false) }
  var showAddLeaveModal by remember { mutableStateOf(false) }
  var customLeaveReason by remember { mutableStateOf("Personal / Family Day Off") }

  val isSelectedDayLeave = leaves.any { it.date == selectedDateStr }
  val currentDaySchedule = schedules[selectedDateStr] ?: PartnerDaySchedule(
    date = selectedDateStr,
    isDayOff = isSelectedDayLeave,
    slots = defaultUcSlots(selectedDateStr)
  )

  val totalAvailableHours = if (isSelectedDayLeave) 0 else currentDaySchedule.totalAvailableHours

  val jobsForSelectedDate = allJobs.filter { job ->
    job.date == selectedDateStr ||
      (selectedDayItem.dayNumber == 24 && (job.date.endsWith("-24") || job.timeSlot.contains("9:00 AM"))) ||
      (selectedDayItem.dayNumber == 25 && (job.date.endsWith("-25") || job.timeSlot.contains("7:30 PM")))
  }

  val dateRowListState = rememberLazyListState()

  LaunchedEffect(Unit) {
    val initialIdx = daysList.indexOfFirst { it.dateStr == "2026-09-29" }
    if (initialIdx >= 0) {
      dateRowListState.scrollToItem(maxOf(0, initialIdx - 1))
    }
  }

  var showHelpDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = if (showMonthView) "Full Month Calendar" else "Shifts & Availability",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        },
        navigationIcon = {
          IconButton(
            onClick = {
              if (showMonthView) {
                showMonthView = false
              } else {
                viewModel.navigateTo(AppScreen.HOME)
              }
            },
            modifier = Modifier.testTag("calendar_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }
        },
        actions = {
          IconButton(onClick = { showMonthView = !showMonthView }) {
            Icon(
              imageVector = if (showMonthView) Icons.Default.ViewDay else Icons.Default.CalendarMonth,
              contentDescription = "Toggle View",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          IconButton(onClick = { showAddLeaveModal = true }) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Apply Leave",
              tint = CleankrViolet
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface,
          titleContentColor = MaterialTheme.colorScheme.onSurface
        )
      )
    },
    floatingActionButton = {
      CleankrHelpFloatingButton(
        onHelpClick = { showHelpDialog = true }
      )
    }
  ) { paddingValues ->
    if (showMonthView) {
      FullMonthCalendarContent(
        paddingValues = paddingValues,
        viewModel = viewModel,
        allJobs = allJobs,
        leaves = leaves,
        onSelectDate = { dateStr ->
          selectedDateStr = dateStr
          showMonthView = false
        }
      )
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .background(Color(0xFFF8F9FA))
          .padding(paddingValues),
        contentPadding = PaddingValues(bottom = 80.dp)
      ) {
        item {
          Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 0.5.dp,
            modifier = Modifier.fillMaxWidth()
          ) {
            LazyRow(
              state = dateRowListState,
              contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              items(daysList) { item ->
                val isSelected = item.dateStr == selectedDateStr
                val itemSchedule = schedules[item.dateStr] ?: PartnerDaySchedule(
                  date = item.dateStr,
                  slots = defaultUcSlots(item.dateStr)
                )
                val isItemAvailable = !leaves.any { it.date == item.dateStr } && itemSchedule.totalAvailableHours > 0

                val itemBg = if (isSelected) Color(0xFFEAEFF5) else Color.Transparent
                val itemBorder = if (isSelected) BorderStroke(1.dp, Color(0xFFD0DCE7)) else null

                Card(
                  shape = RoundedCornerShape(12.dp),
                  colors = CardDefaults.cardColors(containerColor = itemBg),
                  border = itemBorder,
                  elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                  modifier = Modifier
                    .width(58.dp)
                    .height(76.dp)
                    .clickable { selectedDateStr = item.dateStr }
                    .testTag("date_pill_${item.dateStr}")
                ) {
                  Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                    Text(
                      text = item.dayOfWeekShort,
                      fontSize = 11.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                      color = if (isSelected) Color(0xFF1E293B) else Color(0xFF94A3B8)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                      text = "${item.dayNumber}",
                      fontSize = 17.sp,
                      fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
                      color = if (isSelected) Color(0xFF0F172A) else Color(0xFF334155)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    if (isItemAvailable) {
                      Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Available",
                        tint = Color(0xFF00796B),
                        modifier = Modifier.size(13.dp)
                      )
                    } else {
                      Spacer(modifier = Modifier.size(13.dp))
                    }
                  }
                }
              }
            }
          }
        }

        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 20.dp, vertical = 18.dp)
          ) {
            Text(
              text = "${selectedDayItem.dayNameHindi}, ${selectedDayItem.monthShort} ${getOrdinal(selectedDayItem.dayNumber)}",
              fontSize = 22.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = when {
                isSelectedDayLeave -> "Full day marked OFF (On Leave)"
                totalAvailableHours == 0 -> "0 hours marked available (Shift paused)"
                else -> "$totalAvailableHours hours marked available"
              },
              fontSize = 15.sp,
              fontWeight = FontWeight.SemiBold,
              color = if (isSelectedDayLeave) CleankrRed else Color(0xFF00796B)
            )
          }
        }

        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            currentDaySchedule.slots.forEach { slot ->
              SlotToggleRow(
                slot = slot,
                isDateDayOff = isSelectedDayLeave,
                onToggle = { slotId ->
                  viewModel.toggleSlotAvailability(selectedDateStr, slotId)
                }
              )
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(14.dp))
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Full Day Off Toggle",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = if (isSelectedDayLeave) "You are on leave for this day" else "Pause all dispatch offers for today",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Button(
                onClick = {
                  viewModel.toggleLeaveForDate(selectedDateStr, customLeaveReason)
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (isSelectedDayLeave) CleankrGreen else CleankrRed
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
              ) {
                Text(
                  text = if (isSelectedDayLeave) "Mark Available" else "Take Day Off",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }
          }
        }

      }
    }

    if (showAddLeaveModal) {
      var leaveDateInput by remember { mutableStateOf(selectedDateStr) }
      var reasonInput by remember { mutableStateOf("Personal emergency / Health rest") }

      AlertDialog(
        onDismissRequest = { showAddLeaveModal = false },
        title = {
          Text(text = "Apply for Leave / Day Off", fontWeight = FontWeight.Bold)
        },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
              text = "Applying for leave pauses all job dispatch assignments on that date.",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
              value = leaveDateInput,
              onValueChange = { leaveDateInput = it },
              label = { Text("Date (YYYY-MM-DD)") },
              modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
              value = reasonInput,
              onValueChange = { reasonInput = it },
              label = { Text("Reason for Leave") },
              modifier = Modifier.fillMaxWidth()
            )
          }
        },
        confirmButton = {
          Button(
            onClick = {
              viewModel.applyLeave(leaveDateInput, reasonInput)
              showAddLeaveModal = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = CleankrViolet)
          ) {
            Text("Confirm Leave")
          }
        },
        dismissButton = {
          TextButton(onClick = { showAddLeaveModal = false }) {
            Text("Cancel")
          }
        }
      )
    }

    if (showHelpDialog) {
      AlertDialog(
        onDismissRequest = { showHelpDialog = false },
        title = {
          Text(text = "Cleankr Partner Helpline", fontWeight = FontWeight.Bold)
        },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("24/7 Dedicated Partner Support:")
            Text("Toll-Free: 1800-CLEANKR-PARTNER", fontWeight = FontWeight.Bold, color = CleankrViolet)
            Text("Email: partner-support@cleankr.in")
            Text("For duty emergencies, partner support is active 24/7.")
          }
        },
        confirmButton = {
          TextButton(onClick = { showHelpDialog = false }) {
            Text("Close")
          }
        }
      )
    }
  }
}

@Composable
private fun FullMonthCalendarContent(
  paddingValues: PaddingValues,
  viewModel: PartnerViewModel,
  allJobs: List<Job>,
  leaves: List<com.example.data.model.CalendarLeaveEntry>,
  onSelectDate: (String) -> Unit
) {
  var currentMonthIndex by remember { mutableIntStateOf(8) }
  var currentYear by remember { mutableIntStateOf(2026) }

  val monthNames = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(paddingValues)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(4.dp))
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(
              onClick = {
                if (currentMonthIndex > 0) currentMonthIndex-- else {
                  currentMonthIndex = 11
                  currentYear--
                }
              }
            ) {
              Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month")
            }

            Text(
              text = "${monthNames[currentMonthIndex]} $currentYear",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(
              onClick = {
                if (currentMonthIndex < 11) currentMonthIndex++ else {
                  currentMonthIndex = 0
                  currentYear++
                }
              }
            ) {
              Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            daysOfWeek.forEach { dayName ->
              Text(
                text = dayName,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(36.dp),
                textAlign = TextAlign.Center
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
          Spacer(modifier = Modifier.height(8.dp))

          val firstDayOffset = 1
          val daysInMonth = 30
          val totalCells = daysInMonth + firstDayOffset
          val rows = (totalCells + 6) / 7

          for (rowIndex in 0 until rows) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              for (colIndex in 0 until 7) {
                val cellIndex = rowIndex * 7 + colIndex
                val dayNum = cellIndex - firstDayOffset + 1

                if (dayNum in 1..daysInMonth) {
                  val dateKey = String.format("%04d-%02d-%02d", currentYear, currentMonthIndex + 1, dayNum)
                  val hasJob = allJobs.any { it.date == dateKey } || dayNum == 24 || dayNum == 25
                  val isLeave = leaves.any { it.date == dateKey }

                  Box(
                    modifier = Modifier
                      .size(38.dp)
                      .clip(CircleShape)
                      .background(
                        when {
                          isLeave -> CleankrRed.copy(alpha = 0.15f)
                          hasJob -> CleankrCoral.copy(alpha = 0.15f)
                          else -> Color.Transparent
                        }
                      )
                      .clickable { onSelectDate(dateKey) },
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = "$dayNum",
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Bold,
                      color = when {
                        isLeave -> CleankrRed
                        hasJob -> CleankrCoral
                        else -> MaterialTheme.colorScheme.onSurface
                      }
                    )
                  }
                } else {
                  Spacer(modifier = Modifier.size(38.dp))
                }
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
          }
        }
      }
    }
  }
}
