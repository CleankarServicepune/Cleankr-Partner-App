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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Job
import com.example.data.model.JobStatus
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CleankrAmber
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrCyan
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrPeach
import com.example.ui.theme.CleankrRed

@Composable
fun DashboardScreen(viewModel: PartnerViewModel) {
  val isOnline by viewModel.isOnline.collectAsState()
  val profile by viewModel.profile.collectAsState()
  val earnings by viewModel.earnings.collectAsState()
  val allJobs by viewModel.allJobs.collectAsState()
  val securityState by viewModel.securityState.collectAsState()

  // New incoming job dispatch alert (Jobs with status ASSIGNED)
  val newJobRequest = allJobs.find { it.status == JobStatus.ASSIGNED }

  // Active / Accepted / In-progress job
  val activeJob = allJobs.find {
    it.status in listOf(JobStatus.ACCEPTED, JobStatus.ON_THE_WAY, JobStatus.ARRIVED, JobStatus.STARTED)
  }

  // Completed jobs count today
  val completedToday = allJobs.count { it.status == JobStatus.COMPLETED }

  // Rejection dialog state
  var showRejectDialog by remember { mutableStateOf<String?>(null) }
  var rejectionReason by remember { mutableStateOf("Vehicle breakdown / Far distance") }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(8.dp))
      // Greeting and Verification Banner
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Welcome back,",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = profile.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = CleankrGreen.copy(alpha = 0.15f),
          modifier = Modifier.border(1.dp, CleankrGreen.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CleankrGreen, modifier = Modifier.size(16.dp))
            Text(
              text = "KYC Verified",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = CleankrGreen
            )
          }
        }
      }
    }

    // 1. URGENT NEW JOB DISPATCH ALERT BANNER (If any job is ASSIGNED)
    if (newJobRequest != null) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = CleankrCoral.copy(alpha = 0.12f)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, CleankrCoral, RoundedCornerShape(16.dp))
            .testTag("urgent_job_alert_card")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  Icons.Default.NotificationsActive,
                  contentDescription = null,
                  tint = CleankrCoral,
                  modifier = Modifier.size(20.dp)
                )
                Text(
                  text = "NEW BOOKING REQUEST",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = CleankrCoral,
                  letterSpacing = 0.5.sp
                )
              }
              Surface(
                shape = RoundedCornerShape(20.dp),
                color = CleankrCoral
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(Icons.Default.Timer, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("45s left", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = newJobRequest.serviceTitle,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = newJobRequest.packageType,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text("Scheduled Slot", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(newJobRequest.timeSlot, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
              }
              Column(horizontalAlignment = Alignment.End) {
                Text("Partner Payout", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                  "₹${newJobRequest.estimatedEarnings.toInt()}",
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Black,
                  color = CleankrGreen
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "📍 " + newJobRequest.address,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedButton(
                onClick = { showRejectDialog = newJobRequest.id },
                modifier = Modifier
                  .weight(1f)
                  .testTag("reject_job_button"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CleankrRed),
                shape = RoundedCornerShape(10.dp)
              ) {
                Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Decline", fontWeight = FontWeight.SemiBold)
              }

              Button(
                onClick = {
                  viewModel.acceptJob(newJobRequest.id)
                  viewModel.navigateTo(AppScreen.JOB_DETAILS, newJobRequest.id)
                },
                modifier = Modifier
                  .weight(1.4f)
                  .testTag("accept_job_button"),
                colors = ButtonDefaults.buttonColors(containerColor = CleankrCoral),
                shape = RoundedCornerShape(10.dp)
              ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Accept Booking", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }

    // 2. QUICK STATS ROW
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Today's Earnings
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.weight(1f).clickable { viewModel.navigateTo(AppScreen.EARNINGS) }
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text("Today's Earnings", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "₹${earnings.todayEarnings.toInt()}",
              fontSize = 18.sp,
              fontWeight = FontWeight.Black,
              color = CleankrGreen
            )
            Text("Withdrawable: ₹${earnings.withdrawableBalance.toInt()}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }

        // Acceptance & Rating
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.weight(1f)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text("Acceptance & Rating", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Star, contentDescription = null, tint = CleankrAmber, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(3.dp))
              Text("${profile.rating}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.width(6.dp))
              Text("(${profile.acceptanceRate}% acc.)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text("$completedToday Completed Today", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }

    // 3. ACTIVE JOB IN-PROGRESS CARD (If any job accepted or on the way)
    if (activeJob != null) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.navigateTo(AppScreen.JOB_DETAILS, activeJob.id) }
            .testTag("active_job_card")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "ACTIVE JOB IN PROGRESS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp,
                color = CleankrMagenta
              )
              StatusBadge(status = activeJob.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = activeJob.serviceTitle,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Customer: ${activeJob.customerName}",
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "📍 " + activeJob.address,
              fontSize = 12.sp,
              maxLines = 2,
              modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Masked Call Relay Button
              OutlinedButton(
                onClick = { viewModel.startMaskedCall(activeJob) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f).testTag("dashboard_masked_call_button")
              ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp), tint = CleankrMagenta)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Masked Call", fontSize = 12.sp)
              }

              // Turn-by-Turn Navigation
              Button(
                onClick = { viewModel.navigateToCustomer(activeJob) },
                colors = ButtonDefaults.buttonColors(containerColor = CleankrCyan),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f).testTag("dashboard_navigate_button")
              ) {
                Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Navigate", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }

    // 4. QUICK ACTIONS GRID
    item {
      Text(
        text = "Quick Partner Actions",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        QuickActionItem(
          icon = Icons.Default.CalendarMonth,
          title = "Leave & Calendar",
          color = CleankrMagenta,
          onClick = { viewModel.navigateTo(AppScreen.CALENDAR) },
          modifier = Modifier.weight(1f)
        )
        QuickActionItem(
          icon = Icons.Default.Payments,
          title = "Withdraw Payout",
          color = CleankrGreen,
          onClick = { viewModel.navigateTo(AppScreen.EARNINGS) },
          modifier = Modifier.weight(1f)
        )
        QuickActionItem(
          icon = Icons.Default.Security,
          title = "Cyber Security",
          color = CleankrCyan,
          onClick = { viewModel.navigateTo(AppScreen.SECURITY_CENTER) },
          modifier = Modifier.weight(1f)
        )
        QuickActionItem(
          icon = Icons.Default.HelpOutline,
          title = "24/7 Helpline",
          color = CleankrCoral,
          onClick = { viewModel.navigateTo(AppScreen.HELP_SUPPORT) },
          modifier = Modifier.weight(1f)
        )
      }
    }

    // 5. TODAY'S SCHEDULE / JOBS LIST
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "All Assigned & Scheduled Jobs",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
        TextButton(onClick = { viewModel.navigateTo(AppScreen.HISTORY) }) {
          Text("History & Receipts", fontSize = 12.sp, color = CleankrCoral)
        }
      }
    }

    items(allJobs.take(4)) { job ->
      JobSummaryCard(
        job = job,
        onClick = { viewModel.navigateTo(AppScreen.JOB_DETAILS, job.id) }
      )
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  // Rejection Dialog
  if (showRejectDialog != null) {
    AlertDialog(
      onDismissRequest = { showRejectDialog = null },
      title = { Text("Decline Job Request") },
      text = {
        Column {
          Text("Please choose a reason for declining. Excessive declines may impact your priority tier.")
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = rejectionReason,
            onValueChange = { rejectionReason = it },
            label = { Text("Reason for Rejection") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showRejectDialog?.let { viewModel.rejectJob(it, rejectionReason) }
            showRejectDialog = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = CleankrRed)
        ) {
          Text("Confirm Decline")
        }
      },
      dismissButton = {
        TextButton(onClick = { showRejectDialog = null }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
fun QuickActionItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  color: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = modifier.clickable { onClick() }
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = title,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        maxLines = 2,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}

@Composable
fun JobSummaryCard(
  job: Job,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("job_card_${job.id}")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "#" + job.id,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        StatusBadge(status = job.status)
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = job.serviceTitle,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = "Time: ${job.timeSlot} • Payout: ₹${job.estimatedEarnings.toInt()}",
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Text(
        text = "📍 " + job.address,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        modifier = Modifier.padding(top = 2.dp)
      )
    }
  }
}
