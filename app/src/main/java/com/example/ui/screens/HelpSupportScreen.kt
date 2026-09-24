package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.PartnerViewModel
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrCyan
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrRed
import com.example.ui.theme.CleankrRedContainer

data class FaqItem(val question: String, val answer: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(
  viewModel: PartnerViewModel,
  onBack: () -> Unit
) {
  val context = LocalContext.current
  var showSosDialog by remember { mutableStateOf(false) }

  val faqs = listOf(
    FaqItem(
      question = "How does Masked Calling protect my privacy?",
      answer = "When calling a customer, Cleankr routes the audio through an encrypted virtual telephony relay. Neither you nor the customer ever sees real personal mobile numbers."
    ),
    FaqItem(
      question = "How do I request additional charges for extra work?",
      answer = "Partners cannot directly edit prices. Use 'Request Service Scope Adjustment' on the Job Details screen. Our Operations Admin verifies customer consent and approves the extra payout."
    ),
    FaqItem(
      question = "What happens if a customer does not answer the door?",
      answer = "Wait 10 minutes and attempt 2 masked calls. If there is still no response, use 'Cancel Job' and select 'Customer Unavailable'. A standard transit compensation fee is credited to your wallet."
    ),
    FaqItem(
      question = "When can I withdraw my earnings?",
      answer = "Completed job earnings enter Withdrawable Balance immediately or within 2 hours. You can withdraw to your verified Bank Account or UPI ID 24/7."
    )
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Partner Support & Safety", fontWeight = FontWeight.Bold) },
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
      // 1. Emergency Safety SOS Banner
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = CleankrRedContainer),
          modifier = Modifier.fillMaxWidth().testTag("sos_emergency_card")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.Warning, contentDescription = null, tint = CleankrRed)
              Text("On-Site Safety SOS Alert", fontWeight = FontWeight.Bold, color = CleankrRed, fontSize = 15.sp)
            }
            Text(
              text = "In case of harassment, physical threat, or medical emergency during a job, trigger emergency safety response.",
              fontSize = 11.sp,
              color = Color(0xFF5D1212),
              modifier = Modifier.padding(vertical = 6.dp)
            )
            Button(
              onClick = { showSosDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = CleankrRed),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth().testTag("trigger_sos_button")
            ) {
              Text("TRIGGER EMERGENCY SOS", fontWeight = FontWeight.Black, fontSize = 12.sp)
            }
          }
        }
      }

      // 2. Direct Channels
      item {
        Text("Partner Support Helplines", fontWeight = FontWeight.Bold, fontSize = 15.sp)
      }

      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Call Partner Desk
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                  modifier = Modifier.size(38.dp).clip(CircleShape).background(CleankrGreen.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.HeadsetMic, contentDescription = null, tint = CleankrGreen, modifier = Modifier.size(20.dp))
                }
                Column {
                  Text("24/7 Partner Helpline", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                  Text("Toll-free priority queue: 1800-419-2532", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }
              Button(
                onClick = {
                  val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18004192532"))
                  context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CleankrGreen),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text("Call", fontSize = 11.sp)
              }
            }

            // Email Support
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                  modifier = Modifier.size(38.dp).clip(CircleShape).background(CleankrCyan.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Email, contentDescription = null, tint = CleankrCyan, modifier = Modifier.size(20.dp))
                }
                Column {
                  Text("Official Partner Email", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                  Text("cleankarservice@gmail.com", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }
              Button(
                onClick = {
                  val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:cleankarservice@gmail.com"))
                  context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CleankrCyan),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text("Email", fontSize = 11.sp, color = Color.Black)
              }
            }
          }
        }
      }

      // 3. FAQs Accordion
      item {
        Text("Frequently Asked Questions", fontWeight = FontWeight.Bold, fontSize = 15.sp)
      }

      items(faqs) { item ->
        var expanded by remember { mutableStateOf(false) }
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(item.question, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
              Icon(
                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = CleankrCoral
              )
            }
            AnimatedVisibility(visible = expanded) {
              Text(
                text = item.answer,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
              )
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }

  // SOS Confirmation Dialog
  if (showSosDialog) {
    AlertDialog(
      onDismissRequest = { showSosDialog = false },
      title = { Text("EMERGENCY SAFETY SOS") },
      text = {
        Text("Cleankr Emergency Dispatch will be alerted with your active job ID, timestamp, and nearest safety response center. Contacting local helpline immediately.")
      },
      confirmButton = {
        Button(
          onClick = {
            showSosDialog = false
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
            context.startActivity(intent)
          },
          colors = ButtonDefaults.buttonColors(containerColor = CleankrRed)
        ) {
          Text("Call Emergency Helpline (112)")
        }
      },
      dismissButton = {
        TextButton(onClick = { showSosDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
