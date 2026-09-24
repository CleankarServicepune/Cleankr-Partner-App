package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobStatus
import com.example.ui.AppScreen
import com.example.ui.theme.CleankrAmber
import com.example.ui.theme.CleankrAmberContainer
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrCoralDark
import com.example.ui.theme.CleankrCyan
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrGreenContainer
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrMagentaDark
import com.example.ui.theme.CleankrPeach
import com.example.ui.theme.CleankrRed
import com.example.ui.theme.CleankrRedContainer
import com.example.ui.theme.CleankrViolet

/**
 * High-fidelity vector rendering of the official Cleankr "CK" Monogram
 * matching the user's uploaded logo: Interlocking magenta/violet C and coral K.
 */
@Composable
fun CleankrMonogram(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 48.dp
) {
  Canvas(modifier = modifier.size(sizeDp).testTag("cleankr_monogram")) {
    val w = size.width
    val h = size.height

    // Magenta to Coral gradient brush
    val brandGradient = Brush.linearGradient(
      colors = listOf(CleankrMagenta, CleankrViolet, CleankrCoral, CleankrPeach),
      start = Offset(0f, 0f),
      end = Offset(w, h)
    )

    val strokeW = w * 0.09f

    // 1. Draw elegant Serif "C" arc on the left
    val cPath = Path().apply {
      // Start top serif
      moveTo(w * 0.50f, h * 0.22f)
      // Top curve
      cubicTo(w * 0.30f, h * 0.16f, w * 0.12f, h * 0.32f, w * 0.12f, h * 0.50f)
      // Bottom curve
      cubicTo(w * 0.12f, h * 0.70f, w * 0.30f, h * 0.84f, w * 0.50f, h * 0.78f)
    }
    drawPath(
      path = cPath,
      brush = Brush.verticalGradient(
        colors = listOf(CleankrPeach, CleankrMagenta, CleankrViolet)
      ),
      style = Stroke(width = strokeW, cap = StrokeCap.Round)
    )

    // 2. Draw vertical stem of 'I'/'K' inside C
    val stemPath = Path().apply {
      moveTo(w * 0.40f, h * 0.22f)
      lineTo(w * 0.40f, h * 0.78f)
    }
    drawPath(
      path = stemPath,
      brush = Brush.verticalGradient(
        colors = listOf(CleankrViolet, CleankrMagentaDark)
      ),
      style = Stroke(width = strokeW * 1.3f, cap = StrokeCap.Square)
    )

    // 3. Draw calligraphic diagonal arms of 'K' extending outward
    val kUpperArm = Path().apply {
      moveTo(w * 0.44f, h * 0.50f)
      cubicTo(w * 0.50f, h * 0.42f, w * 0.55f, h * 0.24f, w * 0.58f, h * 0.22f)
    }
    drawPath(
      path = kUpperArm,
      color = CleankrCoral,
      style = Stroke(width = strokeW * 0.85f, cap = StrokeCap.Round)
    )

    val kLowerArm = Path().apply {
      moveTo(w * 0.42f, h * 0.48f)
      cubicTo(w * 0.52f, h * 0.65f, w * 0.64f, h * 0.82f, w * 0.78f, h * 0.80f)
    }
    drawPath(
      path = kLowerArm,
      brush = Brush.linearGradient(
        colors = listOf(CleankrCoral, CleankrPeach),
        start = Offset(w * 0.42f, h * 0.48f),
        end = Offset(w * 0.78f, h * 0.80f)
      ),
      style = Stroke(width = strokeW, cap = StrokeCap.Round)
    )
  }
}

/**
 * Top App Bar with Logo, Online/Offline switch, Security status, and Notifications
 */
