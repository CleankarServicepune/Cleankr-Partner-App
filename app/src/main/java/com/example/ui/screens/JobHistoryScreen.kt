package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.ui.theme.CleankrGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobHistoryScreen(viewModel: PartnerViewModel) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Job History",
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
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        Text(
          text = "Completed Jobs & Service Proof",
          fontWeight = FontWeight.Black,
          fontSize = 17.sp,
          fontStyle = FontStyle.Italic,
          color = Color(0xFF1E1E1E)
        )
      }

      item {
        HistoryCard(
          jobId = "CK-8412",
          service = "Full Deep Villa Cleaning",
          date = "21 Sep 2026 • 10:30 AM",
          payout = "₹2,850",
          customer = "Vikram Mehta",
          status = "Completed"
        )
      }

      item {
        HistoryCard(
          jobId = "CK-8390",
          service = "Sofa & Upholstery Descaling",
          date = "19 Sep 2026 • 02:00 PM",
          payout = "₹1,450",
          customer = "Sneha Kulkarni",
          status = "Completed"
        )
      }

      item {
        HistoryCard(
          jobId = "CK-8241",
          service = "Kitchen Exhaust Deep Clean",
          date = "17 Sep 2026 • 11:00 AM",
          payout = "₹1,260",
          customer = "Pooja Verma",
          status = "Completed"
        )
      }
    }
  }
}

@Composable
fun HistoryCard(
  jobId: String,
  service: String,
  date: String,
  payout: String,
  customer: String,
  status: String
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = Color(0xFFE8F5E9)
        ) {
          Text(
            text = "$status • #$jobId",
            color = Color(0xFF1B5E20),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
          )
        }

        Text(
          text = payout,
          color = CleankrGreen,
          fontWeight = FontWeight.Black,
          fontSize = 16.sp
        )
      }

      Text(
        text = service,
        fontWeight = FontWeight.Black,
        fontSize = 15.sp,
        color = Color(0xFF1E1E1E)
      )

      Text(
        text = "$date • Customer: $customer",
        fontSize = 12.sp,
        color = Color(0xFF616161)
      )
    }
  }
}
