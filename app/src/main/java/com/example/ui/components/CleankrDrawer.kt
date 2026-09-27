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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

/**
 * Streamlined, production-ready Cleankr Partner Navigation Drawer.
 * Retains only high-value, fully operational features and removes dead-end placeholders.
 */
@Composable
fun CleankrDrawerContent(
  viewModel: PartnerViewModel,
  onCloseDrawer: () -> Unit
) {
  val profile by viewModel.profile.collectAsState()
  val language by viewModel.language.collectAsState()
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
    // 1. Partner Profile Card Header
    Surface(
      color = MaterialTheme.colorScheme.surface,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 42.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Avatar circle with initials
        Box(
          modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(CleankrViolet.copy(alpha = 0.12f))
            .border(1.5.dp, CleankrViolet.copy(alpha = 0.3f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = profile.name.take(2).uppercase().ifEmpty { "CP" },
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = CleankrViolet
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = profile.name.ifEmpty { "Cleankr Partner" },
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = profile.phoneMasked.ifEmpty { "+91 •••• ••89" },
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = Color(0xFFFFB300),
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${profile.rating} ★",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = "View Profile",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = CleankrViolet,
              modifier = Modifier.clickable {
                viewModel.navigateTo(AppScreen.PROFILE)
                onCloseDrawer()
              }
            )
          }
        }
      }
    }

    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f), thickness = 1.dp)

    // 2. Section: Operations & Schedule
    DrawerSectionHeader(title = if (language == AppLanguage.HINDI) "दैनिक कार्य और समय" else "Daily Operations")

    DrawerMenuItem(
      icon = Icons.Default.DateRange,
      title = if (language == AppLanguage.HINDI) "कैलेंडर और उपलब्धता" else "Calendar & Availability",
      subtitle = if (language == AppLanguage.HINDI) "टाइम स्लॉट और लीव मैनेज करें" else "Manage slots & working hours",
      badge = "Active",
      isHighlighted = true,
      onClick = {
        viewModel.navigateTo(AppScreen.CALENDAR)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.Work,
      title = if (language == AppLanguage.HINDI) "जॉब हिस्ट्री" else "Job History",
      subtitle = if (language == AppLanguage.HINDI) "पूर्ण की गई बुकिंग्स" else "Completed jobs & service proof",
      onClick = {
        viewModel.navigateTo(AppScreen.HISTORY)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.AccountBalanceWallet,
      title = if (language == AppLanguage.HINDI) "कमाई और बैंक विवरण" else "Earnings & Payouts",
      subtitle = if (language == AppLanguage.HINDI) "वॉलेट, विथड्रॉल और बैंक अकाउंट" else "Wallet, payouts & bank account",
      onClick = {
        viewModel.navigateTo(AppScreen.EARNINGS)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.Storefront,
      title = if (language == AppLanguage.HINDI) "मेरा ऑपरेशंस हब" else "My Hub & Equipment Depot",
      subtitle = if (language == AppLanguage.HINDI) "हब पता, उपकरण और मैनेजर" else "Hub address, equipment & manager",
      badge = "Assigned",
      onClick = {
        viewModel.navigateTo(AppScreen.MY_HUB)
        onCloseDrawer()
      }
    )

    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

    // 3. Section: Verification & Safety
    DrawerSectionHeader(title = if (language == AppLanguage.HINDI) "सत्यापन और सुरक्षा" else "Verification & Safety")

    DrawerMenuItem(
      icon = Icons.Default.VerifiedUser,
      title = if (language == AppLanguage.HINDI) "KYC और स्किल इंडिया (NSDC)" else "KYC & Skill India (NSDC)",
      subtitle = if (language == AppLanguage.HINDI) "आधार, पैन और स्किल सर्टिफिकेट" else "Aadhaar, PAN & NSDC certificate",
      onClick = {
        viewModel.navigateTo(AppScreen.KYC)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.Security,
      title = if (language == AppLanguage.HINDI) "सुरक्षा केंद्र (SOS)" else "Security Center & SOS",
      subtitle = if (language == AppLanguage.HINDI) "मास्क्ड कॉलिंग और इमरजेंसी हेल्प" else "Masked calling & safety protocols",
      onClick = {
        viewModel.navigateTo(AppScreen.SECURITY_CENTER)
        onCloseDrawer()
      }
    )

    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

    // 4. Section: Support & Preferences
    DrawerSectionHeader(title = if (language == AppLanguage.HINDI) "सपोर्ट और सेटिंग्स" else "Support & Settings")

    DrawerMenuItem(
      icon = Icons.Default.HelpCenter,
      title = if (language == AppLanguage.HINDI) "पार्टनर हेल्प और सपोर्ट" else "Partner Help & Support",
      subtitle = if (language == AppLanguage.HINDI) "कस्टमर गैर-हाजिरी और इमरजेंसी सहायता" else "Customer unavailable & hotline",
      onClick = {
        viewModel.navigateTo(AppScreen.HELP_SUPPORT)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.Settings,
      title = if (language == AppLanguage.HINDI) "ऐप सेटिंग्स और नोटिफिकेशन्स" else "Settings & Notifications",
      subtitle = if (language == AppLanguage.HINDI) "पुश अलर्ट्स और सिस्टम परमिशन" else "Push alerts & sound settings",
      onClick = {
        viewModel.navigateTo(AppScreen.SETTINGS)
        onCloseDrawer()
      }
    )

    DrawerMenuItem(
      icon = Icons.Default.Policy,
      title = if (language == AppLanguage.HINDI) "प्राइवेसी और कानूनी शर्तें" else "Privacy & Legal Policies",
      subtitle = if (language == AppLanguage.HINDI) "नियम और अकाउंट डिलीशन" else "Terms, privacy & account deletion",
      onClick = {
        viewModel.navigateTo(AppScreen.PRIVACY_LEGAL)
        onCloseDrawer()
      }
    )

    // Language Toggle
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
          tint = CleankrViolet,
          modifier = Modifier.size(22.dp)
        )
        Text(
          text = if (language == AppLanguage.HINDI) "भाषा बदलें" else "Change Language",
          fontSize = 14.sp,
          fontWeight = FontWeight.Medium,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
      Surface(
        shape = RoundedCornerShape(6.dp),
        color = CleankrViolet.copy(alpha = 0.12f),
        modifier = Modifier.padding(end = 4.dp)
      ) {
        Text(
          text = if (language == AppLanguage.ENGLISH) "English" else "हिंदी",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = CleankrViolet,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
      }
    }

    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

    // Logout Item
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { showLogoutConfirm = true }
        .padding(horizontal = 20.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Logout,
        contentDescription = "Logout",
        tint = MaterialTheme.colorScheme.error,
        modifier = Modifier.size(22.dp)
      )
      Text(
        text = if (language == AppLanguage.HINDI) "लॉगआउट करें" else "Secure Logout",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.error
      )
    }

    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = "Cleankr Partner App v7.2.21",
      fontSize = 11.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
      modifier = Modifier.padding(start = 20.dp, bottom = 24.dp)
    )
  }

  // Logout Dialog
  if (showLogoutConfirm) {
    AlertDialog(
      onDismissRequest = { showLogoutConfirm = false },
      title = { Text(if (language == AppLanguage.HINDI) "लॉगआउट की पुष्टि करें" else "Logout from Cleankr Partner?") },
      text = {
        Text(
          if (language == AppLanguage.HINDI)
            "क्या आप वाकई लॉगआउट करना चाहते हैं? दोबारा लॉगिन करने के लिए आपके रजिस्टर्ड फोन और OTP की आवश्यकता होगी।"
          else
            "Are you sure you want to log out? You will need your registered mobile number and OTP to log back in."
        )
      },
      confirmButton = {
        TextButton(
          onClick = {
            showLogoutConfirm = false
            onCloseDrawer()
            viewModel.logout()
          }
        ) {
          Text(if (language == AppLanguage.HINDI) "लॉगआउट" else "Logout", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showLogoutConfirm = false }) {
          Text(if (language == AppLanguage.HINDI) "रद्द करें" else "Cancel")
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
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
private fun DrawerSectionHeader(title: String) {
  Text(
    text = title.uppercase(),
    fontSize = 11.sp,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
    letterSpacing = 0.5.sp,
    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 6.dp)
  )
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
      .padding(horizontal = 20.dp, vertical = 11.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp),
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
            fontSize = 13.5.sp,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlighted) CleankrViolet else MaterialTheme.colorScheme.onSurface
          )
          badge?.let {
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = CleankrGreen.copy(alpha = 0.15f)
            ) {
              Text(
                text = it,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                color = CleankrGreen,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
              )
            }
          }
        }
        subtitle?.let {
          Text(
            text = it,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
          )
        }
      }
    }
    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
      modifier = Modifier.size(11.dp)
    )
  }
}
