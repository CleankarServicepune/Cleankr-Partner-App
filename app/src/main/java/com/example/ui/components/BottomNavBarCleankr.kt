package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen

@Composable
fun BottomNavBarCleankr(
  currentScreen: AppScreen,
  onTabSelect: (AppScreen) -> Unit
) {
  NavigationBar(
    containerColor = Color.White,
    tonalElevation = 6.dp,
    modifier = Modifier.height(68.dp)
  ) {
    // 1. Home
    NavigationBarItem(
      selected = currentScreen == AppScreen.HOME,
      onClick = { onTabSelect(AppScreen.HOME) },
      icon = {
        Icon(
          imageVector = Icons.Default.Home,
          contentDescription = "Home",
          tint = if (currentScreen == AppScreen.HOME) Color.Black else Color(0xFF9E9E9E),
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "Home",
          fontWeight = if (currentScreen == AppScreen.HOME) FontWeight.Black else FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          fontSize = 11.sp,
          color = if (currentScreen == AppScreen.HOME) Color.Black else Color(0xFF757575)
        )
      },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = Color.Transparent
      )
    )

    // 2. New
    NavigationBarItem(
      selected = currentScreen == AppScreen.NEW_JOBS,
      onClick = { onTabSelect(AppScreen.NEW_JOBS) },
      icon = {
        Icon(
          imageVector = Icons.Default.Description,
          contentDescription = "New",
          tint = if (currentScreen == AppScreen.NEW_JOBS) Color.Black else Color(0xFF9E9E9E),
          modifier = Modifier.size(22.dp)
        )
      },
      label = {
        Text(
          text = "New",
          fontWeight = if (currentScreen == AppScreen.NEW_JOBS) FontWeight.Black else FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          fontSize = 11.sp,
          color = if (currentScreen == AppScreen.NEW_JOBS) Color.Black else Color(0xFF757575)
        )
      },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = Color.Transparent
      )
    )

    // 3. Ongoing (with red badge 0)
    NavigationBarItem(
      selected = currentScreen == AppScreen.ONGOING_JOBS,
      onClick = { onTabSelect(AppScreen.ONGOING_JOBS) },
      icon = {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Default.Alarm,
            contentDescription = "Ongoing",
            tint = if (currentScreen == AppScreen.ONGOING_JOBS) Color.Black else Color(0xFF9E9E9E),
            modifier = Modifier.size(22.dp)
          )
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .size(13.dp)
              .background(Color(0xFFD32F2F), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "0",
              color = Color.White,
              fontSize = 8.sp,
              fontWeight = FontWeight.Black
            )
          }
        }
      },
      label = {
        Text(
          text = "Ongoing",
          fontWeight = if (currentScreen == AppScreen.ONGOING_JOBS) FontWeight.Black else FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          fontSize = 11.sp,
          color = if (currentScreen == AppScreen.ONGOING_JOBS) Color.Black else Color(0xFF757575)
        )
      },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = Color.Transparent
      )
    )

    // 4. Target
    NavigationBarItem(
      selected = currentScreen == AppScreen.TARGET,
      onClick = { onTabSelect(AppScreen.TARGET) },
      icon = {
        Icon(
          imageVector = Icons.Default.Speed,
          contentDescription = "Target",
          tint = if (currentScreen == AppScreen.TARGET) Color.Black else Color(0xFF9E9E9E),
          modifier = Modifier.size(22.dp)
        )
      },
      label = {
        Text(
          text = "Target",
          fontWeight = if (currentScreen == AppScreen.TARGET) FontWeight.Black else FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          fontSize = 11.sp,
          color = if (currentScreen == AppScreen.TARGET) Color.Black else Color(0xFF757575)
        )
      },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = Color.Transparent
      )
    )

    // 5. Money
    NavigationBarItem(
      selected = currentScreen == AppScreen.MONEY,
      onClick = { onTabSelect(AppScreen.MONEY) },
      icon = {
        Icon(
          imageVector = Icons.Default.AccountBalanceWallet,
          contentDescription = "Money",
          tint = if (currentScreen == AppScreen.MONEY) Color.Black else Color(0xFF9E9E9E),
          modifier = Modifier.size(22.dp)
        )
      },
      label = {
        Text(
          text = "Money",
          fontWeight = if (currentScreen == AppScreen.MONEY) FontWeight.Black else FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          fontSize = 11.sp,
          color = if (currentScreen == AppScreen.MONEY) Color.Black else Color(0xFF757575)
        )
      },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = Color.Transparent
      )
    )
  }
}
