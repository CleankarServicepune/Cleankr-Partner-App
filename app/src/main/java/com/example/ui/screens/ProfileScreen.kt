package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: PartnerViewModel) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Partner Profile",
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
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFEDE7F6)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "SU",
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                color = Color(0xFF4A148C)
              )
            }

            Text(
              text = "Sunil Sharma",
              fontWeight = FontWeight.Black,
              fontSize = 20.sp,
              color = Color(0xFF1E1E1E)
            )

            Text(
              text = "+91 98*** **412",
              fontSize = 14.sp,
              color = Color(0xFF616161)
            )

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFFFFF8E1),
              modifier = Modifier.padding(top = 4.dp)
            ) {
              Text(
                text = "★ 4.92 Star Partner Rating",
                color = Color(0xFFF57F17),
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
              )
            }
          }
        }
      }

      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
              text = "Partner Information",
              fontWeight = FontWeight.Black,
              fontSize = 16.sp,
              fontStyle = FontStyle.Italic
            )
            ProfileRow(label = "Partner ID", value = "CK-PT-98412")
            ProfileRow(label = "City Hub", value = "Andheri West, Mumbai (MH-02)")
            ProfileRow(label = "Joined", value = "14 January 2025")
            ProfileRow(label = "Primary Skill", value = "Full Home & Villa Deep Cleaning")
            ProfileRow(label = "NSDC Skill ID", value = "NSDC-IND-2025-4819")
          }
        }
      }
    }
  }
}

@Composable
fun ProfileRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(text = label, color = Color(0xFF757575), fontSize = 13.sp)
    Text(text = value, color = Color(0xFF212121), fontWeight = FontWeight.Bold, fontSize = 13.sp)
  }
}
