package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppLanguage
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.ThemeMode
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrMagenta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  viewModel: PartnerViewModel,
  onBack: () -> Unit
) {
  val currentTheme by viewModel.themeMode.collectAsState()
  val currentLang by viewModel.language.collectAsState()

  var soundAlertsEnabled by remember { mutableStateOf(true) }
  var pushNotificationsEnabled by remember { mutableStateOf(true) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("App Settings", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
          }
        }
      )
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Theme Preference
      item {
        Text("Appearance & Theme", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Select Theme Mode", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              FilterChip(
                selected = currentTheme == ThemeMode.DARK,
                onClick = { viewModel.setThemeMode(ThemeMode.DARK) },
                label = { Text("Dark Theme") },
                leadingIcon = { Icon(Icons.Default.DarkMode, contentDescription = null) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CleankrMagenta.copy(alpha = 0.2f), selectedLabelColor = CleankrMagenta),
                modifier = Modifier.weight(1f)
              )
              FilterChip(
                selected = currentTheme == ThemeMode.LIGHT,
                onClick = { viewModel.setThemeMode(ThemeMode.LIGHT) },
                label = { Text("Light Theme") },
                leadingIcon = { Icon(Icons.Default.LightMode, contentDescription = null) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CleankrCoral.copy(alpha = 0.2f), selectedLabelColor = CleankrCoral),
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }

      // Language Selection
      item {
        Text("Language / भाषा", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("App Interface Language", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              FilterChip(
                selected = currentLang == AppLanguage.ENGLISH,
                onClick = { viewModel.setLanguage(AppLanguage.ENGLISH) },
                label = { Text("English") },
                leadingIcon = { Icon(Icons.Default.Language, contentDescription = null) },
                modifier = Modifier.weight(1f)
              )
              FilterChip(
                selected = currentLang == AppLanguage.HINDI,
                onClick = { viewModel.setLanguage(AppLanguage.HINDI) },
                label = { Text("हिन्दी (Hindi)") },
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }

      // Notification & Alerts
      item {
        Text("Audio & Notification Alerts", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text("Loud Ring on New Booking", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text("Plays high priority sound when new dispatch arrives", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
              Switch(
                checked = soundAlertsEnabled,
                onCheckedChange = { soundAlertsEnabled = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = CleankrCoral)
              )
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text("Push Notifications", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text("Booking reminders, payout receipts and policy notices", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
              Switch(
                checked = pushNotificationsEnabled,
                onCheckedChange = { pushNotificationsEnabled = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = CleankrCoral)
              )
            }

            // System App Notification Settings Shortcut for permanently denied or deep config
            val context = androidx.compose.ui.platform.LocalContext.current
            androidx.compose.material3.OutlinedButton(
              onClick = {
                try {
                  val intent = android.content.Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.packageName)
                  }
                  context.startActivity(intent)
                } catch (e: Exception) {
                  val fallback = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = android.net.Uri.fromParts("package", context.packageName, null)
                  }
                  context.startActivity(fallback)
                }
              },
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth().testTag("system_notification_settings_button")
            ) {
              Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.size(6.dp))
              Text("Device Notification Permission Settings", fontSize = 12.sp)
            }
          }
        }
      }

      // Privacy & Legal Section
      item {
        Text("Privacy & Legal Compliance", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.navigateTo(AppScreen.PRIVACY_LEGAL) }
            .testTag("settings_privacy_legal_card")
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Icon(Icons.Default.Policy, contentDescription = null, tint = CleankrCoral)
              Column {
                Text("Privacy & Legal Hub", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text("Privacy policy, terms, refunds & account deletion", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
          }
        }
      }

      // App Build Details
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text("Cleankr Partner • Android v2.6.4 (Release 2026)", fontWeight = FontWeight.Bold, fontSize = 11.sp)
            Text("Optimized for Snapdragon, Dimensity, Exynos and Tensor chipsets. Zero background battery drain.", fontSize = 10.sp, color = Color.Gray)
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
