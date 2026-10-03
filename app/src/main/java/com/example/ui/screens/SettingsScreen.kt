package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: PartnerViewModel) {
  var pushAlerts by remember { mutableStateOf(true) }
  var loudSiren by remember { mutableStateOf(true) }
  var smsUpdates by remember { mutableStateOf(true) }
  var autoAcceptNearby by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Settings & Notifications",
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic
          )
        },
        navigationIcon = {
          IconButton(onClick = { viewModel.navigateTo(AppScreen.HOME) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFBFBFB))
        .padding(padding)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
              text = "Dispatch & Sound Alerts",
              fontWeight = FontWeight.Black,
              fontSize = 16.sp,
              fontStyle = FontStyle.Italic
            )
            SettingToggle(
              title = "Push Notifications for Orders",
              subtitle = "Receive instant pop-up when orders are dispatched",
              checked = pushAlerts,
              onCheckedChange = { pushAlerts = it }
            )
            SettingToggle(
              title = "Loud Dispatch Ringtone",
              subtitle = "Play loud siren tone even in silent mode for 45s timer",
              checked = loudSiren,
              onCheckedChange = { loudSiren = it }
            )
            SettingToggle(
              title = "SMS Backup Notifications",
              subtitle = "Receive booking address details via SMS",
              checked = smsUpdates,
              onCheckedChange = { smsUpdates = it }
            )
            SettingToggle(
              title = "Auto-Accept Nearby Preferred Jobs",
              subtitle = "Automatically accept deep cleaning within 3 km radius",
              checked = autoAcceptNearby,
              onCheckedChange = { autoAcceptNearby = it }
            )
          }
        }
      }
    }
  }
}

@Composable
fun SettingToggle(
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
      Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
      Text(text = subtitle, fontSize = 12.sp, color = Color(0xFF757575))
    }
    Switch(checked = checked, onCheckedChange = onCheckedChange)
  }
}
