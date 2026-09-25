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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobStatus
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.CleankrHelpFloatingButton
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrRed
import com.example.ui.theme.CleankrViolet
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

private var hasRequestedNotificationPermissionOnce = false

@Composable
fun DashboardScreen(viewModel: PartnerViewModel) {
  val context = LocalContext.current
  val isCheckedIn by viewModel.isCheckedIn.collectAsState()
  val allJobs by viewModel.allJobs.collectAsState()

  // Graceful Android 13+ Notification Permission Request (Prompted once politely, no harassment)
  val notificationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    if (!isGranted) {
      // Graceful fallback - push notifications disabled, sound/in-app banners remain active
    }
  }

  LaunchedEffect(Unit) {
    if (!hasRequestedNotificationPermissionOnce && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      val isGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
      ) == PackageManager.PERMISSION_GRANTED
      if (!isGranted) {
        hasRequestedNotificationPermissionOnce = true
        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
      }
    }
  }

  // New incoming job dispatch alert (Jobs with status ASSIGNED)
  val newJobRequest = allJobs.find { it.status == JobStatus.ASSIGNED }
  val todayJobs = allJobs.filter {
    it.date.endsWith("-24") || it.timeSlot.contains("9:00 AM")
  }
  val newJobsCount = allJobs.count { it.status == JobStatus.ASSIGNED }

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
        Spacer(modifier = Modifier.height(6.dp))
      }

      // 1. Check-in to start your day Card (Screenshot 5)
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F3FB)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("check_in_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = if (isCheckedIn) "You are checked in for today" else "Check-in to start your day",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1A22)
              )
              Spacer(modifier = Modifier.height(12.dp))
              Button(
                onClick = { viewModel.toggleCheckIn() },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (isCheckedIn) CleankrGreen else Color(0xFF5A31F4)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("check_in_button")
              ) {
                Text(
                  text = if (isCheckedIn) "Checked in ✓" else "Check in",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }

            // Briefcase illustration / icon with checkmark
            Box(
              modifier = Modifier.size(72.dp),
              contentAlignment = Alignment.Center
            ) {
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFEDE7F6),
                modifier = Modifier.size(60.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = Icons.Default.BusinessCenter,
                    contentDescription = null,
                    tint = Color(0xFF5A31F4),
                    modifier = Modifier.size(32.dp)
                  )
                }
              }
              // Green check circle on corner
              Box(
                modifier = Modifier
                  .size(22.dp)
                  .align(Alignment.BottomEnd)
                  .clip(CircleShape)
                  .background(CleankrGreen),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }

      // 2. CALENDAR & AVAILABILITY ROW (Screenshot 5: Thu, Sep 24 • AVAILABLE | Fri, Sep 25 • AVAILABLE | 📅)
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.navigateTo(AppScreen.CALENDAR) }
            .testTag("home_calendar_availability_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              // Thursday Sep 24 Availability
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = "Thu, Sep 24",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color(0xFF1E1A22)
                )
                Text(text = "•", color = Color.Gray, fontSize = 12.sp)
                Box(
                  modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(CleankrGreen)
                )
                Text(
                  text = "AVAILABLE",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = CleankrGreen
                )
              }

              // Friday Sep 25 Availability
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = "Fri, Sep 25",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color(0xFF1E1A22)
                )
                Text(text = "•", color = Color.Gray, fontSize = 12.sp)
                Box(
                  modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(CleankrGreen)
                )
                Text(
                  text = "AVAILABLE",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = CleankrGreen
                )
              }
            }

            // Calendar Icon Button (Prominent Calendar Entrance!)
            IconButton(
              onClick = { viewModel.navigateTo(AppScreen.CALENDAR) },
              modifier = Modifier
                .size(42.dp)
                .background(Color(0xFFF3EDF7), CircleShape)
                .testTag("home_calendar_icon_button")
            ) {
              Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Open Calendar",
                tint = Color(0xFF5A31F4),
                modifier = Modifier.size(22.dp)
              )
            }
          }
        }
      }

      // 3. Urgent New Job Alert (if any assigned order)
      if (newJobRequest != null) {
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0ED)),
            modifier = Modifier
              .fillMaxWidth()
              .border(1.dp, Color(0xFFFF6E40), RoundedCornerShape(14.dp))
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
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
                    tint = Color(0xFFFF6E40),
                    modifier = Modifier.size(18.dp)
                  )
                  Text(
                    text = "NEW DISPATCH REQUEST",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFF6E40)
                  )
                }
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = Color(0xFFFF6E40)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("45s left", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = newJobRequest.serviceTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              Text(
                text = "${newJobRequest.address} • Payout: ₹${newJobRequest.estimatedEarnings.toInt()}",
                fontSize = 12.sp,
                color = Color(0xFF555555)
              )
              Spacer(modifier = Modifier.height(10.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                  onClick = { viewModel.acceptJob(newJobRequest.id) },
                  colors = ButtonDefaults.buttonColors(containerColor = CleankrGreen),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.weight(1f)
                ) {
                  Text("Accept Order", fontWeight = FontWeight.Bold)
                }
                Button(
                  onClick = { viewModel.rejectJob(newJobRequest.id, "Busy") },
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEEEEE)),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text("Pass", color = Color(0xFF555555))
                }
              }
            }
          }
        }
      }

      // 4. Job Navigation items (Screenshot 5: "No new jobs >", "No jobs today >")
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column {
            // No new jobs >
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(AppScreen.NEW_JOBS) }
                .padding(horizontal = 16.dp, vertical = 14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = if (newJobsCount > 0) "$newJobsCount new jobs available" else "No new jobs",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E1A22)
              )
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(13.dp)
              )
            }

            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

            // No jobs today >
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(AppScreen.HISTORY) }
                .padding(horizontal = 16.dp, vertical = 14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = if (todayJobs.isNotEmpty()) "${todayJobs.size} jobs scheduled today" else "No jobs today",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E1A22)
              )
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(13.dp)
              )
            }
          }
        }
      }

      // 5. Training Upgrade Promo Banner (Screenshot 5)
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.navigateTo(AppScreen.HELP_SUPPORT) }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(14.dp),
              modifier = Modifier.weight(1f)
            ) {
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFFF3E0),
                modifier = Modifier.size(44.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = Color(0xFFF57C00),
                    modifier = Modifier.size(24.dp)
                  )
                }
              }
              Column {
                Text(
                  text = "Upgrade to Bathroom Cleaning",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1E1A22)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "Complete your training >",
                  fontSize = 12.sp,
                  color = Color(0xFF5A31F4),
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
              contentDescription = null,
              tint = Color(0xFF9E9E9E),
              modifier = Modifier.size(13.dp)
            )
          }
        }
      }

      // 6. Master Program Banner (Screenshot 5: Recruit & earn extra ₹30,000 every month as a Master)
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Master Tag
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = Color(0xFFFFB300),
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = "MASTER",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E1A22),
                letterSpacing = 0.8.sp
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Recruit & earn extra ₹30,000 every month as a Master",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E1A22),
              lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Avatars & Social proof text
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Surface(
                  shape = CircleShape,
                  color = Color(0xFFE8F5E9),
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(
                    Icons.Default.Group,
                    contentDescription = null,
                    tint = CleankrGreen,
                    modifier = Modifier.padding(5.dp)
                  )
                }
                Text(
                  text = "Masters have earned\n₹10,00,000+",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFF555555),
                  lineHeight = 14.sp
                )
              }

              // Track progress CTA
              Button(
                onClick = { viewModel.navigateTo(AppScreen.HELP_SUPPORT) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A31F4)),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text(
                  text = "Track progress",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(70.dp))
      }
    }

    // Floating Help Button matching Screenshot 5
    CleankrHelpFloatingButton(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 16.dp, bottom = 16.dp),
      onHelpClick = { viewModel.navigateTo(AppScreen.HELP_SUPPORT) }
    )
  }
}
