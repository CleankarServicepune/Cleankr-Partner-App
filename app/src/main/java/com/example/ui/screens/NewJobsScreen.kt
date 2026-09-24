package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobStatus
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.CleankrHelpFloatingButton
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrViolet

@Composable
fun NewJobsScreen(viewModel: PartnerViewModel) {
  val allJobs by viewModel.allJobs.collectAsState()
  val newJobs = allJobs.filter { it.status == JobStatus.ASSIGNED }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF9F9FB))
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "New Jobs",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1E1A22)
        )
        Text(
          text = "Instant booking dispatch requests in your area",
          fontSize = 13.sp,
          color = Color(0xFF757575)
        )
      }

      if (newJobs.isEmpty()) {
        item {
          Spacer(modifier = Modifier.height(40.dp))
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Surface(
                shape = CircleShape,
                color = Color(0xFFF3EDF7),
                modifier = Modifier.size(64.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = Icons.Default.Article,
                    contentDescription = null,
                    tint = CleankrViolet,
                    modifier = Modifier.size(32.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.height(16.dp))
              Text(
                text = "No new jobs right now",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1A22)
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Keep your duty status ONLINE and stay checked in. High demand orders in your locality will appear here automatically.",
                fontSize = 12.sp,
                color = Color(0xFF757575),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 16.sp
              )
            }
          }
        }
      } else {
        items(newJobs) { job ->
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { viewModel.navigateTo(AppScreen.JOB_DETAILS, jobId = job.id) }
              .testTag("new_job_card_${job.id}")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = CleankrCoral.copy(alpha = 0.12f)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      Icons.Default.Timer,
                      contentDescription = null,
                      tint = CleankrCoral,
                      modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "45s to accept",
                      color = CleankrCoral,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }

                Text(
                  text = "₹${job.estimatedEarnings.toInt()}",
                  fontSize = 18.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = CleankrGreen
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = job.serviceTitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1A22)
              )

              Spacer(modifier = Modifier.height(6.dp))

              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  Icons.Default.Schedule,
                  contentDescription = null,
                  tint = Color(0xFF757575),
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = "${job.date} • ${job.timeSlot}",
                  fontSize = 12.sp,
                  color = Color(0xFF757575)
                )
              }

              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  Icons.Default.LocationOn,
                  contentDescription = null,
                  tint = Color(0xFF757575),
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = job.address,
                  fontSize = 12.sp,
                  color = Color(0xFF757575)
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Button(
                  onClick = { viewModel.acceptJob(job.id) },
                  colors = ButtonDefaults.buttonColors(containerColor = CleankrGreen),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.weight(1f)
                ) {
                  Text("Accept Order", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                  onClick = { viewModel.rejectJob(job.id, "Partner declined") },
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text("Pass", color = Color(0xFF757575))
                }
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }

    CleankrHelpFloatingButton(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 16.dp, bottom = 16.dp),
      onHelpClick = { viewModel.navigateTo(AppScreen.HELP_SUPPORT) }
    )
  }
}
