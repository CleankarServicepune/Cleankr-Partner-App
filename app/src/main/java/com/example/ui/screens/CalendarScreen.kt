package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.CleankrHelpFloatingButton
import com.example.ui.theme.CleankrAmber
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrRed
import com.example.ui.theme.CleankrViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: PartnerViewModel) {
  val allJobs by viewModel.allJobs.collectAsState()
  val leaves by viewModel.leaves.collectAsState()

  // Selected date state (defaults to Sep 24, 2026 matching screenshots)
  var selectedDay by remember { mutableIntStateOf(24) }
  var currentMonthIndex by remember { mutableIntStateOf(8) } // 8 = September (0-indexed)
  var currentYear by remember { mutableIntStateOf(2026) }

  val monthNames = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
  )

  var showAddLeaveModal by remember { mutableStateOf(false) }
  var customLeaveReason by remember { mutableStateOf("Personal / Family Day Off") }

  val selectedDateStr = String.format("%04d-%02d-%02d", currentYear, currentMonthIndex + 1, selectedDay)

  val isSelectedDayLeave = leaves.any { it.date == selectedDateStr }
  val activeLeave = leaves.find { it.date == selectedDateStr }

  // Scheduled jobs for the selected date
  val jobsForSelectedDate = allJobs.filter { job ->
    job.date == selectedDateStr ||
      (selectedDay == 24 && (job.date.endsWith("-24") || job.timeSlot.contains("9:00 AM"))) ||
      (selectedDay == 25 && (job.date.endsWith("-25") || job.timeSlot.contains("7:30 PM")))
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Calendar & Availability",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        },
        navigationIcon = {
          IconButton(
            onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
            modifier = Modifier.testTag("calendar_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        actions = {
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
        onHelpClick = { viewModel.navigateTo(AppScreen.HELP_SUPPORT) }
      )
    }
  ) { paddingValues ->
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

        // Month Navigation Card
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // Month Header with Prev / Next Buttons
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

            // Days of Week Header
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

            // Days Grid (Days 1 to 30 for Sep 2026; starting on Tuesday = offset 1)
            val firstDayOffset = 1 // Sep 1, 2026 is Tuesday
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
                    val isLeave = leaves.any { it.date == dateKey }
                    val isSelected = dayNum == selectedDay
                    val hasJobs = dayNum in listOf(24, 25, 27)

                    Box(
                      modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                          when {
                            isSelected -> CleankrViolet
                            isLeave -> CleankrRed.copy(alpha = 0.12f)
                            else -> Color.Transparent
                          }
                        )
                        .clickable { selectedDay = dayNum }
                        .testTag("calendar_day_$dayNum"),
                      contentAlignment = Alignment.Center
                    ) {
                      Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                      ) {
                        Text(
                          text = "$dayNum",
                          fontSize = 13.sp,
                          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                          color = when {
                            isSelected -> Color.White
                            isLeave -> CleankrRed
                            else -> MaterialTheme.colorScheme.onSurface
                          }
                        )
                        // Status Indicator Dot
                        if (!isSelected) {
                          Box(
                            modifier = Modifier
                              .size(4.dp)
                              .clip(CircleShape)
                              .background(
                                when {
                                  isLeave -> CleankrRed
                                  hasJobs -> CleankrCoral
                                  else -> CleankrGreen
                                }
                              )
                          )
                        }
                      }
                    }
                  } else {
                    Spacer(modifier = Modifier.size(38.dp))
                  }
                }
              }
              Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Calendar Legend
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly
            ) {
              LegendDot(CleankrGreen, "Available")
              LegendDot(CleankrCoral, "Booked Job")
              LegendDot(CleankrRed, "On Leave")
            }
          }
        }
      }

      // Selected Day Availability & Details Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Thursday, $selectedDay ${monthNames[currentMonthIndex]}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(8.dp)
                      .clip(CircleShape)
                      .background(if (isSelectedDayLeave) CleankrRed else CleankrGreen)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = if (isSelectedDayLeave) "ON LEAVE / DAY OFF" else "AVAILABLE • Working Day",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelectedDayLeave) CleankrRed else CleankrGreen
                  )
                }
              }

              // Quick Toggle Button
              Button(
                onClick = {
                  viewModel.toggleLeaveForDate(selectedDateStr, customLeaveReason)
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (isSelectedDayLeave) CleankrGreen else CleankrRed
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("toggle_leave_button")
              ) {
                Text(
                  text = if (isSelectedDayLeave) "Mark Available" else "Take Day Off",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            Spacer(modifier = Modifier.height(12.dp))

            // Duty Slots
            Text(
              text = "Working Shift Slots",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              ShiftSlotChip("Morning: 8:00 AM - 1:00 PM", isAvailable = !isSelectedDayLeave)
              ShiftSlotChip("Evening: 2:00 PM - 8:00 PM", isAvailable = !isSelectedDayLeave)
            }
          }
        }
      }

      // Scheduled Jobs on this Date
      item {
        Text(
          text = "Jobs on $selectedDay ${monthNames[currentMonthIndex]} (${jobsForSelectedDate.size})",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      if (jobsForSelectedDate.isEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(16.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Icon(
                Icons.Default.EventBusy,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
              )
              Text(
                text = if (isSelectedDayLeave) "You are on leave. No jobs dispatched." else "No bookings scheduled yet for this date.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        items(jobsForSelectedDate) { job ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                viewModel.navigateTo(AppScreen.JOB_DETAILS, jobId = job.id)
              }
              .testTag("calendar_job_item_${job.id}")
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
              ) {
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = CleankrViolet.copy(alpha = 0.12f)
                ) {
                  Icon(
                    Icons.Default.Work,
                    contentDescription = null,
                    tint = CleankrViolet,
                    modifier = Modifier
                      .padding(8.dp)
                      .size(20.dp)
                  )
                }
                Column {
                  Text(
                    text = job.serviceTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "${job.timeSlot} • Customer: ${job.customerName}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
              Text(
                text = "₹${job.estimatedEarnings.toInt()}",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CleankrGreen
              )
            }
          }
        }
      }

      // Leave Policy & Balance Summary Card
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F5FC)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Icon(
              Icons.Default.CalendarToday,
              contentDescription = null,
              tint = CleankrViolet,
              modifier = Modifier.size(24.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Monthly Leave Balance",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF1E1A22)
              )
              Text(
                text = "${leaves.size} of 4 leaves used this month. 0 strike penalty if applied 24h in advance.",
                fontSize = 11.sp,
                color = Color(0xFF666666)
              )
            }
            TextButton(onClick = { showAddLeaveModal = true }) {
              Text("Apply", color = CleankrViolet, fontWeight = FontWeight.Bold)
            }
          }
        }
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }

  // Add Leave Dialog
  if (showAddLeaveModal) {
    var leaveDateInput by remember { mutableStateOf(selectedDateStr) }
    var reasonInput by remember { mutableStateOf("Family Function / Personal") }

    AlertDialog(
      onDismissRequest = { showAddLeaveModal = false },
      title = { Text("Apply for Leave / Day Off") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "Mark yourself unavailable for new dispatch orders without affecting your profile rating.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          OutlinedTextField(
            value = leaveDateInput,
            onValueChange = { leaveDateInput = it },
            label = { Text("Leave Date (YYYY-MM-DD)") },
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
}

@Composable
private fun LegendDot(color: Color, label: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(8.dp)
        .clip(CircleShape)
        .background(color)
    )
    Text(
      text = label,
      fontSize = 11.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Composable
private fun ShiftSlotChip(label: String, isAvailable: Boolean) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = if (isAvailable) CleankrGreen.copy(alpha = 0.1f) else CleankrRed.copy(alpha = 0.1f),
    modifier = Modifier.border(
      width = 1.dp,
      color = if (isAvailable) CleankrGreen.copy(alpha = 0.3f) else CleankrRed.copy(alpha = 0.3f),
      shape = RoundedCornerShape(8.dp)
    )
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Icon(
        imageVector = if (isAvailable) Icons.Default.Check else Icons.Default.Close,
        contentDescription = null,
        tint = if (isAvailable) CleankrGreen else CleankrRed,
        modifier = Modifier.size(12.dp)
      )
      Text(
        text = label,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = if (isAvailable) CleankrGreen else CleankrRed
      )
    }
  }
}
