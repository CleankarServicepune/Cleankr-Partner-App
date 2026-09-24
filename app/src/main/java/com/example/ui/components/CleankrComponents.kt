package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Security
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleankrTopBar(
  isOnline: Boolean,
  securityScore: Int,
  unreadCount: Int,
  onToggleDuty: () -> Unit,
  onOpenSecurity: () -> Unit,
  onOpenNotifications: () -> Unit,
  canNavigateBack: Boolean = false,
  onBackClick: () -> Unit = {}
) {
  val dutyColor by animateColorAsState(
    targetValue = if (isOnline) CleankrGreen else Color.Gray,
    label = "dutyColor"
  )

  TopAppBar(
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surface,
      titleContentColor = MaterialTheme.colorScheme.onSurface
    ),
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CleankrMonogram(sizeDp = 36.dp)
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "CLEANKR",
              fontWeight = FontWeight.Black,
              letterSpacing = 1.2.sp,
              fontSize = 17.sp,
              color = CleankrCoral
            )
            Spacer(modifier = Modifier.width(4.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = CleankrMagenta.copy(alpha = 0.2f)
            ) {
              Text(
                text = "PARTNER",
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = CleankrMagenta,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
              )
            }
          }
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(dutyColor)
            )
            Text(
              text = if (isOnline) "ONLINE • Accepting Jobs" else "OFFLINE • On Break",
              fontSize = 11.sp,
              color = dutyColor,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    },
    navigationIcon = {
      if (canNavigateBack) {
        IconButton(
          onClick = onBackClick,
          modifier = Modifier.testTag("top_bar_back_button")
        ) {
          Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
      }
    },
    actions = {
      // Online/Offline Switch
      Switch(
        checked = isOnline,
        onCheckedChange = { onToggleDuty() },
        modifier = Modifier.testTag("duty_toggle_switch"),
        colors = SwitchDefaults.colors(
          checkedThumbColor = Color.White,
          checkedTrackColor = CleankrGreen,
          uncheckedThumbColor = Color.LightGray,
          uncheckedTrackColor = Color.DarkGray
        )
      )

      // Security Shield Icon with Score Badge
      IconButton(
        onClick = onOpenSecurity,
        modifier = Modifier.testTag("security_center_button")
      ) {
        BadgedBox(
          badge = {
            Badge(
              containerColor = if (securityScore >= 90) CleankrGreen else CleankrAmber
            ) {
              Text(text = "$securityScore", fontSize = 9.sp)
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = "Security Shield",
            tint = CleankrCyan
          )
        }
      }

      // Notification Bell
      IconButton(
        onClick = onOpenNotifications,
        modifier = Modifier.testTag("notifications_button")
      ) {
        BadgedBox(
          badge = {
            if (unreadCount > 0) {
              Badge(containerColor = CleankrCoral) {
                Text(text = "$unreadCount", fontSize = 9.sp)
              }
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = MaterialTheme.colorScheme.onSurface
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
 * Bottom Navigation Bar
 */
@Composable
fun CleankrBottomNav(
  currentScreen: AppScreen,
  onScreenSelected: (AppScreen) -> Unit
) {
  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp,
    modifier = Modifier.testTag("cleankr_bottom_nav")
  ) {
    NavigationBarItem(
      selected = currentScreen == AppScreen.DASHBOARD,
      onClick = { onScreenSelected(AppScreen.DASHBOARD) },
      icon = {
        Icon(
          if (currentScreen == AppScreen.DASHBOARD) Icons.Filled.Home else Icons.Outlined.Home,
          contentDescription = "Dashboard"
        )
      },
      label = { Text("Dashboard", fontSize = 11.sp) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = CleankrCoral,
        selectedTextColor = CleankrCoral,
        indicatorColor = CleankrCoral.copy(alpha = 0.15f)
      )
    )

    NavigationBarItem(
      selected = currentScreen == AppScreen.CALENDAR,
      onClick = { onScreenSelected(AppScreen.CALENDAR) },
      icon = {
        Icon(
          if (currentScreen == AppScreen.CALENDAR) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
          contentDescription = "Calendar"
        )
      },
      label = { Text("Schedule", fontSize = 11.sp) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = CleankrMagenta,
        selectedTextColor = CleankrMagenta,
        indicatorColor = CleankrMagenta.copy(alpha = 0.15f)
      )
    )

    NavigationBarItem(
      selected = currentScreen == AppScreen.EARNINGS,
      onClick = { onScreenSelected(AppScreen.EARNINGS) },
      icon = {
        Icon(
          if (currentScreen == AppScreen.EARNINGS) Icons.Filled.Payments else Icons.Outlined.Payments,
          contentDescription = "Earnings"
        )
      },
      label = { Text("Earnings", fontSize = 11.sp) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = CleankrGreen,
        selectedTextColor = CleankrGreen,
        indicatorColor = CleankrGreen.copy(alpha = 0.15f)
      )
    )

    NavigationBarItem(
      selected = currentScreen == AppScreen.HISTORY,
      onClick = { onScreenSelected(AppScreen.HISTORY) },
      icon = {
        Icon(
          if (currentScreen == AppScreen.HISTORY) Icons.Filled.Work else Icons.Outlined.WorkOutline,
          contentDescription = "Jobs History"
        )
      },
      label = { Text("Jobs", fontSize = 11.sp) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = CleankrPeach,
        selectedTextColor = CleankrPeach,
        indicatorColor = CleankrPeach.copy(alpha = 0.15f)
      )
    )

    NavigationBarItem(
      selected = currentScreen == AppScreen.PROFILE,
      onClick = { onScreenSelected(AppScreen.PROFILE) },
      icon = {
        Icon(
          if (currentScreen == AppScreen.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
          contentDescription = "Profile"
        )
      },
      label = { Text("Profile", fontSize = 11.sp) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = CleankrViolet,
        selectedTextColor = CleankrViolet,
        indicatorColor = CleankrViolet.copy(alpha = 0.15f)
      )
    )
  }
}
