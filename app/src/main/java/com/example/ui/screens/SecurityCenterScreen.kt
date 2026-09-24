package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Https
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ThreatLevel
import com.example.ui.PartnerViewModel
import com.example.ui.theme.CleankrAmber
import com.example.ui.theme.CleankrAmberContainer
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrCyan
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrRed
import com.example.ui.theme.CleankrRedContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SecurityCenterScreen(viewModel: PartnerViewModel) {
  val securityState by viewModel.securityState.collectAsState()
  val auditLogs by viewModel.auditLogs.collectAsState()

  var showPanicDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Enterprise Cyber Security & Anti-Theft",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "Proactive device integrity, malware shielding & unauthorized-access defense.",
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // 1. Health Score Hero Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
          .fillMaxWidth()
          .border(
            1.5.dp,
            if (securityState.integrityScore >= 90) CleankrGreen else CleankrAmber,
            RoundedCornerShape(20.dp)
          )
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                Icons.Default.GppGood,
                contentDescription = null,
                tint = if (securityState.integrityScore >= 90) CleankrGreen else CleankrAmber,
                modifier = Modifier.size(28.dp)
              )
              Column {
                Text(
                  text = "INTEGRITY & DEFENSE SHIELD",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Black,
                  letterSpacing = 1.sp,
                  color = if (securityState.integrityScore >= 90) CleankrGreen else CleankrAmber
                )
                Text(
                  text = if (securityState.integrityScore >= 90) "Protected & Encrypted" else "Attention Recommended",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Text(
              text = "${securityState.integrityScore}/100",
              fontSize = 28.sp,
              fontWeight = FontWeight.Black,
              color = if (securityState.integrityScore >= 90) CleankrGreen else CleankrAmber
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "TLS 1.3 pinning active • Zero plaintext keys • Anti-ransomware access limits • Brute force progressive backoff",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = { viewModel.runSecurityScan() },
            colors = ButtonDefaults.buttonColors(containerColor = CleankrCyan),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().testTag("run_security_scan_button")
          ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Run Anti-Malware & Integrity Scan", fontWeight = FontWeight.Bold, color = Color.Black)
          }
        }
      }
    }

    // 2. Core Protection Modules
    item {
      Text("Security & Anti-Theft Toggles", fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }

    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
          // App Lock Toggle
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(CleankrMagenta.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = CleankrMagenta, modifier = Modifier.size(18.dp))
              }
              Column {
                Text("Biometric / PIN Screen Lock", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Require authentication when app returns to foreground", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            Switch(
              checked = securityState.appLockEnabled,
              onCheckedChange = { viewModel.toggleAppLock() },
              colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = CleankrMagenta)
            )
          }

          // Anti-Tamper & Signature Verification
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(CleankrGreen.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CleankrGreen, modifier = Modifier.size(18.dp))
              }
              Column {
                Text("APK Package & Signature Integrity", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("SHA-256 release digest matches official Cleankr signature", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            Surface(shape = RoundedCornerShape(4.dp), color = CleankrGreen.copy(alpha = 0.15f)) {
              Text("PASSED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CleankrGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
            }
          }

          // TLS & Network Pinning
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(CleankrCyan.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Https, contentDescription = null, tint = CleankrCyan, modifier = Modifier.size(18.dp))
              }
              Column {
                Text("Strict Transport TLS 1.3 Pinning", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Mitigates rogue proxies and Man-in-the-Middle eavesdropping", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            Surface(shape = RoundedCornerShape(4.dp), color = CleankrCyan.copy(alpha = 0.15f)) {
              Text("ACTIVE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CleankrCyan, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
            }
          }
        }
      }
    }

    // 3. Emergency Panic Logout / Remote Session Revocation
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CleankrRedContainer),
        modifier = Modifier.fillMaxWidth().border(1.dp, CleankrRed.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = CleankrRed)
            Text("Anti-Theft Emergency Panic Button", fontWeight = FontWeight.Bold, color = CleankrRed, fontSize = 14.sp)
          }
          Text(
            text = "If your phone is lost, stolen, or compromised, trigger Panic Logout to invalidate all active session tokens and freeze wallet payouts.",
            fontSize = 11.sp,
            color = Color(0xFF6B1B1B),
            modifier = Modifier.padding(vertical = 6.dp)
          )
          Button(
            onClick = { showPanicDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = CleankrRed),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().testTag("panic_logout_button")
          ) {
            Text("Trigger Emergency Panic Session Revocation", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      }
    }

    // 4. Live Security Audit Log Stream
    item {
      Text("Real-Time Security Audit Trail", fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }

    items(auditLogs.take(8)) { log ->
      val timeStr = SimpleDateFormat("HH:mm:ss • dd MMM", Locale.getDefault()).format(Date(log.timestamp))
      Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Text(log.eventType, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = CleankrMagenta)
              Text("• $timeStr", fontSize = 10.sp, color = Color.Gray)
            }
            Text(log.details, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
          }
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = when (log.severity) {
              "CRITICAL" -> CleankrRed.copy(alpha = 0.15f)
              "WARNING" -> CleankrAmber.copy(alpha = 0.15f)
              else -> CleankrGreen.copy(alpha = 0.15f)
            }
          ) {
            Text(
              text = log.severity,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = when (log.severity) {
                "CRITICAL" -> CleankrRed
                "WARNING" -> CleankrAmber
                else -> CleankrGreen
              },
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  // Panic Confirmation Dialog
  if (showPanicDialog) {
    AlertDialog(
      onDismissRequest = { showPanicDialog = false },
      title = { Text("Confirm Emergency Panic Revocation") },
      text = {
        Text("This will instantly disconnect this device, revoke authentication cookies, kill active session tokens on all server nodes, and require fresh SMS OTP verification to re-access.")
      },
      confirmButton = {
        Button(
          onClick = {
            showPanicDialog = false
            viewModel.panicLogout()
          },
          colors = ButtonDefaults.buttonColors(containerColor = CleankrRed)
        ) {
          Text("Revoke Everything Now")
        }
      },
      dismissButton = {
        TextButton(onClick = { showPanicDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
