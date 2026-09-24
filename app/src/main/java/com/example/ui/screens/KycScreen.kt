package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.KycStatus
import com.example.ui.PartnerViewModel
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrCyan
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KycScreen(
  viewModel: PartnerViewModel,
  onBack: () -> Unit
) {
  val profile by viewModel.profile.collectAsState()

  var aadhaarInput by remember { mutableStateOf("4812") }
  var panInput by remember { mutableStateOf("8912F") }
  var bankAccountInput by remember { mutableStateOf("4921") }
  var ifscInput by remember { mutableStateOf("HDFC0001243") }
  var upiInput by remember { mutableStateOf("sunil.clean@upi") }

  var capturedDocBitmap by remember { mutableStateOf<Bitmap?>(null) }
  val docCameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicturePreview()
  ) { bm: Bitmap? ->
    capturedDocBitmap = bm
  }

  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Partner KYC & Verification", fontWeight = FontWeight.Bold) },
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
      // KYC Status Banner
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = CleankrGreen.copy(alpha = 0.12f),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, CleankrGreen.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = CleankrGreen, modifier = Modifier.size(32.dp))
          Column {
            Text("KYC Status: ${profile.kycStatus.name}", fontWeight = FontWeight.Bold, color = CleankrGreen, fontSize = 15.sp)
            Text("Government identity verified & cleared for on-site services.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }

      // Government ID Section
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Government Identity Documents", fontWeight = FontWeight.Bold, fontSize = 14.sp)

          OutlinedTextField(
            value = aadhaarInput,
            onValueChange = { aadhaarInput = it },
            label = { Text("Aadhaar Number (Last 4 Digits or Full)") },
            prefix = { Text("XXXX-XXXX-") },
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = panInput,
            onValueChange = { panInput = it },
            label = { Text("PAN Card Number") },
            prefix = { Text("XXXXX") },
            modifier = Modifier.fillMaxWidth()
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Police Clearance Certificate", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
              Text("Status: ${profile.policeVerificationStatus}", fontSize = 11.sp, color = CleankrGreen)
            }
            Icon(Icons.Default.Policy, contentDescription = null, tint = CleankrCoral)
          }

          Spacer(modifier = Modifier.height(4.dp))

          // Document Photo Proof
          OutlinedButton(
            onClick = { docCameraLauncher.launch(null) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (capturedDocBitmap == null) "Take Photo of Govt ID / Certificate" else "Retake Photo")
          }

          capturedDocBitmap?.let { bm ->
            Image(
              bitmap = bm.asImageBitmap(),
              contentDescription = "Document",
              modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, CleankrCoral, RoundedCornerShape(8.dp))
            )
          }
        }
      }

      // Bank & Payout Details
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Payout & Bank Details", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Text("Earnings are automatically transferred to this account upon withdrawal request.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

          OutlinedTextField(
            value = bankAccountInput,
            onValueChange = { bankAccountInput = it },
            label = { Text("Bank Account Number") },
            prefix = { Text("*******") },
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = ifscInput,
            onValueChange = { ifscInput = it },
            label = { Text("Bank IFSC Code") },
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = upiInput,
            onValueChange = { upiInput = it },
            label = { Text("UPI Virtual Payment Address (VPA)") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      }

      // Save & Update Button
      Button(
        onClick = {
          viewModel.submitKyc(aadhaarInput, panInput, bankAccountInput, ifscInput, upiInput)
        },
        colors = ButtonDefaults.buttonColors(containerColor = CleankrCoral),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_kyc_button")
      ) {
        Text("Save & Verify Documents", fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
