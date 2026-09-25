package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.CleankrMonogram
import com.example.ui.theme.CleankrAmber
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrCyan
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrPeach
import com.example.ui.theme.CleankrRed

@Composable
fun ProfileScreen(viewModel: PartnerViewModel) {
  val profile by viewModel.profile.collectAsState()
  val earnings by viewModel.earnings.collectAsState()

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(8.dp))
      // Profile Hero Card
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(72.dp)
              .clip(CircleShape)
              .background(CleankrMagenta.copy(alpha = 0.15f))
              .border(2.dp, CleankrMagenta, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = CleankrMagenta, modifier = Modifier.size(40.dp))
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = profile.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Partner ID: ${profile.id} • ${profile.tier}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = CleankrAmber, modifier = Modifier.size(16.dp))
            Text(
              text = "${profile.rating} Rating",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
            Text(
              text = "• ${profile.totalJobsCompleted} Jobs Completed",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // KYC & Verification Status Card
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { viewModel.navigateTo(AppScreen.KYC) }
          .testTag("profile_kyc_card")
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier.size(40.dp).clip(CircleShape).background(CleankrGreen.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = CleankrGreen, modifier = Modifier.size(22.dp))
            }
            Column {
              Text("KYC & Payout Account", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Text("Aadhaar, PAN & Bank Details Verified", fontSize = 11.sp, color = CleankrGreen)
            }
          }
          Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
        }
      }
    }

    // Navigation Menu Options
    item {
      Text("Settings & Compliance", fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }

    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column {
          ProfileMenuItem(
            icon = Icons.Default.Security,
            iconTint = CleankrCyan,
            title = "Cyber Security & Anti-Theft",
            subtitle = "Integrity scan, app lock & audit trail",
            onClick = { viewModel.navigateTo(AppScreen.SECURITY_CENTER) }
          )

          ProfileMenuItem(
            icon = Icons.Default.Settings,
            iconTint = CleankrCoral,
            title = "App Settings & Theme",
            subtitle = "Language, theme mode & notifications",
            onClick = { viewModel.navigateTo(AppScreen.SETTINGS) }
          )

          ProfileMenuItem(
            icon = Icons.Default.HelpOutline,
            iconTint = CleankrGreen,
            title = "Help & Support Desk",
            subtitle = "24/7 helpline, chat & safety SOS",
            onClick = { viewModel.navigateTo(AppScreen.HELP_SUPPORT) }
          )

          ProfileMenuItem(
            icon = Icons.Default.Policy,
            iconTint = CleankrPeach,
            title = "Privacy & Legal",
            subtitle = "Privacy policy, terms, refunds & account deletion",
            onClick = { viewModel.navigateTo(AppScreen.PRIVACY_LEGAL) }
          )

          ProfileMenuItem(
            icon = Icons.Default.Policy,
            iconTint = CleankrPeach,
            title = "Privacy Policy (Sept 3, 2026)",
            subtitle = "Data usage, telephony masking & no-sale policy",
            onClick = { viewModel.navigateTo(AppScreen.PRIVACY_POLICY) }
          )

          ProfileMenuItem(
            icon = Icons.Default.DeleteForever,
            iconTint = CleankrRed,
            title = "Account & Data Deletion",
            subtitle = "Exercise GDPR/DPDP right to be forgotten",
            onClick = { viewModel.navigateTo(AppScreen.ACCOUNT_DELETION) }
          )
        }
      }
    }

    // Logout Button
    item {
      OutlinedButton(
        onClick = { viewModel.logout() },
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CleankrRed),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("profile_logout_button")
      ) {
        Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Log Out of Partner Account", fontWeight = FontWeight.Bold)
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
fun ProfileMenuItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconTint: Color,
  title: String,
  subtitle: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 16.dp, vertical = 14.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.weight(1f)
    ) {
      Box(
        modifier = Modifier.size(36.dp).clip(CircleShape).background(iconTint.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
      }
      Column {
        Text(title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
  }
}
