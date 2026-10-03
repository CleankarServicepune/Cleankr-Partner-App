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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.theme.CleankrViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(viewModel: PartnerViewModel) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Partner Help & Support",
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
              text = "24/7 Dedicated Partner Hotline",
              fontWeight = FontWeight.Black,
              fontSize = 16.sp,
              fontStyle = FontStyle.Italic
            )
            Text(
              text = "Toll-Free: 1800-CLEANKR-PARTNER (1800-253-2657)",
              fontWeight = FontWeight.Bold,
              color = CleankrViolet,
              fontSize = 14.sp
            )
            Text(
              text = "Email: partner-support@cleankr.in\nAverage response time: under 3 minutes",
              fontSize = 13.sp,
              color = Color(0xFF616161)
            )
            Button(
              onClick = {},
              colors = ButtonDefaults.buttonColors(containerColor = CleankrViolet),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("Call Priority Hotline Now")
            }
          }
        }
      }

      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              text = "Common Operational Issues",
              fontWeight = FontWeight.Black,
              fontSize = 15.sp,
              fontStyle = FontStyle.Italic
            )
            Text("• Customer not reachable on-site (Wait 10 min protocol)", fontSize = 13.sp)
            Text("• Service change request / Extra room addition", fontSize = 13.sp)
            Text("• Equipment malfunction / Chemical shortage", fontSize = 13.sp)
            Text("• Payout or settlement inquiry", fontSize = 13.sp)
          }
        }
      }
    }
  }
}
