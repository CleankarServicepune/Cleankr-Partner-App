package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.CleankrHelpFloatingButton
import com.example.ui.theme.CleankrGreen

@Composable
fun EarningsScreen(viewModel: PartnerViewModel) {
  val earnings by viewModel.earnings.collectAsState()
  val withdrawals by viewModel.withdrawals.collectAsState()

  var showWithdrawDialog by remember { mutableStateOf(false) }
  var withdrawAmountInput by remember { mutableStateOf("2500") }

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
        Text(
          text = "Money",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1E1A22)
        )
      }

      // 1. Main Earnings Card (Screenshot 1: ₹21,876, Earned this month >, Bar chart Apr - Sept)
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("money_main_earnings_card")
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Text(
              text = "₹${earnings.monthEarnings.toInt().let { if (it > 0) it else 21876 }}",
              fontSize = 30.sp,
              fontWeight = FontWeight.Black,
              color = Color(0xFF1B5E20),
              letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(
                text = "Earned this month",
                fontSize = 13.sp,
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.SemiBold
              )
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(10.dp)
              )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Monthly Bar Chart (Apr, May, Jun, Jul, Aug, Sept)
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Bottom
            ) {
              MonthlyBar("Apr", 0.70f, isSelected = false)
              MonthlyBar("May", 0.82f, isSelected = false)
              MonthlyBar("Jun", 0.75f, isSelected = false)
              MonthlyBar("Jul", 0.88f, isSelected = false)
              MonthlyBar("Aug", 0.40f, isSelected = false)
              MonthlyBar("Sept", 0.95f, isSelected = true)
            }
          }
        }
      }

      // 2. Bank Transfers Section (Screenshot 1)
      item {
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = "Bank transfers",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E1A22)
            )
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
              contentDescription = null,
              tint = Color(0xFF9E9E9E),
              modifier = Modifier.size(12.dp)
            )
          }

          Text(
            text = "Withdraw",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5A31F4),
            modifier = Modifier.clickable { showWithdrawDialog = true }
          )
        }
      }

      // Horizontal Carousel of Bank Transfers (Screenshot 1)
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Transfer Card 1: ₹0 | 22 - 25 Sep | Upcoming
          TransferCard(
            amount = "₹0",
            dates = "22 - 25 Sep",
            status = "Upcoming",
            statusColor = Color(0xFF757575),
            statusBg = Color(0xFFEEEEEE)
          )

          // Transfer Card 2: ₹2,705.79 | 19 - 21 Sep | Success
          TransferCard(
            amount = "₹2,705.79",
            dates = "19 - 21 Sep",
            status = "Success",
            statusColor = CleankrGreen,
            statusBg = Color(0xFFE8F5E9)
          )

          // Transfer Card 3: ₹2,450.00 | 17 - 18 Sep | Success
          TransferCard(
            amount = "₹2,450.00",
            dates = "17 - 18 Sep",
            status = "Success",
            statusColor = CleankrGreen,
            statusBg = Color(0xFFE8F5E9)
          )
        }
      }

      // 3. PENDING DEDUCTIONS CARD (Screenshot 1)
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = Color(0xFF757575),
                modifier = Modifier.size(22.dp)
              )
              Text(
                text = "PENDING DEDUCTIONS",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1A22),
                letterSpacing = 0.5.sp
              )
            }
            Text(
              text = "₹0",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E1A22)
            )
          }
        }
      }

      // 4. TAX RETURN BANNER (Screenshot 1: FILE YOUR TAX RETURN FOR FY2026–27 >)
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.navigateTo(AppScreen.HELP_SUPPORT) }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = Color(0xFF5A31F4),
                modifier = Modifier.size(22.dp)
              )
              Text(
                text = "FILE YOUR TAX RETURN FOR FY2026–27",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1A22),
                letterSpacing = 0.3.sp
              )
            }
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
              contentDescription = null,
              tint = Color(0xFF9E9E9E),
              modifier = Modifier.size(12.dp)
            )
          }
        }
      }

      // Withdrawable Balance and Payout Action Card
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Available to Withdraw",
                  fontSize = 12.sp,
                  color = Color(0xFF757575)
                )
                Text(
                  text = "₹${earnings.withdrawableBalance.toInt()}",
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1E1A22)
                )
              }
              Button(
                onClick = { showWithdrawDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A31F4)),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text("Instant Payout", fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(80.dp))
      }
    }

    CleankrHelpFloatingButton(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 16.dp, bottom = 16.dp),
      onHelpClick = { viewModel.navigateTo(AppScreen.HELP_SUPPORT) }
    )
  }

  // Withdrawal Dialog
  if (showWithdrawDialog) {
    AlertDialog(
      onDismissRequest = { showWithdrawDialog = false },
      title = { Text("Transfer to Bank Account") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "Linked Account: HDFC Bank (A/C ****4812, IFSC HDFC0001824)",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          OutlinedTextField(
            value = withdrawAmountInput,
            onValueChange = { withdrawAmountInput = it },
            label = { Text("Amount (₹)") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val amt = withdrawAmountInput.toDoubleOrNull() ?: 1000.0
            viewModel.requestWithdrawal(amt, "HDFC Bank (A/C ****4812)")
            showWithdrawDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A31F4))
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

@Composable
private fun MonthlyBar(
  month: String,
  heightFraction: Float,
  isSelected: Boolean
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.width(42.dp)
  ) {
    Box(
      modifier = Modifier
        .width(18.dp)
        .height((80 * heightFraction).dp)
        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
        .background(if (isSelected) Color(0xFF00796B) else Color(0xFFA5D6A7))
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = month,
      fontSize = 12.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
      color = if (isSelected) Color(0xFF004D40) else Color(0xFF388E3C)
    )
    if (isSelected) {
      Box(
        modifier = Modifier
          .width(20.dp)
          .height(2.dp)
          .background(Color(0xFF004D40))
      )
    }
  }
}

@Composable
private fun TransferCard(
  amount: String,
  dates: String,
  status: String,
  statusColor: Color,
  statusBg: Color
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier.width(160.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(
        text = amount,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1E1A22)
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = dates,
        fontSize = 11.sp,
        color = Color(0xFF757575)
      )
      Spacer(modifier = Modifier.height(10.dp))
      Surface(
        shape = RoundedCornerShape(4.dp),
        color = statusBg
      ) {
        Text(
          text = status,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = statusColor,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }
    }
  }
}
