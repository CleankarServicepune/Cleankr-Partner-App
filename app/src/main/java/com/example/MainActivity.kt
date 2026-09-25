package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.ThemeMode
import com.example.ui.components.CleankrBottomNav
import com.example.ui.components.CleankrDrawerContent
import com.example.ui.components.CleankrTopBar
import com.example.ui.screens.AccountDeletionScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EarningsScreen
import com.example.ui.screens.HelpSupportScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.JobDetailScreen
import com.example.ui.screens.KycScreen
import com.example.ui.screens.NewJobsScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.PrivacyLegalScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SecurityCenterScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TargetScreen
import com.example.ui.theme.CleankrPartnerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  private val viewModel: PartnerViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Safely initialize FCM and retrieve token when network/Play Services are available
    com.example.notifications.CleankrMessagingService.fetchTokenSafely(applicationContext)

    setContent {
      val themeMode by viewModel.themeMode.collectAsState()
      val isDark = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
      }

      CleankrPartnerTheme(darkTheme = isDark) {
        CleankrPartnerApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun CleankrPartnerApp(viewModel: PartnerViewModel) {
  val isAuthenticated by viewModel.isAuthenticated.collectAsState()
  val currentScreen by viewModel.currentScreen.collectAsState()
  val selectedJobId by viewModel.selectedJobId.collectAsState()
  val profile by viewModel.profile.collectAsState()
  val coinsBalance by viewModel.coinsBalance.collectAsState()
  val unreadNotificationsCount by viewModel.unreadNotificationsCount.collectAsState()
  val userFeedback by viewModel.userFeedback.collectAsState()

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(userFeedback) {
    userFeedback?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearFeedback()
    }
  }

  // Not authenticated: show Auth screen (Mobile + OTP)
  if (!isAuthenticated) {
    AuthScreen(viewModel = viewModel)
    return
  }

  // Determine if current screen is one of the 5 main bottom navigation tabs
  val isTopLevel = currentScreen in listOf(
    AppScreen.DASHBOARD,
    AppScreen.NEW_JOBS,
    AppScreen.HISTORY,
    AppScreen.TARGET,
    AppScreen.EARNINGS
  )

  BackHandler(enabled = !isTopLevel || drawerState.isOpen) {
    if (drawerState.isOpen) {
      scope.launch { drawerState.close() }
    } else {
      viewModel.navigateTo(AppScreen.DASHBOARD)
    }
  }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      CleankrDrawerContent(
        viewModel = viewModel,
        onCloseDrawer = {
          scope.launch { drawerState.close() }
        }
      )
    }
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        CleankrTopBar(
          coins = coinsBalance,
          unreadCount = unreadNotificationsCount,
          title = when (currentScreen) {
            AppScreen.JOB_DETAILS -> "Job Details"
            AppScreen.CALENDAR -> "Calendar & Availability"
            AppScreen.SECURITY_CENTER -> "Security Center"
            AppScreen.KYC -> "KYC Verification"
            AppScreen.NOTIFICATIONS -> "Notifications"
            AppScreen.HELP_SUPPORT -> "Help & Support"
            AppScreen.PROFILE -> "Partner Profile"
            AppScreen.SETTINGS -> "Settings"
            AppScreen.PRIVACY_POLICY -> "Privacy Policy"
            AppScreen.PRIVACY_LEGAL -> "Privacy & Legal"
            AppScreen.ACCOUNT_DELETION -> "Delete Account"
            else -> null
          },
          onOpenDrawer = {
            scope.launch { drawerState.open() }
          },
          onEmergencyClick = {
            viewModel.triggerSosEmergency()
          },
          onOpenNotifications = {
            viewModel.navigateTo(AppScreen.NOTIFICATIONS)
          },
          canNavigateBack = !isTopLevel,
          onBackClick = {
            viewModel.navigateTo(AppScreen.DASHBOARD)
          }
        )
      },
      bottomBar = {
        if (isTopLevel) {
          CleankrBottomNav(
            currentScreen = currentScreen,
            ongoingBadgeCount = 0,
            onScreenSelected = { viewModel.navigateTo(it) }
          )
        }
      },
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
      }
    ) { innerPadding ->
      AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screenTransition",
        modifier = Modifier.padding(innerPadding)
      ) { screen ->
        when (screen) {
          AppScreen.AUTH -> AuthScreen(viewModel = viewModel)
          AppScreen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
          AppScreen.NEW_JOBS -> NewJobsScreen(viewModel = viewModel)
          AppScreen.HISTORY -> HistoryScreen(viewModel = viewModel)
          AppScreen.TARGET -> TargetScreen(viewModel = viewModel)
          AppScreen.EARNINGS -> EarningsScreen(viewModel = viewModel)
          AppScreen.CALENDAR -> CalendarScreen(viewModel = viewModel)
          AppScreen.JOB_DETAILS -> JobDetailScreen(
            viewModel = viewModel,
            jobId = selectedJobId ?: "CK-1092",
            onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) }
          )
          AppScreen.SECURITY_CENTER -> SecurityCenterScreen(viewModel = viewModel)
          AppScreen.KYC -> KycScreen(
            viewModel = viewModel,
            onBack = { viewModel.navigateTo(AppScreen.PROFILE) }
          )
          AppScreen.NOTIFICATIONS -> NotificationsScreen(
            viewModel = viewModel,
            onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) }
          )
          AppScreen.HELP_SUPPORT -> HelpSupportScreen(
            viewModel = viewModel,
            onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) }
          )
          AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
          AppScreen.SETTINGS -> SettingsScreen(
            viewModel = viewModel,
            onBack = { viewModel.navigateTo(AppScreen.PROFILE) }
          )
          AppScreen.PRIVACY_POLICY -> PrivacyPolicyScreen(
            onBack = { viewModel.navigateTo(AppScreen.PROFILE) }
          )
          AppScreen.PRIVACY_LEGAL -> PrivacyLegalScreen(
            viewModel = viewModel,
            onBack = { viewModel.navigateTo(AppScreen.PROFILE) }
          )
          AppScreen.ACCOUNT_DELETION -> AccountDeletionScreen(
            viewModel = viewModel,
            onBack = { viewModel.navigateTo(AppScreen.PROFILE) }
          )
        }
      }
    }
  }
}
