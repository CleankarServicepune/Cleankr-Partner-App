package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.legal.CleankrLegalConfig
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrCyan
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrRed
import com.example.ui.theme.CleankrRedContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyLegalScreen(
  viewModel: PartnerViewModel,
  onBack: () -> Unit
) {
  val context = LocalContext.current
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabTitles = listOf("Privacy Policy", "Terms & Conditions", "Refund Policy", "Account Deletion")

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Privacy & Legal", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(onClick = {
            CleankrLegalConfig.sendEmailInquiry(context, "Legal Inquiry - Cleankr Partner App")
          }) {
            Icon(Icons.Default.Email, contentDescription = "Email Legal Desk", tint = CleankrGreen)
          }
        }
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // Tab selector for the 4 required sections
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = CleankrMagenta
      ) {
        tabTitles.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1
              )
            }
          )
        }
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        when (selectedTab) {
          0 -> {
            // 1. PRIVACY POLICY
            item {
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(16.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text("Cleankr Published Privacy Policy", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CleankrCoral)
                      Text("Official Grievance Desk: cleankarservice@gmail.com", fontSize = 12.sp, color = CleankrGreen, fontWeight = FontWeight.SemiBold)
                    }
                  }

                  Spacer(modifier = Modifier.height(10.dp))
                  Text(
                    text = "We strictly adhere to fair data governance and privacy mandates. Personal customer and partner contact numbers are completely masked via automated telephony proxies. Personal data is never monetized or sold.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                  )

                  Spacer(modifier = Modifier.height(12.dp))

                  // Open Published Policy using Custom Tabs
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
                    modifier = Modifier.fillMaxWidth().testTag("open_published_policy_button")
                  ) {
                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open Published Cleankr Web Policy")
                  }
                }
              }
            }

            item {
              LegalSectionCard(
                title = "Data Collected for Service Fulfillment",
                points = listOf(
                  "Partner Mobile Number & Verified Identity: To maintain authorized partner role and job dispatch.",
                  "Assigned Booking Information: Location address and job scope strictly needed to perform authorized services.",
                  "Payout & Bank Credentials: Stored in encrypted, PCI-compliant vault for weekly and instant withdrawals.",
                  "Zero Continuous Tracking: Location is queried exclusively on-demand when turn-by-turn navigation is opened for an active job."
                )
              )
            }

            item {
              LegalSectionCard(
                title = "Masked Communications",
                points = listOf(
                  "Automated VoIP/Telephony Relay: Real phone numbers of customers and partners remain private.",
                  "In-app Chat & Support Records: Monitored strictly for partner safety, quality assurance, and dispute resolution."
                )
              )
            }
          }

          1 -> {
            // 2. TERMS & CONDITIONS
            item {
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = CleankrCyan)
                    Text("Partner Agreement & Terms of Service", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                  }
                  Text(
                    "By using the Cleankr Partner App, you agree to uphold our professional standards, code of conduct, and safety guidelines across Mumbai & Pune operations.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }

            item {
              LegalSectionCard(
                title = "Partner Code of Conduct",
                points = listOf(
                  "Punctuality & Attire: Wear clean uniform and Cleankr ID badge at customer premises.",
                  "No Offline Direct Deals: Performing cash transactions directly with customers outside the platform is strictly prohibited and results in immediate permanent blacklisting.",
                  "Respect & Safety: Zero tolerance for harassment, aggressive conduct, or unauthorized personnel at client sites.",
                  "Equipment Standards: Utilize commercial-grade, verified non-hazardous cleaning chemicals and industrial extractors."
                )
              )
            }

            item {
              LegalSectionCard(
                title = "Service Change Authorization",
                points = listOf(
                  "Backend Authoritative Pricing: Partners cannot unilaterally change official company prices.",
                  "Scope Modifications: Any additional work or change requested by customer must be submitted through the in-app Service Change proposal tool for Admin review.",
                  "70% Revenue Share: Upon Admin approval, partner earnings are automatically credited according to the company payout tier."
                )
              )
            }
          }

          2 -> {
            // 3. REFUND & CANCELLATION POLICY
            item {
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Cancel, contentDescription = null, tint = CleankrCoral)
                    Text("Partner Refund & Cancellation Guidelines", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                  }
                  Text(
                    "Fair settlement rules governing customer cancellations, partner unavailabilities, and compensation for travel time.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }

            item {
              LegalSectionCard(
                title = "Customer Cancellations & Partner Compensation",
                points = listOf(
                  "Arrived on Site: If customer cancels after partner status reaches 'ARRIVED', partner receives ₹350 inconvenience & travel reimbursement.",
                  "More than 2 hours before slot: Full customer refund; job slot released back to partner for new dispatches.",
                  "Less than 2 hours before slot: Partial cancellation fee disbursed to partner."
                )
              )
            }

            item {
              LegalSectionCard(
                title = "Partner Emergency Cancellations",
                points = listOf(
                  "Genuine Emergencies: Must be reported immediately to Partner Support desk with valid reason.",
                  "Frequent Late Cancellations: Unexcused cancellations within 30 minutes of scheduled start time negatively impact partner tier rating.",
                  "Reschedule Facility: Jobs can be rescheduled only with customer consent and operations desk confirmation."
                )
              )
            }
          }

          3 -> {
            // 4. ACCOUNT DELETION
            item {
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CleankrRedContainer),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.DeleteForever, contentDescription = null, tint = CleankrRed)
                    Text("Backend-Authorized Account Deletion", fontWeight = FontWeight.Bold, color = CleankrRed, fontSize = 16.sp)
                  }
                  Text(
                    "You have the right to request deletion of your account and personal identifiers under Indian Digital Personal Data Protection (DPDP) and international privacy frameworks.",
                    fontSize = 12.sp,
                    color = Color(0xFF631515)
                  )
                }
              }
            }

            item {
              LegalSectionCard(
                title = "Statutory Record Retention Notice",
                points = listOf(
                  "Personal Identifiers Purged: Profile photos, active device sessions, and GPS history are permanently erased.",
                  "Legally Required Financial Records: Indian taxation, GST compliance, banking dispute, and anti-money laundering statutes require transactional ledgers and audit records to be retained securely for statutory audit periods.",
                  "Settlement of Dues: Outstanding withdrawable balances are settled to your linked bank account prior to final decommission."
                )
              )
            }

            item {
              Button(
                onClick = { viewModel.navigateTo(AppScreen.ACCOUNT_DELETION) },
                colors = ButtonDefaults.buttonColors(containerColor = CleankrRed),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().testTag("launch_account_deletion_flow")
              ) {
                Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Proceed to Account Deletion Request")
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}

@Composable
private fun LegalSectionCard(title: String, points: List<String>) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
      Spacer(modifier = Modifier.height(8.dp))
      points.forEach { pt ->
        Row(
          modifier = Modifier.padding(vertical = 3.dp),
          verticalAlignment = Alignment.Top,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text("•", fontWeight = FontWeight.Bold, color = CleankrCoral)
          Text(pt, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 17.sp)
        }
      }
    }
  }
}
