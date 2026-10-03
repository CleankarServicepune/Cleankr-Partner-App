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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.BottomNavBarCleankr
import com.example.ui.components.CleankrTopHeader
import com.example.ui.theme.CleankrGreen

@Composable
fun NewJobsScreen(
  viewModel: PartnerViewModel,
  onOpenDrawer: () -> Unit
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val dispatchStatus by viewModel.dispatchStatus.collectAsState()
  val countdownSeconds by viewModel.countdownSeconds.collectAsState()
  var showHelpDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      CleankrTopHeader(
        onMenuClick = onOpenDrawer,
        onEmergencyClick = { viewModel.navigateTo(AppScreen.SECURITY_CENTER) },
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
      Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF1E1E1E),
        shadowElevation = 4.dp,
        modifier = Modifier
          .height(44.dp)
          .clickable { showHelpDialog = true }
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
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFBFBFB))
        .padding(padding)
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "New Jobs",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF1E1E1E)
          )
          Text(
            text = "Instant booking dispatch requests in your area",
            fontSize = 13.sp,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF616161)
          )
        }
      }

      if (dispatchStatus == "ACCEPTED") {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
            border = BorderStroke(1.dp, Color(0xFFA5D6A7)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "✓ Job Accepted! You have assigned Full Deep Villa Cleaning to your active tasks.",
              color = Color(0xFF1B5E20),
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              modifier = Modifier.padding(18.dp)
            )
          }
        }
      } else if (dispatchStatus != "PASSED") {
        item {
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(18.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = Color(0xFFFBE9E7)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Text(text = "⏱", fontSize = 12.sp)
                    Text(
                      text = "${countdownSeconds}s to accept",
                      color = Color(0xFFD84315),
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp,
                      fontStyle = FontStyle.Italic
                    )
                  }
                }

                Text(
                  text = "₹2850",
                  color = CleankrGreen,
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Black,
                  fontStyle = FontStyle.Italic
                )
              }

              Text(
                text = "Full Deep Villa Cleaning",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF1E1E1E)
              )

              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.AccessTime,
                  contentDescription = null,
                  tint = Color(0xFF757575),
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = "2026-09-27 • 10:30 AM – 02:30 PM",
                  fontSize = 13.sp,
                  fontStyle = FontStyle.Italic,
                  color = Color(0xFF424242)
                )
              }

              Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.LocationOn,
                  contentDescription = null,
                  tint = Color(0xFF757575),
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = "B-1402, Oberoi Springs, Off Link Road, Andheri West, Mumbai",
                  fontSize = 13.sp,
                  fontStyle = FontStyle.Italic,
                  color = Color(0xFF424242),
                  lineHeight = 18.sp
                )
              }

              Spacer(modifier = Modifier.height(4.dp))

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
      } else {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "No pending dispatch orders",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 15.sp,
                color = Color(0xFF757575)
              )
            }
          }
        }
      }
    }
  }
}
