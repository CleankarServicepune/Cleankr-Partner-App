package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.Job
import com.example.data.model.JobStatus
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.CleankrHelpFloatingButton
import com.example.ui.theme.CleankrGreen

@Composable
fun HistoryScreen(viewModel: PartnerViewModel) {
  val allJobs by viewModel.allJobs.collectAsState()

  // 4 Horizontal Sub-tabs matching Screenshot 3: Upcoming, Pending, Completed, Cancelled
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  val tabs = listOf("Upcoming", "Pending", "Completed", "Cancelled")

  val filteredJobs = when (selectedTabIndex) {
    0 -> allJobs.filter { it.status != JobStatus.CANCELLED } // Upcoming
    1 -> allJobs.filter { it.status == JobStatus.ASSIGNED || it.status == JobStatus.ACCEPTED } // Pending
    2 -> allJobs.filter { it.status == JobStatus.COMPLETED } // Completed
    else -> allJobs.filter { it.status == JobStatus.CANCELLED } // Cancelled
  }

  // Group by Today vs Tomorrow vs Other
  val todayJobs = filteredJobs.filter {
    it.date.endsWith("-24") || it.timeSlot.contains("9:00 AM")
  }
  val tomorrowJobs = filteredJobs.filter {
    it.date.endsWith("-25") || it.timeSlot.contains("7:30 PM")
  }
  val otherJobs = filteredJobs.filter { it !in todayJobs && it !in tomorrowJobs }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF9F9FB))
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Sub-tabs (Upcoming | Pending | Completed | Cancelled)
      TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = Color(0xFF1E1A22),
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
            color = Color(0xFF1E1A22),
            height = 2.5.dp
          )
        }
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTabIndex == index,
            onClick = { selectedTabIndex = index },
            text = {
              Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                color = if (selectedTabIndex == index) Color(0xFF1E1A22) else Color(0xFF757575)
              )
            }
          )
        }
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Today Section (Screenshot 3)
        if (todayJobs.isNotEmpty() || (selectedTabIndex == 0)) {
          item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "Today",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E1A22)
            )
          }

          if (todayJobs.isEmpty()) {
            item {
              Text(
                text = "No jobs scheduled for today.",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
              )
            }
          } else {
            items(todayJobs) { job ->
              OngoingJobCard(
                job = job,
                badgeLabel = if (job.status == JobStatus.COMPLETED) "Job ended" else "Job ended",
                badgeColor = Color(0xFFEEEEEE),
                badgeTextColor = Color(0xFF616161),
                onCall = { viewModel.startMaskedCall(job) },
                onNavigate = { viewModel.navigateToCustomer(job) },
                onClick = { viewModel.navigateTo(AppScreen.JOB_DETAILS, jobId = job.id) }
              )
            }
          }
        }

        // Tomorrow Section (Screenshot 3)
        if (tomorrowJobs.isNotEmpty() || (selectedTabIndex == 0)) {
          item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Tomorrow",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E1A22)
            )
          }

          if (tomorrowJobs.isEmpty()) {
            item {
              Text(
                text = "No jobs scheduled for tomorrow.",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
              )
            }
          } else {
            items(tomorrowJobs) { job ->
              OngoingJobCard(
                job = job,
                badgeLabel = "REPEAT",
                badgeColor = Color(0xFFE8F5E9),
                badgeTextColor = CleankrGreen,
                onCall = { viewModel.startMaskedCall(job) },
                onNavigate = { viewModel.navigateToCustomer(job) },
                onClick = { viewModel.navigateTo(AppScreen.JOB_DETAILS, jobId = job.id) }
              )
            }
          }
        }

        // Other scheduled jobs
        if (otherJobs.isNotEmpty()) {
          item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Other Dates",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E1A22)
            )
          }

          items(otherJobs) { job ->
            OngoingJobCard(
              job = job,
              badgeLabel = job.status.name.replace("_", " "),
              badgeColor = Color(0xFFEDE7F6),
              badgeTextColor = Color(0xFF5A31F4),
              onCall = { viewModel.startMaskedCall(job) },
              onNavigate = { viewModel.navigateToCustomer(job) },
              onClick = { viewModel.navigateTo(AppScreen.JOB_DETAILS, jobId = job.id) }
            )
          }
        }

        item {
          Spacer(modifier = Modifier.height(80.dp))
        }
      }
    }

    // Floating Help Button
    CleankrHelpFloatingButton(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 16.dp, bottom = 16.dp),
      onHelpClick = { viewModel.navigateTo(AppScreen.HELP_SUPPORT) }
    )
  }
}

@Composable
private fun OngoingJobCard(
  job: Job,
  badgeLabel: String,
  badgeColor: Color,
  badgeTextColor: Color,
  onCall: () -> Unit,
  onNavigate: () -> Unit,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("ongoing_job_card_${job.id}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        // Chip
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = badgeColor
        ) {
          Text(
            text = badgeLabel,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = badgeTextColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Time in bold (9:00 AM, 7:30 PM)
        Text(
          text = job.timeSlot,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1E1A22)
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Customer name
        Text(
          text = job.customerName,
          fontSize = 14.sp,
          color = Color(0xFF616161)
        )

        Text(
          text = job.serviceTitle,
          fontSize = 12.sp,
          color = Color(0xFF9E9E9E)
        )
      }

      // Action buttons on right (Call & Navigate)
      Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Call Button
        IconButton(
          onClick = onCall,
          modifier = Modifier
            .size(42.dp)
            .border(1.dp, Color(0xFFE0E0E0), CircleShape)
            .background(Color.White, CircleShape)
            .testTag("call_button_${job.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "Call Customer",
            tint = Color(0xFF1E1A22),
            modifier = Modifier.size(18.dp)
          )
        }

        // Navigate Button (Diamond with arrow)
        IconButton(
          onClick = onNavigate,
          modifier = Modifier
            .size(42.dp)
            .border(1.dp, Color(0xFFE0E0E0), CircleShape)
            .background(Color.White, CircleShape)
            .testTag("navigate_button_${job.id}")
        ) {
          Icon(
            imageVector = Icons.Default.NearMe,
            contentDescription = "Navigate to location",
            tint = Color(0xFF1E1A22),
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
