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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CleankrAmber
import com.example.ui.theme.CleankrAmberContainer
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrPeach
import com.example.ui.theme.CleankrRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class CalendarViewMode {
  DAY,
  WEEK,
  MONTH
}

@Composable
fun CalendarScreen(viewModel: PartnerViewModel) {
  val allJobs by viewModel.allJobs.collectAsState()
  val leaves by viewModel.leaves.collectAsState()

  var viewMode by remember { mutableStateOf(CalendarViewMode.DAY) }
  var showAddLeaveDialog by remember { mutableStateOf(false) }
  var leaveDateInput by remember {
    mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(System.currentTimeMillis() + 86400000L)))
  }
  var leaveReasonInput by remember { mutableStateOf("Personal / Family Function") }

  val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

  // Conflict Detection: check if any scheduled job coincides with an active leave date!
  val conflictingJobs = allJobs.filter { job ->
    leaves.any { it.date == job.date }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Partner Schedule & Calendar",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "Manage your working slots, appointments, and leave requests.",
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // View Mode Switcher (Day / Week / Month)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CalendarViewMode.values().forEach { mode ->
          FilterChip(
            selected = viewMode == mode,
            onClick = { viewMode = mode },
            label = {
              Text(
                text = mode.name.lowercase().replaceFirstChar { it.uppercase() } + " View",
                fontWeight = if (viewMode == mode) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CleankrMagenta.copy(alpha = 0.2f),
              selectedLabelColor = CleankrMagenta
            ),
            modifier = Modifier.weight(1f).testTag("calendar_tab_${mode.name.lowercase()}")
          )
        }
      }
    }

    // Conflict Alert Banner (If conflict detected)
    if (conflictingJobs.isNotEmpty()) {
      item {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = CleankrAmberContainer,
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CleankrAmber, RoundedCornerShape(12.dp))
            .testTag("calendar_conflict_banner")
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = CleankrAmber)
            Column {
              Text(
                text = "Schedule Conflict Detected!",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF7B5800)
              )
              Text(
                text = "${conflictingJobs.size} booking(s) scheduled on dates marked as unavailable/leave. Please reschedule booking or adjust leave.",
                fontSize = 11.sp,
                color = Color(0xFF7B5800)
              )
            }
          }
        }
      }
    }

    // Apply Leave / Unavailable Date Button
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Marked Leaves & Off-Days", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Button(
          onClick = { showAddLeaveDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = CleankrCoral),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("apply_leave_button")
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Apply Leave", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    // Leaves List
    if (leaves.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "No upcoming leaves marked. You are available for dispatch on all upcoming dates.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(14.dp)
          )
        }
      }
    } else {
      items(leaves) { leave ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(CleankrCoral.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.EventBusy, contentDescription = null, tint = CleankrCoral, modifier = Modifier.size(20.dp))
              }
              Column {
                Text(leave.date, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(leave.reason, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            IconButton(
              onClick = { viewModel.deleteLeave(leave.id) },
              modifier = Modifier.testTag("delete_leave_${leave.id}")
            ) {
              Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CleankrRed)
            }
          }
        }
      }
    }

    // Scheduled Bookings Section
    item {
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "Bookings for Selected Period (${allJobs.size})",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
      )
    }

    items(allJobs) { job ->
      val hasConflict = leaves.any { it.date == job.date }
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { viewModel.navigateTo(AppScreen.JOB_DETAILS, job.id) }
          .border(
            if (hasConflict) 1.dp else 0.dp,
            if (hasConflict) CleankrAmber else Color.Transparent,
            RoundedCornerShape(14.dp)
          )
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Text(job.date, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CleankrCoral)
              Text("• ${job.timeSlot}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            StatusBadge(status = job.status)
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(job.serviceTitle, fontWeight = FontWeight.Bold, fontSize = 15.sp)
          Text("Customer: ${job.customerName} • Payout: ₹${job.estimatedEarnings.toInt()}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("📍 " + job.address, fontSize = 11.sp, color = Color.Gray, maxLines = 1)

          if (hasConflict) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = CleankrAmberContainer,
              modifier = Modifier.padding(top = 8.dp)
            ) {
              Text(
                text = "⚠️ Overlaps with requested Leave Date ($job.date)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8D5B00),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  // Add Leave Dialog
  if (showAddLeaveDialog) {
    AlertDialog(
      onDismissRequest = { showAddLeaveDialog = false },
      title = { Text("Request Leave / Unavailable Date") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Dispatch will automatically stop routing new bookings on this date.", fontSize = 12.sp)
          OutlinedTextField(
            value = leaveDateInput,
            onValueChange = { leaveDateInput = it },
            label = { Text("Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = leaveReasonInput,
            onValueChange = { leaveReasonInput = it },
            label = { Text("Reason for Unavailable Status") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.applyLeave(leaveDateInput, leaveReasonInput)
            showAddLeaveDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = CleankrCoral)
        ) {
          Text("Confirm Leave")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddLeaveDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
