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
import androidx.compose.material3.HorizontalDivider
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
fun MyHubScreen(viewModel: PartnerViewModel) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "My Hub & Equipment Depot",
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
          Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Cleankr Central Depot #04",
                fontWeight = FontWeight.Black,
                fontSize = 17.sp,
                fontStyle = FontStyle.Italic
              )

              Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFE8F5E9)) {
                Text(
                  text = "Assigned",
                  color = Color(0xFF1B5E20),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
              }
            }

            Text(
              text = "Plot 42, Link Road Industrial Estate, Andheri West, Mumbai 400053",
              fontSize = 13.sp,
              color = Color(0xFF616161)
            )

            HorizontalDivider(color = Color(0xFFF0F0F0))

            Text(
              text = "Depot Manager: Rajesh Patil (+91 98200 48123)",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E1E1E)
            )
            Text(
              text = "Hours: 07:00 AM – 09:00 PM (Chemical Refill & Tool Testing)",
              fontSize = 12.sp,
              color = Color(0xFF757575)
            )
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
              text = "Equipment Kit Inventory",
              fontWeight = FontWeight.Black,
              fontSize = 15.sp,
              fontStyle = FontStyle.Italic
            )
            Text("✓ Industrial Wet & Dry Vacuum (Karcher WD3) - Checked Out", fontSize = 13.sp)
            Text("✓ Microfiber Mop & Descaling Foam Gun - Inspected", fontSize = 13.sp)
            Text("✓ Safety Goggles, Chemical Respirator Mask - Provided", fontSize = 13.sp)
            Text("✓ Eco-friendly Bathroom Descaler (5L refill) - Active", fontSize = 13.sp)
          }
        }
      }
    }
  }
}
