package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.theme.CleankrCoral

@Composable
fun CleankrTopHeader(
  onMenuClick: () -> Unit,
  onEmergencyClick: () -> Unit,
  onNavigate: (AppScreen) -> Unit,
  onRefresh: () -> Unit = {}
) {
  var showMenu by remember { mutableStateOf(false) }

  Surface(
    color = Color.White,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Left: Hamburger + Logo
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        IconButton(
          onClick = onMenuClick,
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = Color(0xFF1E1E1E),
            modifier = Modifier.size(24.dp)
          )
        }

        // Cleankr Logo & Text
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(5.dp),
          modifier = Modifier.clickable { onNavigate(AppScreen.HOME) }
        ) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(CleankrCoral),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "K",
              color = Color.White,
              fontWeight = FontWeight.Black,
              fontSize = 13.sp
            )
          }

          Text(
            text = "CLEANKR",
            fontWeight = FontWeight.Black,
            fontSize = 17.sp,
            color = Color(0xFF1A1A1A),
            letterSpacing = 1.1.sp
          )
        }
      }

      // Right: Points + Bell + Emergency + Overflow Menu
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // "77 ◈"
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color.White,
          border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
          modifier = Modifier.height(28.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
          ) {
            Text(
              text = "77",
              fontWeight = FontWeight.Black,
              fontSize = 12.sp,
              color = Color(0xFF1E1E1E)
            )
            Text(
              text = "◈",
              color = Color(0xFF5A31F4),
              fontSize = 12.sp,
              fontWeight = FontWeight.Black
            )
          }
        }

        // Notification Bell with badge "3"
        Box(
          modifier = Modifier
            .size(32.dp)
            .clickable { onNavigate(AppScreen.SETTINGS_NOTIFICATIONS) },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = Color(0xFF212121),
            modifier = Modifier.size(20.dp)
          )

          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .size(15.dp)
              .background(Color(0xFFD32F2F), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "3",
              color = Color.White,
              fontSize = 9.sp,
              fontWeight = FontWeight.Black
            )
          }
        }

        // "Emergency 🔔" pill button
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color(0xFFFFEBEE),
          border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
          modifier = Modifier
            .height(28.dp)
            .clickable { onEmergencyClick() }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
          ) {
            Text(
              text = "Emergency",
              color = Color(0xFFC62828),
              fontSize = 11.sp,
              fontWeight = FontWeight.Black,
              fontStyle = FontStyle.Italic
            )
            Text(text = "🔔", fontSize = 10.sp)
          }
        }

        // Real Three-Dot Overflow Menu
        Box {
          IconButton(
            onClick = { showMenu = true },
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "More Options",
              tint = Color(0xFF212121),
              modifier = Modifier.size(20.dp)
            )
          }

          DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
          ) {
            DropdownMenuItem(
              text = { Text("Refresh Data", fontWeight = FontWeight.Bold) },
              leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = null) },
              onClick = {
                showMenu = false
                onRefresh()
              }
            )

            DropdownMenuItem(
              text = { Text("Help & Support", fontWeight = FontWeight.Bold) },
              leadingIcon = { Icon(Icons.Default.HelpOutline, contentDescription = null) },
              onClick = {
                showMenu = false
                onNavigate(AppScreen.HELP_SUPPORT)
              }
            )

            DropdownMenuItem(
              text = { Text("Notifications", fontWeight = FontWeight.Bold) },
              leadingIcon = { Icon(Icons.Default.Notifications, contentDescription = null) },
              onClick = {
                showMenu = false
                onNavigate(AppScreen.SETTINGS_NOTIFICATIONS)
              }
            )

            DropdownMenuItem(
              text = { Text("Privacy & Terms", fontWeight = FontWeight.Bold) },
              leadingIcon = { Icon(Icons.Default.Security, contentDescription = null) },
              onClick = {
                showMenu = false
                onNavigate(AppScreen.PRIVACY_LEGAL)
              }
            )

            DropdownMenuItem(
              text = { Text("Settings", fontWeight = FontWeight.Bold) },
              leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) },
              onClick = {
                showMenu = false
                onNavigate(AppScreen.SETTINGS_NOTIFICATIONS)
              }
            )
          }
        }
      }
    }
  }
}
