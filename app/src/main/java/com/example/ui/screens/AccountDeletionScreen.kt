package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.PartnerViewModel
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrRed
import com.example.ui.theme.CleankrRedContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDeletionScreen(
  viewModel: PartnerViewModel,
  onBack: () -> Unit
) {
  var confirmationInput by remember { mutableStateOf("") }
  var deletionReason by remember { mutableStateOf("Relocating to another city") }
  var generatedTicket by remember { mutableStateOf<String?>(null) }

  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Account & Data Deletion", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
          }
        }
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp)
        .verticalScroll(scrollState),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CleankrRedContainer),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = CleankrRed)
            Text("Permanent Account Deletion Request", fontWeight = FontWeight.Bold, color = CleankrRed, fontSize = 15.sp)
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Submitting this request will deactivate your Cleankr Partner account immediately. A 30-day grace period applies during which you may revoke this request by contacting cleankarservice@gmail.com.",
            fontSize = 12.sp,
            color = Color(0xFF631515)
          )
        }
      }

      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Data Handling upon Deletion", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Text("• Profile photos, phone numbers, and identity documents are purged.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("• Device session tokens and location cache are wiped immediately.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("• Unclaimed withdrawable earnings will be disbursed to your registered bank account before final purge.", fontSize = 12.sp, color = CleankrGreen, fontWeight = FontWeight.SemiBold)
          Text("• In accordance with Indian accounting and tax statutes, financial transaction logs are retained securely in cold storage for statutory audit periods.", fontSize = 11.sp, color = Color.Gray)
        }
      }

      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Confirm Request", fontWeight = FontWeight.Bold, fontSize = 14.sp)

          OutlinedTextField(
            value = deletionReason,
            onValueChange = { deletionReason = it },
            label = { Text("Reason for Departure") },
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = confirmationInput,
            onValueChange = { confirmationInput = it },
            label = { Text("Type 'DELETE' to confirm") },
            placeholder = { Text("DELETE") },
            modifier = Modifier.fillMaxWidth().testTag("deletion_confirmation_input")
          )

          Spacer(modifier = Modifier.height(4.dp))

          Button(
            onClick = {
              val ticket = viewModel.requestAccountDeletion()
              generatedTicket = ticket
            },
            enabled = confirmationInput.trim().equals("DELETE", ignoreCase = true),
            colors = ButtonDefaults.buttonColors(containerColor = CleankrRed),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_deletion_button")
          ) {
            Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Permanently Request Account Deletion", fontWeight = FontWeight.Bold)
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  if (generatedTicket != null) {
    AlertDialog(
      onDismissRequest = {
        generatedTicket = null
        viewModel.logout()
      },
      title = { Text("Deletion Ticket Generated") },
      text = {
        Text("Your request ticket #$generatedTicket has been logged. 30-day grace period has begun. All active partner sessions will now be terminated.")
      },
      confirmButton = {
        Button(
          onClick = {
            generatedTicket = null
            viewModel.logout()
          },
          colors = ButtonDefaults.buttonColors(containerColor = CleankrCoral)
        ) {
          Text("Acknowledge & Exit")
        }
      }
    )
  }
}
