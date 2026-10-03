package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CleankrViolet

@Composable
fun CleankrHelpFloatingButton(onHelpClick: () -> Unit) {
  FloatingActionButton(
    onClick = onHelpClick,
    shape = CircleShape,
    containerColor = CleankrViolet,
    contentColor = Color.White,
    modifier = Modifier.size(52.dp)
  ) {
    Icon(
      imageVector = Icons.Default.HelpOutline,
      contentDescription = "Help & Support",
      modifier = Modifier.size(24.dp)
    )
  }
}
