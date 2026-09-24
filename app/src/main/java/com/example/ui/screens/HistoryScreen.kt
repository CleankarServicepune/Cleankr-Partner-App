package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobStatus
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrGreen

enum class HistoryFilter {
  ALL,
  COMPLETED,
  ACTIVE,
  CANCELLED
}

@Composable
fun HistoryScreen(viewModel: PartnerViewModel) {
  val allJobs by viewModel.allJobs.collectAsState()
  var currentFilter by remember { mutableStateOf(HistoryFilter.ALL) }

  val filteredJobs = when (currentFilter) {
    HistoryFilter.ALL -> allJobs
    HistoryFilter.COMPLETED -> allJobs.filter { it.status == JobStatus.COMPLETED }
    HistoryFilter.ACTIVE -> allJobs.filter { it.status !in listOf(JobStatus.COMPLETED, JobStatus.CANCELLED) }
    HistoryFilter.CANCELLED -> allJobs.filter { it.status == JobStatus.CANCELLED }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Booking History & Audit Receipts",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "Complete archive of all dispatched, completed and settled appointments.",
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // Filter Chips
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        HistoryFilter.values().forEach { filter ->
          FilterChip(
            selected = currentFilter == filter,
            onClick = { currentFilter = filter },
            label = { Text(filter.name.lowercase().replaceFirstChar { it.uppercase() }) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CleankrCoral.copy(alpha = 0.2f),
              selectedLabelColor = CleankrCoral
            ),
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    if (filteredJobs.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "No jobs found under '${currentFilter.name}'.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(20.dp)
          )
        }
      }
    } else {
      items(filteredJobs) { job ->
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.navigateTo(AppScreen.JOB_DETAILS, job.id) }
            .testTag("history_job_item_${job.id}")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Booking #${job.id} • ${job.date}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              StatusBadge(status = job.status)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = job.serviceTitle,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = job.packageType,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Customer: ${job.customerName}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "₹${job.estimatedEarnings.toInt()}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = CleankrGreen
              )
            }

            if (job.beforePhotos.isNotEmpty() || job.afterPhotos.isNotEmpty()) {
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.padding(top = 6.dp)
              ) {
                Text(
                  text = "📸 Photos attached: ${job.beforePhotos.size} Before, ${job.afterPhotos.size} After",
                  fontSize = 10.sp,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