/**
 * High-fidelity Top Bar matching Urban Company Partner structure:
 * Left: Hamburger Menu (☰) or Back button
 * Right: Coins pill (77 ⟐), Notifications bell (🔔 5), Emergency button (🚨)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleankrTopBar(
  coins: Int = 77,
  unreadCount: Int = 5,
  title: String? = null,
  onOpenDrawer: () -> Unit = {},
  onEmergencyClick: () -> Unit = {},
  onOpenNotifications: () -> Unit = {},
  canNavigateBack: Boolean = false,
  onBackClick: () -> Unit = {}
) {
  TopAppBar(
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surface,
      titleContentColor = MaterialTheme.colorScheme.onSurface
    ),
    navigationIcon = {
      if (canNavigateBack) {
        IconButton(
          onClick = onBackClick,
          modifier = Modifier.testTag("top_bar_back_button")
        ) {
          Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }
      } else {
        IconButton(
          onClick = onOpenDrawer,
          modifier = Modifier.testTag("top_bar_menu_button")
        ) {
          Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Open Drawer Menu",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(26.dp)
          )
        }
      }
    },
    title = {
      if (canNavigateBack && title != null) {
        Text(
          text = title,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          CleankrMonogram(sizeDp = 28.dp)
          Text(
            text = "CLEANKR",
            fontWeight = FontWeight.Black,
            fontSize = 15.sp,
            color = Color(0xFF1E1A22),
            letterSpacing = 1.sp
          )
        }
      }
    },
    actions = {
      // 1. Coins Pill Badge (e.g. 77 ⟐)
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
        modifier = Modifier.padding(end = 6.dp)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
          Text(
            text = "$coins",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "⟐",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = CleankrViolet
          )
        }
      }

      // 2. Notification Bell with Badge
      IconButton(
        onClick = onOpenNotifications,
        modifier = Modifier.size(38.dp).testTag("notifications_button")
      ) {
        BadgedBox(
          badge = {
            if (unreadCount > 0) {
              Badge(
                containerColor = CleankrRed,
                contentColor = Color.White
              ) {
                Text(text = "$unreadCount", fontSize = 9.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        ) {
          Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = "Notifications",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
          )
        }
      }

      // 3. Emergency SOS Pill Button
      Surface(
        shape = RoundedCornerShape(18.dp),
        color = CleankrRed.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, CleankrRed.copy(alpha = 0.45f)),
        modifier = Modifier
          .padding(start = 2.dp, end = 8.dp)
          .clickable(onClick = onEmergencyClick)
          .testTag("emergency_button")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = "Emergency",
            color = CleankrRed,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
          Icon(
            imageVector = Icons.Default.NotificationsActive,
            contentDescription = null,
            tint = CleankrRed,
            modifier = Modifier.size(13.dp)
          )
        }
      }
    }
  )
}

/**
 * Status Badge for Lifecycle: Assigned, Accepted, On the Way, Arrived, Started, Completed
 */
@Composable
fun StatusBadge(
  status: JobStatus,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor, label) = when (status) {
    JobStatus.ASSIGNED -> Triple(CleankrAmberContainer, CleankrAmber, "NEW ASSIGNED")
    JobStatus.ACCEPTED -> Triple(Color(0xFFE1F5FE), Color(0xFF0288D1), "ACCEPTED")
    JobStatus.ON_THE_WAY -> Triple(Color(0xFFEDE7F6), CleankrViolet, "ON THE WAY")
    JobStatus.ARRIVED -> Triple(Color(0xFFFFF3E0), CleankrCoralDark, "ARRIVED ON SITE")
    JobStatus.STARTED -> Triple(Color(0xFFFCE4EC), CleankrMagenta, "IN PROGRESS")
    JobStatus.COMPLETED -> Triple(CleankrGreenContainer, CleankrGreen, "COMPLETED")
    JobStatus.CANCELLED -> Triple(CleankrRedContainer, CleankrRed, "CANCELLED")
    JobStatus.RESCHEDULED -> Triple(Color(0xFFECEFF1), Color(0xFF546E7A), "RESCHEDULED")
  }

  Surface(
    shape = RoundedCornerShape(6.dp),
    color = bgColor,
    modifier = modifier.testTag("status_badge_${status.name.lowercase()}")
  ) {
    Text(
      text = label,
      color = textColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
  }
}

/**
 * Bottom Navigation Bar with 5 tabs matching Urban Company Partner:
 * 1. Home
 * 2. New
 * 3. Ongoing
 * 4. Target
 * 5. Money
 */
