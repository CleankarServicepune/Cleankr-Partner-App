package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrCyan
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrPeach
import com.example.ui.theme.CleankrViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHubScreen(viewModel: PartnerViewModel) {
  val hub by viewModel.assignedHub.collectAsState()
  val context = LocalContext.current
  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "My Operations Hub",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        },
        navigationIcon = {
          IconButton(
            onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
            modifier = Modifier.testTag("my_hub_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(
            onClick = { viewModel.syncHubFromAdmin(hub.hubId) },
            modifier = Modifier.testTag("my_hub_sync_button")
          ) {
            Icon(Icons.Default.Refresh, contentDescription = "Sync Hub with Admin")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(scrollState)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Hero Card: Hub Name, Code & Status
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.horizontalGradient(
                colors = listOf(
                  CleankrViolet.copy(alpha = 0.08f),
                  CleankrCoral.copy(alpha = 0.08f)
                )
              )
            )
            .padding(18.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = CleankrViolet.copy(alpha = 0.15f)
              ) {
                Text(
                  text = hub.hubCode,
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 11.sp,
                  color = CleankrViolet,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = CleankrGreen.copy(alpha = 0.15f)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CleankrGreen, modifier = Modifier.size(12.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = hub.status,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = CleankrGreen
                  )
                }
              }
            }

            Text(
              text = hub.hubName,
              fontSize = 20.sp,
              fontWeight = FontWeight.Black,
              color = MaterialTheme.colorScheme.onSurface
            )

            Text(
              text = "Zone: ${hub.zone} • ${hub.city}",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = CleankrCoral
            )

            Text(
              text = "Assigned via Cleankr Admin Panel on ${hub.adminAssignedDate}. Designated center for equipment checkout, weekly chemical refills, and safety audits.",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // 2. Hub Address & Directions Card
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = CleankrCoral, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Hub Location & Address", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          }

          Text(
            text = hub.address,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Button(
            onClick = {
              val uri = Uri.parse("geo:${hub.latitude},${hub.longitude}?q=${Uri.encode(hub.hubName)}")
              val intent = Intent(Intent.ACTION_VIEW, uri)
              try {
                context.startActivity(intent)
              } catch (e: Exception) {
                // Fallback to browser web maps
                val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${hub.latitude},${hub.longitude}")
                context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = CleankrCoral),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("hub_navigate_maps_button")
          ) {
            Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Navigate to Hub in Google Maps", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
      }

      // 3. Hub Manager & Operations Contact Card
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(CleankrMagenta.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Person, contentDescription = null, tint = CleankrMagenta)
            }
            Column(modifier = Modifier.weight(1f)) {
              Text(hub.hubManagerName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
              Text("Hub Operations & Equipment Lead", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(hub.hubManagerPhone, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CleankrViolet)
            }
            IconButton(
              onClick = {
                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${hub.hubManagerPhone.replace(" ", "")}"))
                context.startActivity(dialIntent)
              },
              modifier = Modifier
                .size(40.dp)
                .background(CleankrGreen.copy(alpha = 0.15f), CircleShape)
                .testTag("hub_call_manager_button")
            ) {
              Icon(Icons.Default.Call, contentDescription = "Call Manager", tint = CleankrGreen, modifier = Modifier.size(18.dp))
            }
          }

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.AccessTime, contentDescription = null, tint = CleankrCyan, modifier = Modifier.size(16.dp))
              Text(
                text = "Operating Timings: ${hub.hubTimings}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      // 4. Hub Facilities & Services
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Storefront, contentDescription = null, tint = CleankrViolet, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Hub Facilities & Services", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          }

          hub.facilities.forEach { facility ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.padding(vertical = 3.dp)
            ) {
              Icon(Icons.Default.Verified, contentDescription = null, tint = CleankrViolet, modifier = Modifier.size(14.dp))
              Text(text = facility, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
            }
          }
        }
      }

      // 5. Partner's Checked-out Equipment Kit
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Inventory, contentDescription = null, tint = CleankrCoral, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Checked-out Kit from Hub", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Text("5 Items Active", fontSize = 11.sp, color = CleankrGreen, fontWeight = FontWeight.Bold)
          }

          hub.assignedEquipment.forEach { eq ->
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
              modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Icon(Icons.Default.Build, contentDescription = null, tint = CleankrMagenta, modifier = Modifier.size(14.dp))
                Text(text = eq, fontSize = 12.sp, fontWeight = FontWeight.Medium)
              }
            }
          }
        }
      }

      // 6. Admin Panel Sync Notice
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.Top,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Icon(Icons.Default.Shield, contentDescription = null, tint = CleankrCyan, modifier = Modifier.size(20.dp))
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = "Admin Panel Controlled Operations",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Hub assignments, machinery serial transfers, and weekly chemical allowances are managed via Cleankr Admin Panel. If you relocate or require a hub transfer, submit a transfer request via Help & Support.",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      OutlinedButton(
        onClick = { viewModel.navigateTo(AppScreen.HELP_SUPPORT) },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("hub_request_transfer_button")
      ) {
        Text("Request Hub Transfer / Support", fontSize = 13.sp, color = CleankrViolet, fontWeight = FontWeight.SemiBold)
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
