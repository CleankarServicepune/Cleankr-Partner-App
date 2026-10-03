package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.notifications.CleankrMessagingService
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.CleankrDrawerContent
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.HelpSupportScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JobHistoryScreen
import com.example.ui.screens.KycScreen
import com.example.ui.screens.MoneyScreen
import com.example.ui.screens.MyHubScreen
import com.example.ui.screens.NewJobsScreen
import com.example.ui.screens.OngoingJobsScreen
import com.example.ui.screens.PrivacyLegalScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SecurityCenterScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TargetScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val viewModel: PartnerViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    CleankrMessagingService.fetchTokenSafely(applicationContext)

    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          CleankrPartnerApp(viewModel = viewModel)
        }
      }
    }
  }
}

@Composable
fun CleankrPartnerApp(viewModel: PartnerViewModel) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val activeLanguage by viewModel.selectedLanguage.collectAsState()
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  var showLanguageDialog by remember { mutableStateOf(false) }
  var showLogoutDialog by remember { mutableStateOf(false) }

  if (currentScreen != AppScreen.HOME) {
    BackHandler {
      viewModel.navigateTo(AppScreen.HOME)
    }
  }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet {
        CleankrDrawerContent(
          activeScreen = currentScreen,
          currentLanguage = activeLanguage,
          onNavigate = { screen ->
            scope.launch { drawerState.close() }
            viewModel.navigateTo(screen)
          },
          onLanguageClick = {
            scope.launch { drawerState.close() }
            showLanguageDialog = true
          },
          onLogoutClick = {
            scope.launch { drawerState.close() }
            showLogoutDialog = true
          }
        )
      }
    }
  ) {
    when (currentScreen) {
      AppScreen.HOME -> HomeScreen(viewModel = viewModel, onOpenDrawer = { scope.launch { drawerState.open() } })
      AppScreen.NEW_JOBS -> NewJobsScreen(viewModel = viewModel, onOpenDrawer = { scope.launch { drawerState.open() } })
      AppScreen.ONGOING_JOBS -> OngoingJobsScreen(viewModel = viewModel, onOpenDrawer = { scope.launch { drawerState.open() } })
      AppScreen.TARGET -> TargetScreen(viewModel = viewModel, onOpenDrawer = { scope.launch { drawerState.open() } })
      AppScreen.MONEY -> MoneyScreen(viewModel = viewModel, onOpenDrawer = { scope.launch { drawerState.open() } })
      AppScreen.CALENDAR -> CalendarScreen(viewModel = viewModel)
      AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
      AppScreen.JOB_HISTORY -> JobHistoryScreen(viewModel = viewModel)
      AppScreen.MY_HUB -> MyHubScreen(viewModel = viewModel)
      AppScreen.KYC_SKILL_INDIA -> KycScreen(viewModel = viewModel)
      AppScreen.SECURITY_CENTER -> SecurityCenterScreen(viewModel = viewModel)
      AppScreen.HELP_SUPPORT -> HelpSupportScreen(viewModel = viewModel)
      AppScreen.SETTINGS_NOTIFICATIONS -> SettingsScreen(viewModel = viewModel)
      AppScreen.PRIVACY_LEGAL -> PrivacyLegalScreen(viewModel = viewModel)
    }
  }

  if (showLanguageDialog) {
    val languages = listOf("English", "हिंदी (Hindi)", "मराठी (Marathi)", "ಕನ್ನಡ (Kannada)")
    AlertDialog(
      onDismissRequest = { showLanguageDialog = false },
      title = { Text("Select Partner App Language") },
      text = {
        Column {
          languages.forEach { lang ->
            TextButton(
              onClick = {
                viewModel.setLanguage(lang.split(" ")[0])
                showLanguageDialog = false
              },
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(text = lang, modifier = Modifier.fillMaxWidth())
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showLanguageDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  if (showLogoutDialog) {
    AlertDialog(
      onDismissRequest = { showLogoutDialog = false },
      title = { Text("Confirm Secure Logout") },
      text = {
        Text("Are you sure you want to end your session? Your duty status will remain active for already accepted orders.")
      },
      confirmButton = {
        Button(
          onClick = {
            showLogoutDialog = false
            viewModel.navigateTo(AppScreen.HOME)
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
          Text("Logout")
        }
      },
      dismissButton = {
        TextButton(onClick = { showLogoutDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