@Composable
fun CleankrBottomNav(
  currentScreen: AppScreen,
  ongoingBadgeCount: Int = 0,
  onScreenSelected: (AppScreen) -> Unit
) {
  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 4.dp,
    modifier = Modifier.testTag("cleankr_bottom_nav")
  ) {
    // 1. Home Tab
    NavigationBarItem(
      selected = currentScreen == AppScreen.DASHBOARD,
      onClick = { onScreenSelected(AppScreen.DASHBOARD) },
      icon = {
        Icon(
          if (currentScreen == AppScreen.DASHBOARD) Icons.Filled.Home else Icons.Outlined.Home,
          contentDescription = "Home"
        )
      },
      label = { Text("Home", fontSize = 11.sp, fontWeight = if (currentScreen == AppScreen.DASHBOARD) FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0xFF1E1A22),
        selectedTextColor = Color(0xFF1E1A22),
        unselectedIconColor = Color(0xFF757575),
        unselectedTextColor = Color(0xFF757575),
        indicatorColor = Color(0xFFF0EDF5)
      )
    )

    // 2. New Tab
    NavigationBarItem(
      selected = currentScreen == AppScreen.NEW_JOBS,
      onClick = { onScreenSelected(AppScreen.NEW_JOBS) },
      icon = {
        Icon(
          if (currentScreen == AppScreen.NEW_JOBS) Icons.Filled.Article else Icons.Outlined.Article,
          contentDescription = "New"
        )
      },
      label = { Text("New", fontSize = 11.sp, fontWeight = if (currentScreen == AppScreen.NEW_JOBS) FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0xFF1E1A22),
        selectedTextColor = Color(0xFF1E1A22),
        unselectedIconColor = Color(0xFF757575),
        unselectedTextColor = Color(0xFF757575),
        indicatorColor = Color(0xFFF0EDF5)
      )
    )

    // 3. Ongoing Tab (with badge)
    NavigationBarItem(
      selected = currentScreen == AppScreen.HISTORY,
      onClick = { onScreenSelected(AppScreen.HISTORY) },
      icon = {
        BadgedBox(
          badge = {
            Badge(
              containerColor = CleankrRed,
              contentColor = Color.White
            ) {
              Text(text = "$ongoingBadgeCount", fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
          }
        ) {
          Icon(
            if (currentScreen == AppScreen.HISTORY) Icons.Filled.Schedule else Icons.Outlined.Schedule,
            contentDescription = "Ongoing"
          )
        }
      },
      label = { Text("Ongoing", fontSize = 11.sp, fontWeight = if (currentScreen == AppScreen.HISTORY) FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0xFF1E1A22),
        selectedTextColor = Color(0xFF1E1A22),
        unselectedIconColor = Color(0xFF757575),
        unselectedTextColor = Color(0xFF757575),
        indicatorColor = Color(0xFFF0EDF5)
      )
    )

    // 4. Target Tab
    NavigationBarItem(
      selected = currentScreen == AppScreen.TARGET,
      onClick = { onScreenSelected(AppScreen.TARGET) },
      icon = {
        Icon(
          if (currentScreen == AppScreen.TARGET) Icons.Filled.TrackChanges else Icons.Outlined.TrackChanges,
          contentDescription = "Target"
        )
      },
      label = { Text("Target", fontSize = 11.sp, fontWeight = if (currentScreen == AppScreen.TARGET) FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0xFF1E1A22),
        selectedTextColor = Color(0xFF1E1A22),
        unselectedIconColor = Color(0xFF757575),
        unselectedTextColor = Color(0xFF757575),
        indicatorColor = Color(0xFFF0EDF5)
      )
    )

    // 5. Money Tab
    NavigationBarItem(
      selected = currentScreen == AppScreen.EARNINGS,
      onClick = { onScreenSelected(AppScreen.EARNINGS) },
      icon = {
        Icon(
          if (currentScreen == AppScreen.EARNINGS) Icons.Filled.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet,
          contentDescription = "Money"
        )
      },
      label = { Text("Money", fontSize = 11.sp, fontWeight = if (currentScreen == AppScreen.EARNINGS) FontWeight.Bold else FontWeight.Normal) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0xFF1E1A22),
        selectedTextColor = Color(0xFF1E1A22),
        unselectedIconColor = Color(0xFF757575),
        unselectedTextColor = Color(0xFF757575),
        indicatorColor = Color(0xFFF0EDF5)
      )
    )
  }
}

/**
 * Floating Help Pill Button matching Screenshots 1, 2, 3, 5:
 * Black rounded pill button [? Help] on bottom right
 */
@Composable
fun CleankrHelpFloatingButton(
  modifier: Modifier = Modifier,
  onHelpClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(24.dp),
    color = Color(0xFF1E1E1E),
    shadowElevation = 6.dp,
    modifier = modifier
      .clickable(onClick = onHelpClick)
      .testTag("help_floating_button")
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Icon(
        imageVector = Icons.Default.HelpOutline,
        contentDescription = "Help",
        tint = Color.White,
        modifier = Modifier.size(17.dp)
      )
      Text(
        text = "Help",
        color = Color.White,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}
