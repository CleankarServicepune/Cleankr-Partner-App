package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.PartnerViewModel
import com.example.ui.theme.CleankrAmber
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrCyan
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrPeach
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class EarningsTimeframe {
  TODAY,
  THIS_WEEK,
  THIS_MONTH
}

@Composable
fun EarningsScreen(viewModel: PartnerViewModel) {
  val earnings by viewModel.earnings.collectAsState()
  val withdrawals by viewModel.withdrawals.collectAsState()
  val profile by viewModel.profile.collectAsState()
  val allJobs by viewModel.allJobs.collectAsState()

  var selectedTimeframe by remember { mutableStateOf(EarningsTimeframe.TODAY) }
  var showWithdrawDialog by remember { mutableStateOf(false) }
  var withdrawAmountInput by remember { mutableStateOf("5000") }
  var withdrawDestination by remember { mutableStateOf(profile.bankAccountMasked) }

  val timeframeEarnings = when (selectedTimeframe) {
    EarningsTimeframe.TODAY -> earnings.todayEarnings
    EarningsTimeframe.THIS_WEEK -> earnings.weekEarnings
    EarningsTimeframe.THIS_MONTH -> earnings.monthEarnings
  }

  val completedJobs = allJobs.filter { it.status.name == "COMPLETED" }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Partner Payouts & Earnings",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "Transparent automated settlements with zero hidden deductions.",
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // Withdrawable Balance Hero Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.5.dp, CleankrGreen.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "AVAILABLE FOR WITHDRAWAL",
              fontSize = 11.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 1.sp,
              color = CleankrGreen
            )
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = CleankrGreen.copy(alpha = 0.15f)
            ) {
              Text(
                text = "Instant Payout",
                color = CleankrGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "₹${earnings.withdrawableBalance.toInt()}",
            fontSize = 34.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "In Clearing / Pending: ₹${earnings.pendingBalance.toInt()}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "Total Paid: ₹${earnings.totalPaidOut.toInt()}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = { showWithdrawDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = CleankrGreen),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("request_payout_button")
          ) {
            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Transfer to Bank / UPI", fontWeight = FontWeight.Bold, color = Color.White)
          }
        }
      }
    }

    // Timeframe filter tabs (Today / Week / Month)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        EarningsTimeframe.values().forEach { tf ->
          val label = when (tf) {
            EarningsTimeframe.TODAY -> "Daily"
            EarningsTimeframe.THIS_WEEK -> "Weekly"
            EarningsTimeframe.THIS_MONTH -> "Monthly"
          }
          FilterChip(
            selected = selectedTimeframe == tf,
            onClick = { selectedTimeframe = tf },
            label = { Text(label, fontWeight = if (selectedTimeframe == tf) FontWeight.Bold else FontWeight.Normal) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CleankrCoral.copy(alpha = 0.2f),
              selectedLabelColor = CleankrCoral
            ),
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // Timeframe Summary Card
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = when (selectedTimeframe) {
                EarningsTimeframe.TODAY -> "Today's Gross Earnings"
                EarningsTimeframe.THIS_WEEK -> "Current Week Total"
                EarningsTimeframe.THIS_MONTH -> "Current Month Total"
              },
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "₹${timeframeEarnings.toInt()}",
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              color = CleankrCoral
            )
          }
          Icon(Icons.Default.TrendingUp, contentDescription = null, tint = CleankrCoral, modifier = Modifier.size(36.dp))
        }
      }
    }

    // Recent Withdrawal Requests
    item {
      Text(
        text = "Payout Requests & History",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
      )
    }

    items(withdrawals) { wdl ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (wdl.status == "Paid") CleankrGreen.copy(alpha = 0.15f) else CleankrAmber.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                if (wdl.status == "Paid") Icons.Default.CheckCircle else Icons.Default.HourglassTop,
                contentDescription = null,
                tint = if (wdl.status == "Paid") CleankrGreen else CleankrAmber,
                modifier = Modifier.size(20.dp)
              )
            }
            Column {
              Text("₹${wdl.amount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
              Text(wdl.destination, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("Ref: ${wdl.referenceNumber}", fontSize = 10.sp, color = Color.Gray)
            }
          }
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (wdl.status == "Paid") CleankrGreen.copy(alpha = 0.15f) else CleankrAmber.copy(alpha = 0.15f)
          ) {
            Text(
              text = wdl.status,
              color = if (wdl.status == "Paid") CleankrGreen else CleankrAmber,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
        }
      }
    }

    // Job-wise Earnings Section
    item {
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Completed Jobs Payout Breakdown",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
      )
    }

    items(completedJobs) { job ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(job.serviceTitle, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text("${job.date} • ${job.customerName}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          Column(horizontalAlignment = Alignment.End) {
            Text("+₹${job.estimatedEarnings.toInt()}", fontWeight = FontWeight.Black, color = CleankrGreen, fontSize = 14.sp)
            Text("Settled", fontSize = 10.sp, color = Color.Gray)
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  // Withdrawal Dialog
  if (showWithdrawDialog) {
    AlertDialog(
      onDismissRequest = { showWithdrawDialog = false },
      title = { Text("Transfer Withdrawable Balance") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "Maximum available: ₹${earnings.withdrawableBalance.toInt()}",
            fontSize = 12.sp,
            color = CleankrGreen,
            fontWeight = FontWeight.Bold
          )
          OutlinedTextField(
            value = withdrawAmountInput,
            onValueChange = { withdrawAmountInput = it },
            label = { Text("Amount (₹)") },
            prefix = { Text("₹") },
            modifier = Modifier.fillMaxWidth()
          )

          Text("Select Destination:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
              selected = withdrawDestination == profile.bankAccountMasked,
              onClick = { withdrawDestination = profile.bankAccountMasked },
              label = { Text("Bank A/C") }
            )
            FilterChip(
              selected = withdrawDestination == profile.upiIdMasked,
              onClick = { withdrawDestination = profile.upiIdMasked },
              label = { Text("UPI ID") }
            )
          }
          Text("Destination: $withdrawDestination", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val amount = withdrawAmountInput.toDoubleOrNull() ?: 0.0
            viewModel.requestWithdrawal(amount, withdrawDestination)
            showWithdrawDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = CleankrGreen)
        ) {
          Text("Confirm Transfer")
        }
      },
      dismissButton = {
        TextButton(onClick = { showWithdrawDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
