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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.BottomNavBarCleankr
import com.example.ui.components.CleankrTopHeader

@Composable
fun TargetScreen(
  viewModel: PartnerViewModel,
  onOpenDrawer: () -> Unit
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  var showStrikesDialog by remember { mutableStateOf(false) }
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
      // Header row
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Low performance",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF1E1E1E)
          )

          Text(
            text = "Learn about strikes",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF1A237E),
            modifier = Modifier.clickable { showStrikesDialog = true }
          )
        }
      }

      // Warning Strike Banner
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFDECEF)),
          border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showStrikesDialog = true }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              modifier = Modifier.weight(1f),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .background(Color(0xFFFFCDD2), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.TrendingDown,
                  contentDescription = null,
                  tint = Color(0xFFD32F2F),
                  modifier = Modifier.size(20.dp)
                )
              }

              Text(
                text = "Low performance. You might get a profile strike on 28 Sep >",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF212121),
                lineHeight = 18.sp
              )
            }

            Icon(
              imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
              contentDescription = null,
              tint = Color(0xFF9E9E9E),
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      // Metrics title
      item {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
          Text(
            text = "Metrics",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF1E1E1E)
          )
          Text(
            text = "Next performance review on 28 Sep",
            fontSize = 13.sp,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF616161)
          )
        }
      }

      // Metric Row 1: Rating & Cancellations
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          MetricBox(
            modifier = Modifier.weight(1f),
            title = "Rating",
            condition = "Keep 4.6 or above",
            score = "4.92",
            isPass = true
          )

          MetricBox(
            modifier = Modifier.weight(1f),
            title = "Cancellations",
            condition = "Keep 4 or below",
            score = "6",
            isPass = false
          )
        }
      }

      // Metric Row 2: Peak & Weekend unavailable hours
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          MetricBox(
            modifier = Modifier.weight(1f),
            title = "Peak unavailable hours",
            condition = "Keep 48 or below",
            score = "40.64",
            isPass = true
          )

          MetricBox(
            modifier = Modifier.weight(1f),
            title = "Weekend unavailable hours",
            condition = "Keep 30 or below",
            score = "30",
            isPass = true
          )
        }
      }

      // Metric Row 3: Audit failures
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          MetricBox(
            modifier = Modifier.weight(1f),
            title = "Audit failures",
            condition = "Keep 100 or below",
            score = "2",
            isPass = true
          )

          Spacer(modifier = Modifier.weight(1f))
        }
      }
    }
  }

  if (showStrikesDialog) {
    AlertDialog(
      onDismissRequest = { showStrikesDialog = false },
      title = { Text("Profile Strike Guidelines", fontWeight = FontWeight.Bold) },
      text = {
        Text("Partners are allowed up to 4 cancellations per cycle. Your current cancellations (6) exceed the threshold. Complete 5 consecutive on-time orders without cancellation to reset your strike status.")
      },
      confirmButton = {
        TextButton(onClick = { showStrikesDialog = false }) {
          Text("Got It")
        }
      }
    )
  }

  if (showHelpDialog) {
    AlertDialog(
      onDismissRequest = { showHelpDialog = false },
      title = { Text("Performance & Audit Support") },
      text = {
        Text("Need assistance disputing a cancellation strike? Call 1800-CLEANKR-PARTNER or speak with your city hub supervisor.")
      },
      confirmButton = {
        TextButton(onClick = { showHelpDialog = false }) {
          Text("Close")
        }
      }
    )
  }
}

@Composable
fun MetricBox(
  modifier: Modifier = Modifier,
  title: String,
  condition: String,
  score: String,
  isPass: Boolean
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        color = Color(0xFF1E1E1E)
      )

      Text(
        text = condition,
        fontSize = 12.sp,
        fontStyle = FontStyle.Italic,
        color = Color(0xFF616161)
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text(
          text = score,
          fontSize = 20.sp,
          fontWeight = FontWeight.Black,
          color = Color(0xFF1E1E1E)
        )

        if (isPass) {
          Text(
            text = "✓",
            color = Color(0xFF00C853),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black
          )
        } else {
          Text(
            text = "✕",
            color = Color(0xFFD32F2F),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black
          )
        }
      }
    }
  }
}
