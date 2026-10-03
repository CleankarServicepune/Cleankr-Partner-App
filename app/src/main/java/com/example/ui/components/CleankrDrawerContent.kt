package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen

@Composable
fun CleankrDrawerContent(
  activeScreen: AppScreen,
  currentLanguage: String,
  onNavigate: (AppScreen) -> Unit,
  onLanguageClick: () -> Unit,
  onLogoutClick: () -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxHeight()
      .width(320.dp),
    color = Color.White
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxHeight()
        .padding(horizontal = 18.dp),
      contentPadding = PaddingValues(top = 44.dp, bottom = 28.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Profile Header
      item {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate(AppScreen.PROFILE) }
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(Color(0xFFEDE7F6)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "SU",
              fontWeight = FontWeight.Black,
              fontSize = 18.sp,
              color = Color(0xFF4A148C)
            )
          }

          Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
              text = "Sunil Sharma",
              fontWeight = FontWeight.Black,
              fontSize = 17.sp,
              color = Color(0xFF1E1E1E)
            )
            Text(
              text = "+91 98*** **412",
              fontSize = 13.sp,
              color = Color(0xFF616161)
            )
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Text(
                text = "★ 4.92 ★",
                color = Color(0xFFF57F17),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "View Profile",
                color = Color(0xFF5A31F4),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))
        HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
      }

      // 2. DAILY OPERATIONS
      item {
        SectionTitle(title = "DAILY OPERATIONS")

        DrawerItem(
          icon = Icons.Default.CalendarMonth,
          title = "Calendar & Availability",
          subtitle = "Manage slots & working hours",
          badgeText = "Active",
          badgeColor = Color(0xFFE8F5E9),
          badgeTextColor = Color(0xFF1B5E20),
          onClick = { onNavigate(AppScreen.CALENDAR) }
        )

        DrawerItem(
          icon = Icons.Default.Work,
          title = "Job History",
          subtitle = "Completed jobs & service proof",
          onClick = { onNavigate(AppScreen.JOB_HISTORY) }
        )

        DrawerItem(
          icon = Icons.Default.AccountBalanceWallet,
          title = "Earnings & Payouts",
          subtitle = "Wallet, payouts & bank account",
          onClick = { onNavigate(AppScreen.MONEY) }
        )

        DrawerItem(
          icon = Icons.Default.Business,
          title = "My Hub & Equipment Depot",
          subtitle = "Hub address, equipment & manager",
          badgeText = "Assigned",
          badgeColor = Color(0xFFE8F5E9),
          badgeTextColor = Color(0xFF1B5E20),
          onClick = { onNavigate(AppScreen.MY_HUB) }
        )
      }

      // 3. VERIFICATION & SAFETY
      item {
        SectionTitle(title = "VERIFICATION & SAFETY")

        DrawerItem(
          icon = Icons.Default.VerifiedUser,
          title = "KYC & Skill India (NSDC)",
          subtitle = "Aadhaar, PAN & NSDC certificate",
          onClick = { onNavigate(AppScreen.KYC_SKILL_INDIA) }
        )

        DrawerItem(
          icon = Icons.Default.Security,
          title = "Security Center & SOS",
          subtitle = "Masked calling & safety protocols",
          onClick = { onNavigate(AppScreen.SECURITY_CENTER) }
        )
      }

      // 4. SUPPORT & SETTINGS
      item {
        SectionTitle(title = "SUPPORT & SETTINGS")

        DrawerItem(
          icon = Icons.AutoMirrored.Filled.HelpOutline,
          title = "Partner Help & Support",
          subtitle = "Customer unavailable & hotline",
          onClick = { onNavigate(AppScreen.HELP_SUPPORT) }
        )

        DrawerItem(
          icon = Icons.Default.Settings,
          title = "Settings & Notifications",
          subtitle = "Push alerts & sound settings",
          onClick = { onNavigate(AppScreen.SETTINGS_NOTIFICATIONS) }
        )

        DrawerItem(
          icon = Icons.Default.Security,
          title = "Privacy & Legal Policies",
          subtitle = "Terms, privacy & account deletion",
          onClick = { onNavigate(AppScreen.PRIVACY_LEGAL) }
        )

        // Change Language
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onLanguageClick() }
            .padding(vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Language,
              contentDescription = null,
              tint = Color(0xFF5A31F4),
              modifier = Modifier.size(24.dp)
            )
            Text(
              text = "Change Language",
              fontWeight = FontWeight.Black,
              fontStyle = FontStyle.Italic,
              fontSize = 15.sp,
              color = Color(0xFF1E1E1E)
            )
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFEDE7F6),
            modifier = Modifier.padding(end = 4.dp)
          ) {
            Text(
              text = currentLanguage,
              color = Color(0xFF4A148C),
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }

        // Secure Logout
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onLogoutClick() }
            .padding(vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = "Logout",
            tint = Color(0xFFD32F2F),
            modifier = Modifier.size(24.dp)
          )
          Text(
            text = "Secure Logout",
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            fontSize = 15.sp,
            color = Color(0xFFD32F2F)
          )
        }
      }

      // App Version
      item {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "Cleankr Partner App v7.2.21",
          fontSize = 13.sp,
          fontStyle = FontStyle.Italic,
          color = Color(0xFF9E9E9E),
          modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )
      }
    }
  }
}

@Composable
private fun SectionTitle(title: String) {
  Text(
    text = title,
    fontWeight = FontWeight.Black,
    fontSize = 11.sp,
    color = Color(0xFF212121),
    letterSpacing = 0.8.sp,
    modifier = Modifier.padding(vertical = 6.dp)
  )
}

@Composable
private fun DrawerItem(
  icon: ImageVector,
  title: String,
  subtitle: String,
  badgeText: String? = null,
  badgeColor: Color = Color(0xFFE8F5E9),
  badgeTextColor: Color = Color(0xFF1B5E20),
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      modifier = Modifier.weight(1f),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = Color(0xFF424242),
        modifier = Modifier.size(22.dp)
      )

      Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = title,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            fontSize = 14.sp,
            color = Color(0xFF1E1E1E)
          )

          if (badgeText != null) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = badgeColor
            ) {
              Text(
                text = badgeText,
                color = badgeTextColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }

        Text(
          text = subtitle,
          fontSize = 12.sp,
          fontStyle = FontStyle.Italic,
          color = Color(0xFF616161)
        )
      }
    }

    Icon(
      imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
      contentDescription = null,
      tint = Color(0xFFBDBDBD),
      modifier = Modifier.size(18.dp)
    )
  }
}
