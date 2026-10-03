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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.BottomNavBarCleankr
import com.example.ui.components.CleankrTopHeader
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrViolet

@Composable
fun HomeScreen(
  viewModel: PartnerViewModel,
  onOpenDrawer: () -> Unit
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val isCheckedIn by viewModel.isCheckedIn.collectAsState()
  val dispatchStatus by viewModel.dispatchStatus.collectAsState()
  val countdownSeconds by viewModel.countdownSeconds.collectAsState()

  var showEmergencyDialog by remember { mutableStateOf(false) }
  var showHelpDialog by remember { mutableStateOf(false) }
  var showTrainingDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      CleankrTopHeader(
        onMenuClick = onOpenDrawer,
        onEmergencyClick = { showEmergencyDialog = true },
        onNavigate = { screen -> viewModel.navigateTo(screen) }
      )
    },
    bottomBar = {
      BottomNavBarCleankr(
        currentScreen = currentScreen,
        onTabSelect = { screen -> viewModel.navigateTo(screen) }
      )
    },
    floatingActionButton = {
      // Floating Help Button as shown in the screenshot
      Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF1E1E1E),
        shadowElevation = 4.dp,
        modifier = Modifier
          .height(44.dp)
          .clickable { showHelpDialog = true }
          .testTag("floating_help_button")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.HelpOutline,
            contentDescription = "Help",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "Help",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFBFBFB))
        .padding(paddingValues)
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 10.dp, bottom = 80.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Check-in to start your day card
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F0FB)),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("check_in_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(
              verticalArrangement = Arrangement.spacedBy(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text(
                text = if (isCheckedIn) "Checked in • On Duty" else "Check-in to start your day",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF1A1A1A)
              )

              Button(
                onClick = { viewModel.toggleCheckIn() },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (isCheckedIn) CleankrGreen else Color(0xFF4F2BED)
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
              ) {
                Text(
                  text = if (isCheckedIn) "Checked in" else "Check in",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  fontStyle = FontStyle.Italic
                )
              }
            }

            // Briefcase icon container with checkmark badge
            Box(
              modifier = Modifier
                .size(68.dp)
                .background(Color(0xFFEBE4F7), RoundedCornerShape(16.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.BusinessCenter,
                contentDescription = null,
                tint = Color(0xFF5A31F4),
                modifier = Modifier.size(34.dp)
              )

              // Green check circle badge
              Box(
                modifier = Modifier
                  .align(Alignment.BottomEnd)
                  .padding(4.dp)
                  .size(20.dp)
                  .background(Color(0xFF00C853), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(13.dp)
                )
              }
            }
          }
        }
      }

      // 2. Availability Schedule Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.navigateTo(AppScreen.CALENDAR) }
            .testTag("availability_summary_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Thu, Sep 24 • ",
                  fontWeight = FontWeight.Black,
                  fontSize = 14.sp,
                  fontStyle = FontStyle.Italic,
                  color = Color(0xFF1E1E1E)
                )
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF00C853), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "AVAILABLE",
                  fontWeight = FontWeight.Black,
                  fontSize = 13.sp,
                  fontStyle = FontStyle.Italic,
                  color = Color(0xFF1B5E20)
                )
              }

              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Fri, Sep 25 • ",
                  fontWeight = FontWeight.Black,
                  fontSize = 14.sp,
                  fontStyle = FontStyle.Italic,
                  color = Color(0xFF1E1E1E)
                )
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF00C853), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "AVAILABLE",
                  fontWeight = FontWeight.Black,
                  fontSize = 13.sp,
                  fontStyle = FontStyle.Italic,
                  color = Color(0xFF1B5E20)
                )
              }
            }

            Box(
              modifier = Modifier
                .size(46.dp)
                .background(Color(0xFFF3E8FF), CircleShape)
                .clickable { viewModel.navigateTo(AppScreen.CALENDAR) },
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Open Calendar",
                tint = Color(0xFF5A31F4),
                modifier = Modifier.size(22.dp)
              )
            }
          }
        }
      }

      // 3. NEW DISPATCH REQUEST Card
      if (dispatchStatus != "PASSED") {
        item {
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF6F1)),
            border = BorderStroke(1.5.dp, Color(0xFFFFCCBC)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("dispatch_request_card")
          ) {
            Column(
              modifier = Modifier.padding(18.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Header with Bell and Timer
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Text(text = "🔔", fontSize = 16.sp)
                  Text(
                    text = "NEW DISPATCH REQUEST",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFF212121),
                    letterSpacing = 0.4.sp
                  )
                }

                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = Color(0xFFFF5722)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Text(text = "⏱", fontSize = 12.sp)
                    Text(
                      text = "${countdownSeconds}s left",
                      color = Color.White,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      fontStyle = FontStyle.Italic
                    )
                  }
                }
              }

              Text(
                text = "Full Deep Villa Cleaning",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF1E1E1E)
              )

              Text(
                text = "B-1402, Oberoi Springs, Off Link Road, Andheri West, Mumbai • Payout: ₹2850",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF333333),
                lineHeight = 18.sp
              )

              Spacer(modifier = Modifier.height(2.dp))

              if (dispatchStatus == "ACCEPTED") {
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = Color(0xFFE8F5E9),
                  border = BorderStroke(1.dp, Color(0xFFA5D6A7)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    text = "✓ Order Accepted! Dispatched to Ongoing queue.",
                    color = Color(0xFF1B5E20),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(12.dp)
                  )
                }
              } else {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                  Button(
                    onClick = { viewModel.acceptDispatchOrder() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier.weight(2f)
                  ) {
                    Text(
                      text = "Accept Order",
                      fontWeight = FontWeight.Black,
                      fontSize = 15.sp,
                      fontStyle = FontStyle.Italic,
                      color = Color.White
                    )
                  }

                  Button(
                    onClick = { viewModel.passDispatchOrder() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECEFF1)),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier.weight(1f)
                  ) {
                    Text(
                      text = "Pass",
                      fontWeight = FontWeight.Black,
                      fontSize = 15.sp,
                      fontStyle = FontStyle.Italic,
                      color = Color(0xFF37474F)
                    )
                  }
                }
              }
            }
          }
        }
      }

      // 4. Jobs counter card (1 new jobs available / No jobs today)
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(AppScreen.NEW_JOBS) }
                .padding(horizontal = 18.dp, vertical = 16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "1 new jobs available",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF1E1E1E)
              )
              Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(20.dp)
              )
            }

            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(AppScreen.ONGOING_JOBS) }
                .padding(horizontal = 18.dp, vertical = 16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "No jobs today",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF1E1E1E)
              )
              Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }
      }

      // 5. Upgrade to Bathroom Cleaning card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showTrainingDialog = true }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .background(Color(0xFFFFF3E0), RoundedCornerShape(14.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(30.dp)
              )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                text = "Upgrade to Bathroom Cleaning",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF1E1E1E)
              )
              Text(
                text = "Complete your training >",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF283593)
              )
            }
          }
        }
      }
    }
  }

  // Emergency SOS Dialog
  if (showEmergencyDialog) {
    AlertDialog(
      onDismissRequest = { showEmergencyDialog = false },
      title = { Text("Emergency Response Alert", fontWeight = FontWeight.Bold) },
      text = {
        Text("Immediate SOS partner dispatch is activated. Our 24/7 emergency response team has been alerted with your live location coordinates.")
      },
      confirmButton = {
        Button(
          onClick = { showEmergencyDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
          Text("Call Helpline Now")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEmergencyDialog = false }) {
          Text("Dismiss")
        }
      }
    )
  }

  // Help Dialog
  if (showHelpDialog) {
    AlertDialog(
      onDismissRequest = { showHelpDialog = false },
      title = { Text("Cleankr Partner Support", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Toll Free: 1800-CLEANKR-PARTNER", fontWeight = FontWeight.Bold, color = CleankrViolet)
          Text("Email: partner-support@cleankr.in")
          Text("Live partner support is active 24/7.")
        }
      },
      confirmButton = {
        TextButton(onClick = { showHelpDialog = false }) {
          Text("Close")
        }
      }
    )
  }

  // Training Dialog
  if (showTrainingDialog) {
    AlertDialog(
      onDismissRequest = { showTrainingDialog = false },
      title = { Text("Partner Training & Upskilling", fontWeight = FontWeight.Bold) },
      text = {
        Text("Bathroom & Tile Deep Cleaning module is ready. Complete this 10-minute video certification to unlock jobs with 25% higher payouts.")
      },
      confirmButton = {
        Button(
          onClick = { showTrainingDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = CleankrViolet)
        ) {
          Text("Start Training")
        }
      },
      dismissButton = {
        TextButton(onClick = { showTrainingDialog = false }) {
          Text("Later")
        }
      }
    )
  }
}
