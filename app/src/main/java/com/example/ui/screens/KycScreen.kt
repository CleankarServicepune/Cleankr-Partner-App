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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun KycScreen(viewModel: PartnerViewModel) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "KYC & Skill India (NSDC)",
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
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
              text = "Government & Skill Credentials",
              fontWeight = FontWeight.Black,
              fontSize = 16.sp,
              fontStyle = FontStyle.Italic
            )
            KycBadgeRow(name = "Aadhaar e-KYC (UIDAI)", status = "Verified", isDone = true)
            KycBadgeRow(name = "PAN Card Tax ID", status = "Verified", isDone = true)
            KycBadgeRow(name = "NSDC Skill India Certificate", status = "Certified Level 4", isDone = true)
            KycBadgeRow(name = "Police Clearance Certificate (PCC)", status = "Clear / Approved", isDone = true)
            KycBadgeRow(name = "Bank Account Verification (Penny Drop)", status = "Verified", isDone = true)
          }
        }
      }
    }
  }
}

@Composable
fun KycBadgeRow(name: String, status: String, isDone: Boolean) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = name, fontSize = 13.sp, color = Color(0xFF333333))
    Surface(
      shape = RoundedCornerShape(6.dp),
      color = if (isDone) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
    ) {
      Text(
        text = status,
        color = if (isDone) Color(0xFF2E7D32) else Color(0xFFE65100),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
      )
    }
  }
}
