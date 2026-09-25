package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.legal.CleankrLegalConfig
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
  val context = LocalContext.current
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Privacy Policy", fontWeight = FontWeight.Bold) },
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
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text("CleanKar Privacy Policy", fontWeight = FontWeight.Black, fontSize = 18.sp, color = CleankrCoral)
            Text("Last Updated: September 3, 2026", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Official Contact: cleankarservice@gmail.com", fontSize = 12.sp, color = CleankrGreen, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = CleankrGreen.copy(alpha = 0.12f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "🛡️ Core Commitment: Personal information must not be sold under any circumstances. Real contact numbers are strictly masked.",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = CleankrGreen,
                modifier = Modifier.padding(10.dp)
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = {
                val opened = CleankrLegalConfig.openPublishedPrivacyPolicy(context)
                if (!opened) {
                  Toast.makeText(
                    context,
                    "Viewing in-app policy. Published URL constant ready in CleankrLegalConfig.",
                    Toast.LENGTH_LONG
                  ).show()
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = CleankrCoral),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Open Published Cleankr Web Policy")
            }
          }
        }
      }

      item {
        PolicySection(
          title = "1. Account & Partner Registration",
          content = "We collect your verified mobile number, full name, residential address, profile photograph, and operational service categories to maintain your authorized partner account and enable job dispatching."
        )
      }

      item {
        PolicySection(
          title = "2. Partner KYC & Payout Details",
          content = "For legal regulatory compliance, fraud prevention, and automated earnings payouts, we collect and securely verify government identity documents (Aadhaar, PAN card, Police Clearance Certificate) and linked bank account / UPI VPA details. This information is encrypted using AES-256 and accessible only to authorized compliance personnel."
        )
      }

      item {
        PolicySection(
          title = "3. Address & Location Data (Minimum On-Demand)",
          content = "We practice minimum location tracking. Location permissions are utilized strictly and exclusively on-demand when turn-by-turn navigation is launched for an accepted job. There is NO hidden tracking, NO continuous background GPS, and location data is NEVER shared with third parties or used for advertising."
        )
      }

      item {
        PolicySection(
          title = "4. Masked Telephony Relay & Communications",
          content = "Customer and partner phone numbers are safeguarded by our automated telephony relay proxy. Neither customer nor partner ever sees each other's real phone number before, during, or after bookings. All masked call sessions require secure backend authorization."
        )
      }

      item {
        PolicySection(
          title = "5. Payment Processing & Direct Payouts",
          content = "Payment and withdrawal transactions are routed through certified, PCI-DSS compliant banking channels. No credit/debit card numbers or confidential banking credentials are ever stored in the local APK."
        )
      }

      item {
        PolicySection(
          title = "6. Limited Necessary Sharing",
          content = "We share data only where strictly necessary: (a) with customers (partner first name, ratings, masked contact extension), (b) with payment gateways for settlement, and (c) with law enforcement only when strictly required by a court order."
        )
      }

      item {
        PolicySection(
          title = "7. Security & Retention",
          content = "We implement enterprise cyber security measures, TLS 1.3 certificate pinning, brute-force rate-limiting, and encrypted sessions. Data is retained only as long as necessary for active partnership or as legally mandated for tax and accounting records."
        )
      }

      item {
        PolicySection(
          title = "8. Account & Data Deletion Rights",
          content = "Partners possess the unequivocal right to request complete account and personal data deletion under applicable data protection laws. Submit requests via the in-app Deletion tool or email cleankarservice@gmail.com. We observe a 30-day grace period, retaining only tax-mandated transactional receipts."
        )
      }

      item {
        PolicySection(
          title = "9. Policy Changes & Inquiries",
          content = "We may update this Privacy Policy periodically. Continued use of the partner app after updates constitutes acceptance. For inquiries, email: cleankarservice@gmail.com."
        )
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

@Composable
fun PolicySection(title: String, content: String) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
      Spacer(modifier = Modifier.height(4.dp))
      Text(content, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
    }
  }
}
