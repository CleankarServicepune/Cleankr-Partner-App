package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.CleankrHelpFloatingButton
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrRed

@Composable
fun TargetScreen(viewModel: PartnerViewModel) {
  val profile by viewModel.profile.collectAsState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF9F9FB))
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(10.dp))
        // Title & Learn about strikes link
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Low performance",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E1A22)
          )
          Text(
            text = "Learn about strikes",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF5A31F4),
            modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.HELP_SUPPORT) }
          )
        }
      }

      // Warning Card (Screenshot 2: Low performance. You might get a profile strike on 28 Sep >)
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFDE8E8)),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.navigateTo(AppScreen.HELP_SUPPORT) }
            .testTag("low_performance_warning_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Surface(
                shape = CircleShape,
                color = CleankrRed.copy(alpha = 0.15f),
                modifier = Modifier.size(36.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                    contentDescription = null,
                    tint = CleankrRed,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }
              Text(
                text = "Low performance. You might get a profile strike on 28 Sep >",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E1A22),
                lineHeight = 18.sp
              )
            }
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
              contentDescription = null,
              tint = Color(0xFF9E9E9E),
              modifier = Modifier.size(13.dp)
            )
          }
        }
      }

      // Section: Metrics
      item {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Metrics",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1E1A22)
        )
        Text(
          text = "Next performance review on 28 Sep",
          fontSize = 13.sp,
          color = Color(0xFF757575)
        )
      }

      // Metrics Grid (Row 1: Rating & Cancellations)
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // 1. Rating
          MetricCard(
            modifier = Modifier.weight(1f),
            title = "Rating",
            condition = "Keep 4.6 or above",
            value = "${profile.rating}",
            isPass = true
          )

          // 2. Cancellations
          MetricCard(
            modifier = Modifier.weight(1f),
            title = "Cancellations",
            condition = "Keep 4 or below",
            value = "6",
            isPass = false
          )
        }
      }

      // Metrics Grid (Row 2: Peak unavailable & Weekend unavailable)
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // 3. Peak unavailable hours
          MetricCard(
            modifier = Modifier.weight(1f),
            title = "Peak unavailable hours",
            condition = "Keep 48 or below",
            value = "40.64",
            isPass = true
          )

          // 4. Weekend unavailable hours
          MetricCard(
            modifier = Modifier.weight(1f),
            title = "Weekend unavailable hours",
            condition = "Keep 30 or below",
            value = "30",
            isPass = true
          )
        }
      }

      // Metrics Grid (Row 3: Audit failures)
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // 5. Audit failures
          MetricCard(
            modifier = Modifier.weight(0.5f),
            title = "Audit failures",
            condition = "Keep 100 or below",
            value = "2",
            isPass = true
          )

          Spacer(modifier = Modifier.weight(0.5f))
        }

        Spacer(modifier = Modifier.height(80.dp))
      }
    }

    // Floating Help Button
    CleankrHelpFloatingButton(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 16.dp, bottom = 16.dp),
      onHelpClick = { viewModel.navigateTo(AppScreen.HELP_SUPPORT) }
    )
  }
}

@Composable
private fun MetricCard(
  modifier: Modifier = Modifier,
  title: String,
  condition: String,
  value: String,
  isPass: Boolean
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier.testTag("metric_card_${title.lowercase().replace(" ", "_")}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1E1A22)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = condition,
        fontSize = 11.sp,
        color = Color(0xFF757575)
      )
      Spacer(modifier = Modifier.height(14.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = value,
          fontSize = 19.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color(0xFF1E1A22)
        )
        // Check or Cross badge
        Icon(
          imageVector = if (isPass) Icons.Default.Check else Icons.Default.Close,
          contentDescription = null,
          tint = if (isPass) CleankrGreen else CleankrRed,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}
