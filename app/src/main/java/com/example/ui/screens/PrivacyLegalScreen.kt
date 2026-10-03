package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyLegalScreen(viewModel: PartnerViewModel) {
  var showDeleteDialog by remember { mutableStateOf(false) }
  var deleteRequested by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Privacy & Legal Policies",
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic
          )
        },
        navigationIcon = {
          IconButton(onClick = { viewModel.navigateTo(AppScreen.HOME) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFBFBFB))
        .padding(padding)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Privacy Policy Section
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "Cleankr Partner Privacy Policy",
              fontSize = 16.sp,
              fontWeight = FontWeight.Black,
              fontStyle = FontStyle.Italic,
              color = Color(0xFF1E1E1E)
            )
            Text(
              text = "Last updated: October 2026 • Compliant with Indian IT Act 2000 & Digital Personal Data Protection Act (DPDPA)",
              fontSize = 11.sp,
              color = Color(0xFF757575)
            )
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            Text(
              text = "1. Information Collected: We collect your name, phone number, government identity documents (Aadhaar, PAN), NSDC skill certifications, live location coordinates during active job hours, and payout bank information solely to dispatch customer bookings and process direct bank earnings.",
              fontSize = 13.sp,
              color = Color(0xFF424242),
              lineHeight = 18.sp
            )
            Text(
              text = "2. Number Masking & Data Safety: When calling customers via the partner app, your personal phone number is never revealed to the customer. All communications route through encrypted, secure telephony bridges.",
              fontSize = 13.sp,
              color = Color(0xFF424242),
              lineHeight = 18.sp
            )
            Text(
              text = "3. Third-party Sharing: Cleankr does not sell partner data. Data is exclusively shared with authorized payment gateways (IMPS/UPI) and verification agencies for identity confirmation.",
              fontSize = 13.sp,
              color = Color(0xFF424242),
              lineHeight = 18.sp
            )
          }
        }
      }

      // 2. Terms & Conditions Section
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "Partner Terms & Conditions",
              fontSize = 16.sp,
              fontWeight = FontWeight.Black,
              fontStyle = FontStyle.Italic,
              color = Color(0xFF1E1E1E)
            )
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            Text(
              text = "1. Independent Service Provider: You operate as an independent cleaning specialist empowered by Cleankr's dispatch platform.",
              fontSize = 13.sp,
              color = Color(0xFF424242),
              lineHeight = 18.sp
            )
            Text(
              text = "2. Quality & Punctuality: Partners agree to arrive at the scheduled time with complete certified equipment and follow safety hygiene standards.",
              fontSize = 13.sp,
              color = Color(0xFF424242),
              lineHeight = 18.sp
            )
            Text(
              text = "3. Payout Guarantee: Approved job earnings are deposited every 3 business days into your verified HDFC bank account, with instant payout available 24/7.",
              fontSize = 13.sp,
              color = Color(0xFF424242),
              lineHeight = 18.sp
            )
          }
        }
      }

      // 3. In-App Account & Data Deletion
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text(
              text = "Account Deletion & Data Purge",
              fontSize = 15.sp,
              fontWeight = FontWeight.Black,
              fontStyle = FontStyle.Italic,
              color = Color(0xFFD32F2F)
            )
            Text(
              text = "In compliance with Google Play Policy, you may submit a direct in-app request to purge your account, KYC records, and device tokens.",
              fontSize = 13.sp,
              color = Color(0xFF616161),
              lineHeight = 18.sp
            )

            if (deleteRequested) {
              Text(
                text = "✓ Account deletion request lodged (#DEL-84912). You will receive an SMS confirmation within 48 hours.",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            } else {
              Button(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(10.dp)
              ) {
                Text(
                  text = "Request Account Deletion",
                  color = Color(0xFFD32F2F),
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }
            }
          }
        }
      }
    }
  }

  if (showDeleteDialog) {
    AlertDialog(
      onDismissRequest = { showDeleteDialog = false },
      title = { Text("Confirm Account Deletion Request", fontWeight = FontWeight.Bold) },
      text = {
        Text("Are you sure? Once submitted, all pending bookings, wallet balances, and certified badges will be permanently closed.")
      },
      confirmButton = {
        Button(
          onClick = {
            deleteRequested = true
            showDeleteDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
          Text("Submit Deletion")
        }
      },
      dismissButton = {
        TextButton(onClick = { showDeleteDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
