package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import com.example.ui.components.BottomNavBarCleankr
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.CleankrTopHeader

@Composable
fun OngoingJobsScreen(
  viewModel: PartnerViewModel,
  onOpenDrawer: () -> Unit
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val selectedTab by viewModel.selectedOngoingTab.collectAsState()
  val tabs = listOf("Upcoming", "Pending", "Completed", "Cancelled")

  var callDialogJob by remember { mutableStateOf<String?>(null) }
  var mapDialogJob by remember { mutableStateOf<String?>(null) }
  var showHelpDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      Column(modifier = Modifier.background(Color.White)) {
        CleankrTopHeader(
          onMenuClick = onOpenDrawer,
          onEmergencyClick = { viewModel.navigateTo(AppScreen.SECURITY_CENTER) },
          onNavigate = { screen -> viewModel.navigateTo(screen) }
        )

        // Sub-tabs row matching screenshot
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = Color.White,
          contentColor = Color.Black,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = Color.Black,
              height = 2.dp
            )
          },
          divider = { HorizontalDivider(color = Color(0xFFEEEEEE)) }
        ) {
          tabs.forEachIndexed { index, tabTitle ->
            Tab(
              selected = selectedTab == index,
              onClick = { viewModel.setSelectedOngoingTab(index) },
              text = {
                Text(
                  text = tabTitle,
                  fontWeight = if (selectedTab == index) FontWeight.Black else FontWeight.Bold,
                  fontStyle = FontStyle.Italic,
                  fontSize = 13.sp,
                  color = if (selectedTab == index) Color.Black else Color(0xFF757575)
                )
              }
            )
          }
        }
      }
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
      if (selectedTab == 0) {
        // Today Section
        item {
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
              text = "Today",
              fontSize = 18.sp,
              fontWeight = FontWeight.Black,
              fontStyle = FontStyle.Italic,
              color = Color(0xFF1E1E1E)
            )
            Text(
              text = "No jobs scheduled for today.",
              fontSize = 14.sp,
              fontStyle = FontStyle.Italic,
              color = Color(0xFF616161)
            )
          }
        }

        // Tomorrow Section
        item {
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
              text = "Tomorrow",
              fontSize = 18.sp,
              fontWeight = FontWeight.Black,
              fontStyle = FontStyle.Italic,
              color = Color(0xFF1E1E1E)
            )
            Text(
              text = "No jobs scheduled for tomorrow.",
              fontSize = 14.sp,
              fontStyle = FontStyle.Italic,
              color = Color(0xFF616161)
            )
          }
        }

        // Other Dates Section
        item {
          Text(
            text = "Other Dates",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF1E1E1E)
          )
        }

        // Card 1: ASSIGNED
        item {
          JobOngoingCard(
            statusPill = "ASSIGNED",
            statusPillBg = Color(0xFFEDE7F6),
            statusPillText = Color(0xFF3F51B5),
            timeSlot = "10:30 AM – 02:30 PM",
            customerName = "Ananya Sharma",
            serviceTitle = "Full Deep Villa Cleaning",
            onCall = { callDialogJob = "Ananya Sharma" },
            onNavigate = { mapDialogJob = "Ananya Sharma (B-1402, Oberoi Springs)" }
          )
        }

        // Card 2: ACCEPTED
        item {
          JobOngoingCard(
            statusPill = "ACCEPTED",
            statusPillBg = Color(0xFFEDE7F6),
            statusPillText = Color(0xFF5A31F4),
            timeSlot = "03:30 PM – 05:30 PM",
            customerName = "Rohit Verma",
            serviceTitle = "Sofa & Carpet Shampooing",
            onCall = { callDialogJob = "Rohit Verma" },
            onNavigate = { mapDialogJob = "Rohit Verma (A-402, Palm Beach)" }
          )
        }

        // Card 3: COMPLETED
        item {
          JobOngoingCard(
            statusPill = "COMPLETED",
            statusPillBg = Color(0xFFE8F5E9),
            statusPillText = Color(0xFF2E7D32),
            timeSlot = "11:00 AM – 01:00 PM",
            customerName = "Pooja Verma",
            serviceTitle = "Bathroom Cleaning & Descaling",
            onCall = { callDialogJob = "Pooja Verma" },
            onNavigate = { mapDialogJob = "Pooja Verma (Flat 204, Green Heights)" }
          )
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
                text = "No ${tabs[selectedTab].lowercase()} jobs at the moment",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF757575)
              )
            }
          }
        }
      }
    }
  }

  // Masked calling dialog
  if (callDialogJob != null) {
    AlertDialog(
      onDismissRequest = { callDialogJob = null },
      title = { Text("Cleankr Number Masking Bridge") },
      text = {
        Text("Connecting private masked call with $callDialogJob. Your personal partner phone number is 100% hidden and protected.")
      },
      confirmButton = {
        TextButton(onClick = { callDialogJob = null }) {
          Text("Call via Bridge")
        }
      },
      dismissButton = {
        TextButton(onClick = { callDialogJob = null }) {
          Text("Cancel")
        }
      }
    )
  }

  // Turn by turn navigation dialog
  if (mapDialogJob != null) {
    AlertDialog(
      onDismissRequest = { mapDialogJob = null },
      title = { Text("Navigation Directions") },
      text = {
        Text("Route opened to destination: $mapDialogJob. Estimated travel time: 14 mins.")
      },
      confirmButton = {
        TextButton(onClick = { mapDialogJob = null }) {
          Text("Start Guidance")
        }
      }
    )
  }
}

@Composable
fun JobOngoingCard(
  statusPill: String,
  statusPillBg: Color,
  statusPillText: Color,
  timeSlot: String,
  customerName: String,
  serviceTitle: String,
  onCall: () -> Unit,
  onNavigate: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = statusPillBg
        ) {
          Text(
            text = statusPill,
            color = statusPillText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
          )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = timeSlot,
          fontSize = 16.sp,
          fontWeight = FontWeight.Black,
          color = Color(0xFF1E1E1E)
        )

        Text(
          text = customerName,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          color = Color(0xFF424242)
        )

        Text(
          text = serviceTitle,
          fontSize = 13.sp,
          fontStyle = FontStyle.Italic,
          color = Color(0xFF616161)
        )
      }

      // Circular action buttons
      Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .background(Color.White, CircleShape)
            .border(BorderStroke(1.dp, Color(0xFFE0E0E0)), CircleShape)
            .clickable { onCall() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "Call Customer",
            tint = Color(0xFF212121),
            modifier = Modifier.size(20.dp)
          )
        }

        Box(
          modifier = Modifier
            .size(46.dp)
            .background(Color.White, CircleShape)
            .border(BorderStroke(1.dp, Color(0xFFE0E0E0)), CircleShape)
            .clickable { onNavigate() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.NearMe,
            contentDescription = "Directions",
            tint = Color(0xFF212121),
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
