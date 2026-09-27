package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TimeSlot
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrGreen

/**
 * Exact Urban Company Partner style slot card:
 * - Direct left-aligned time text (e.g. "08:00 AM – 09:00 AM")
 * - Peak hour banner with bolt icon ("⚡ PEAK HOUR SLOT") with light green tint
 * - Right-aligned high-contrast green switch toggle
 */
@Composable
fun SlotToggleRow(
  slot: TimeSlot,
  isDateDayOff: Boolean,
  onToggle: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val isBooked = slot.bookedJobId != null
  val isSlotActive = slot.isEnabled && !isDateDayOff

  // Background and border matching UC style
  val isPeak = slot.isPeakHour

  val cardBgColor = when {
    isBooked -> CleankrCoral.copy(alpha = 0.08f)
    isPeak && isSlotActive -> Color(0xFFE8F5E9) // UC subtle mint green for peak
    isPeak -> Color(0xFFF1F8F3)
    isSlotActive -> Color(0xFFF0F4F8) // Clean light slate/gray card
    else -> Color(0xFFF5F7FA)
  }

  val borderColor = when {
    isBooked -> CleankrCoral.copy(alpha = 0.4f)
    isPeak -> Color(0xFFA5D6A7) // Light emerald border for peak slot
    else -> Color(0xFFCFD8DC) // Neutral subtle border
  }

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = cardBgColor),
    border = BorderStroke(1.dp, borderColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    modifier = modifier
      .fillMaxWidth()
      .clickable(enabled = !isDateDayOff) { onToggle(slot.id) }
      .testTag("slot_row_${slot.id}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = if (isPeak) 12.dp else 14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(3.dp)
      ) {
        // Peak Hour Badge matching UC image
        if (isPeak) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Bolt,
              contentDescription = null,
              tint = Color(0xFF00796B),
              modifier = Modifier.size(14.dp)
            )
            Text(
              text = "PEAK HOUR SLOT",
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color(0xFF00796B),
              letterSpacing = 0.6.sp
            )
          }
        }

        // Slot Time Label
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = slot.label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSlotActive) Color(0xFF1E293B) else Color(0xFF64748B)
          )

          if (isBooked) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = CleankrCoral.copy(alpha = 0.15f)
            ) {
              Text(
                text = "Booked #${slot.bookedJobId}",
                color = CleankrCoral,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }
      }

      // Toggle Switch on Right - NEVER locked, partner can always toggle slot availability
      Switch(
        checked = slot.isEnabled && !isDateDayOff,
        onCheckedChange = { onToggle(slot.id) },
        enabled = !isDateDayOff,
        colors = SwitchDefaults.colors(
          checkedThumbColor = Color(0xFF1B5E20),
          checkedTrackColor = Color(0xFFA5D6A7),
          uncheckedThumbColor = Color(0xFF78909C),
          uncheckedTrackColor = Color(0xFFCFD8DC),
          disabledCheckedTrackColor = Color(0xFFC8E6C9),
          disabledUncheckedTrackColor = Color(0xFFE2E8F0)
        ),
        modifier = Modifier.testTag("switch_${slot.id}")
      )
    }
  }
}
