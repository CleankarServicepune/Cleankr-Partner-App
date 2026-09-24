package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppLanguage
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrViolet

@Composable
fun CleankrDrawerContent(
  viewModel: PartnerViewModel,
  onCloseDrawer: () -> Unit
) {
  val profile by viewModel.profile.collectAsState()
  val language by viewModel.language.collectAsState()
  var whatsappUpdatesEnabled by remember { mutableStateOf(true) }
  var showLogoutConfirm by remember { mutableStateOf(false) }
  var showLanguageDialog by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxHeight()
      .width(320.dp)
      .background(MaterialTheme.colorScheme.surface)
      .verticalScroll(rememberScrollState())
      .testTag("cleankr_drawer_content")
  ) {
    // 1. Partner Profile Header (Matching Screenshot 4 & 6)
    Surface(
      color = MaterialTheme.colorScheme.surface,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Avatar circle
        Box(
          modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(CleankrViolet.copy(alpha = 0.15f))
            .border(1.5.dp, CleankrViolet.copy(alpha = 0.3f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = profile.name.take(2).uppercase(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = CleankrViolet
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = profile.name.ifEmpty { "Madhav Karegave" },
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = profile.phoneMasked,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = Color(0xFFFFB300),
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "${profile.rating}",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Change photo",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = CleankrViolet,
            modifier = Modifier.clickable {
              viewModel.navigateTo(AppScreen.PROFILE)
              onCloseDrawer()
            }
          )
        }
      }
    }

    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), thickness = 1.dp)

    // 2. Primary Navigation Options
    DrawerMenuItem(
      icon = Icons.Default.MonetizationOn,
      title = "Pension",
      badge = "New!",
      onClick = {
        viewModel.navigateTo(AppScreen.EARNINGS)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.Person,
      title = "Become Cleankr Master",
      onClick = {
        viewModel.navigateTo(AppScreen.HELP_SUPPORT)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.Chat,
      title = "Onboarding Journey",
      onClick = {
        viewModel.navigateTo(AppScreen.KYC)
        onCloseDrawer()
      }
    )

    // CALENDAR ITEM (PROMINENT!)
    DrawerMenuItem(
      icon = Icons.Default.DateRange,
      title = "Calendar",
      isHighlighted = true,
      onClick = {
        viewModel.navigateTo(AppScreen.CALENDAR)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.Work,
      title = "Job history",
      onClick = {
        viewModel.navigateTo(AppScreen.HISTORY)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.MenuBook,
      title = "My Hub",
      onClick = {
        viewModel.navigateTo(AppScreen.DASHBOARD)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.Share,
      title = "Share my details",
      onClick = {
        viewModel.navigateTo(AppScreen.PROFILE)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.TrackChanges,
      title = "Credits",
      onClick = {
        viewModel.navigateTo(AppScreen.TARGET)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.CreditCard,
      title = "Loans",
      onClick = {
        viewModel.navigateTo(AppScreen.EARNINGS)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.Security,
      title = "Insurance",
      onClick = {
        viewModel.navigateTo(AppScreen.SECURITY_CENTER)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.School,
      title = "Training",
      onClick = {
        viewModel.navigateTo(AppScreen.HELP_SUPPORT)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.HelpCenter,
      title = "Help Center",
      onClick = {
        viewModel.navigateTo(AppScreen.HELP_SUPPORT)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.CardGiftcard,
      title = "Invite a friend to Cleankr",
      onClick = {
        viewModel.navigateTo(AppScreen.PROFILE)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.ShoppingBag,
      title = "Cleankr shop",
      onClick = {
        viewModel.navigateTo(AppScreen.HELP_SUPPORT)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.VerifiedUser,
      title = "Skill India Certificate",
      onClick = {
        viewModel.navigateTo(AppScreen.KYC)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.Person,
      title = "Complete UAN",
      onClick = {
        viewModel.navigateTo(AppScreen.KYC)
        onCloseDrawer()
      }
    )

    Divider(
      color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
      thickness = 1.dp,
      modifier = Modifier.padding(vertical = 8.dp)
    )

    // Financial details
    DrawerMenuItem(
      icon = Icons.Default.AccountBalance,
      title = "Financial details",
      subtitle = "GST, PAN and Bank information",
      onClick = {
        viewModel.navigateTo(AppScreen.EARNINGS)
        onCloseDrawer()
      }
    )

    // WhatsApp updates toggle
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Icon(
          Icons.Default.Chat,
          contentDescription = null,
          tint = CleankrGreen,
          modifier = Modifier.size(22.dp)
        )
        Text(
          text = "Send WhatsApp updates",
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
      Switch(
        checked = whatsappUpdatesEnabled,
        onCheckedChange = { whatsappUpdatesEnabled = it },
        colors = SwitchDefaults.colors(
          checkedThumbColor = Color.White,
          checkedTrackColor = CleankrGreen
        )
      )
    }

    // Change language
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { showLanguageDialog = true }
        .padding(horizontal = 20.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Icon(
          Icons.Default.Language,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(22.dp)
        )
        Text(
          text = "Change language",
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
      Text(
        text = if (language == AppLanguage.ENGLISH) "English" else "हिंदी",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = CleankrViolet
      )
    }

    Divider(
      color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
      thickness = 1.dp,
      modifier = Modifier.padding(vertical = 8.dp)
    )

    // Legal and General
    DrawerSimpleItem("Contact us") {
      viewModel.navigateTo(AppScreen.HELP_SUPPORT)
      onCloseDrawer()
    }
    DrawerSimpleItem("Terms of use") {
      viewModel.navigateTo(AppScreen.PRIVACY_POLICY)
      onCloseDrawer()
    }
    DrawerSimpleItem("Privacy policy") {
      viewModel.navigateTo(AppScreen.PRIVACY_POLICY)
      onCloseDrawer()
    }
    DrawerSimpleItem("Welfare policy") {
      viewModel.navigateTo(AppScreen.HELP_SUPPORT)
      onCloseDrawer()
    }
    DrawerSimpleItem("Rate us on the Play Store") {
      viewModel.clearFeedback()
      onCloseDrawer()
    }
    DrawerSimpleItem("Download Customer app") {
      onCloseDrawer()
    }
    DrawerSimpleItem("Logout", isDestructive = true) {
      showLogoutConfirm = true
    }

    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = "App version v7.2.21 341",
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
      modifier = Modifier.padding(start = 20.dp, bottom = 24.dp)
    )
  }

  // Logout Dialog
  if (showLogoutConfirm) {
    AlertDialog(
      onDismissRequest = { showLogoutConfirm = false },
      title = { Text("Logout from Cleankr Partner?") },
      text = { Text("Are you sure you want to log out? You will need your registered phone number and OTP to log back in.") },
      confirmButton = {
        TextButton(
          onClick = {
            showLogoutConfirm = false
            onCloseDrawer()
            viewModel.logout()
          }
        ) {
          Text("Logout", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showLogoutConfirm = false }) {
          Text("Cancel")
        }
      }
    )
  }

  // Language Dialog
  if (showLanguageDialog) {
    AlertDialog(
      onDismissRequest = { showLanguageDialog = false },
      title = { Text("Choose Language / भाषा चुनें") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (language == AppLanguage.ENGLISH) CleankrViolet.copy(alpha = 0.15f) else Color.Transparent,
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                viewModel.setLanguage(AppLanguage.ENGLISH)
                showLanguageDialog = false
              }
              .padding(12.dp)
          ) {
            Text("English (Default)", fontWeight = FontWeight.Bold)
          }
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (language == AppLanguage.HINDI) CleankrViolet.copy(alpha = 0.15f) else Color.Transparent,
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                viewModel.setLanguage(AppLanguage.HINDI)
                showLanguageDialog = false
              }
              .padding(12.dp)
          ) {
            Text("हिंदी (Hindi)", fontWeight = FontWeight.Bold)
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showLanguageDialog = false }) {
          Text("Done")
        }
      }
    )
  }
}

@Composable
private fun DrawerMenuItem(
  icon: ImageVector,
  title: String,
  subtitle: String? = null,
  badge: String? = null,
  isHighlighted: Boolean = false,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .background(if (isHighlighted) CleankrViolet.copy(alpha = 0.08f) else Color.Transparent)
      .padding(horizontal = 20.dp, vertical = 13.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier.weight(1f)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (isHighlighted) CleankrViolet else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(22.dp)
      )
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlighted) CleankrViolet else MaterialTheme.colorScheme.onSurface
          )
          badge?.let {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = Color(0xFFEDE7F6)
            ) {
              Text(
                text = it,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = CleankrViolet,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
              )
            }
          }
        }
        subtitle?.let {
          Text(
            text = it,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
      modifier = Modifier.size(13.dp)
    )
  }
}

@Composable
private fun DrawerSimpleItem(
  title: String,
  isDestructive: Boolean = false,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(horizontal = 20.dp, vertical = 11.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = title,
      fontSize = 13.sp,
      color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
      fontWeight = if (isDestructive) FontWeight.Bold else FontWeight.Normal
    )
    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
      modifier = Modifier.size(11.dp)
    )
  }
}
