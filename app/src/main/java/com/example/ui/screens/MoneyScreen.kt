package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.BottomNavBarCleankr
import com.example.ui.components.CleankrTopHeader

@Composable
fun MoneyScreen(
  viewModel: PartnerViewModel,
  onOpenDrawer: () -> Unit
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  var showInstantPayoutDialog by remember { mutableStateOf(false) }
  var showTaxDialog by remember { mutableStateOf(false) }
  var showHelpDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      CleankrTopHeader(
        onMenuClick = onOpenDrawer,
        onEmergencyClick = { viewModel.navigateTo(AppScreen.SECURITY_CENTER) },
        onNavigate = { screen -> viewModel.navigateTo(screen) }
      )
    },
    bottomBar = {
      BottomNavBarCleankr(
        currentScreen = currentScreen,
        onTabSelect = { screen -> viewModel.navigateTo(screen) }
      )
    },
    floatingActionButton = {
      Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF1E1E1E),
        shadowElevation = 4.dp,
        modifier = Modifier
          .height(44.dp)
          .clickable { showHelpDialog = true }
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.HelpOutline,
            contentDescription = "Help",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "Help",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFBFBFB))
        .padding(padding)
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Earned this month card with Bar Chart
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Text(
              text = "Earned this month >",
              fontSize = 14.sp,
              fontWeight = FontWeight.Black,
              fontStyle = FontStyle.Italic,
              color = Color(0xFF1B5E20)
            )

            Text(
              text = "₹16,240",
              fontSize = 28.sp,
              fontWeight = FontWeight.Black,
              color = Color(0xFF1E1E1E)
            )

            // Bar Chart row
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Bottom
            ) {
              BarColumn(month = "Apr", heightDp = 48, isHighlight = false)
              BarColumn(month = "May", heightDp = 62, isHighlight = false)
              BarColumn(month = "Jun", heightDp = 54, isHighlight = false)
              BarColumn(month = "Jul", heightDp = 70, isHighlight = false)
              BarColumn(month = "Aug", heightDp = 38, isHighlight = false)
              BarColumn(month = "Sept", heightDp = 80, isHighlight = true)
            }
          }
        }
      }

      // 2. Bank transfers section
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Bank transfers >",
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF1E1E1E)
          )

          Text(
            text = "Withdraw",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF1A237E),
            modifier = Modifier.clickable { showInstantPayoutDialog = true }
          )
        }
      }

      // Horizontal transfer cards
      item {
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          item {
            BankTransferCard(
              amount = "₹0",
              dateRange = "22 - 25 Sep",
              status = "Upcoming",
              statusBg = Color(0xFFF5F5F5),
              statusTextColor = Color(0xFF616161)
            )
          }

          item {
            BankTransferCard(
              amount = "₹2,705.79",
              dateRange = "19 - 21 Sep",
              status = "Success",
              statusBg = Color(0xFFE8F5E9),
              statusTextColor = Color(0xFF2E7D32)
            )
          }

          item {
            BankTransferCard(
              amount = "₹3,410.00",
              dateRange = "15 - 18 Sep",
              status = "Success",
              statusBg = Color(0xFFE8F5E9),
              statusTextColor = Color(0xFF2E7D32)
            )
          }
        }
      }

      // 3. PENDING DEDUCTIONS
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = Color(0xFF757575),
                modifier = Modifier.size(20.dp)
              )
              Text(
                text = "PENDING DEDUCTIONS",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF1E1E1E)
              )
            }

            Text(
              text = "₹0",
              fontSize = 16.sp,
              fontWeight = FontWeight.Black,
              color = Color(0xFF1E1E1E)
            )
          }
        }
      }

      // 4. FILE YOUR TAX RETURN
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showTaxDialog = true }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = Color(0xFF5A31F4),
                modifier = Modifier.size(22.dp)
              )
              Text(
                text = "FILE YOUR TAX RETURN FOR FY2026-27",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF1E1E1E)
              )
            }

            Icon(
              imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
              contentDescription = null,
              tint = Color(0xFF9E9E9E),
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      // 5. Available to Withdraw card
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                  text = "Available to Withdraw",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  fontStyle = FontStyle.Italic,
                  color = Color(0xFF616161)
                )
                Text(
                  text = "₹8650",
                  fontSize = 26.sp,
                  fontWeight = FontWeight.Black,
                  color = Color(0xFF1E1E1E)
                )
              }

              Button(
                onClick = { showInstantPayoutDialog = true },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
              ) {
                Text(
                  text = "Instant Payout",
                  fontWeight = FontWeight.Black,
                  fontStyle = FontStyle.Italic,
                  fontSize = 13.sp,
                  color = Color.White
                )
              }
            }

            // Bank details subcard
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFFFAFAFA),
              border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = null,
                    tint = Color(0xFF4A148C),
                    modifier = Modifier.size(24.dp)
                  )

                  Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                      text = "HDFC Bank • •••••••4921",
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Black,
                      fontStyle = FontStyle.Italic,
                      color = Color(0xFF1E1E1E)
                    )
                    Text(
                      text = "IFSC: HDFC0001243 | UPI: sunil.clean@upi",
                      fontSize = 11.sp,
                      fontStyle = FontStyle.Italic,
                      color = Color(0xFF757575)
                    )
                  }
                }

                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFFE8F5E9)
                ) {
                  Text(
                    text = "VERIFIED",
                    color = Color(0xFF1B5E20),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  if (showInstantPayoutDialog) {
    AlertDialog(
      onDismissRequest = { showInstantPayoutDialog = false },
      title = { Text("Instant Bank Transfer", fontWeight = FontWeight.Bold) },
      text = {
        Text("Payout of ₹8,650 will be sent via IMPS to HDFC Bank (••••4921). Zero deduction fees for registered certified partners.")
      },
      confirmButton = {
        Button(
          onClick = { showInstantPayoutDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
        ) {
          Text("Confirm Transfer")
        }
      },
      dismissButton = {
        TextButton(onClick = { showInstantPayoutDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  if (showTaxDialog) {
    AlertDialog(
      onDismissRequest = { showTaxDialog = false },
      title = { Text("Tax Statement & TDS Form 16A", fontWeight = FontWeight.Bold) },
      text = {
        Text("Annual TDS certificate for FY2026-27 is ready for download. Cleankr complies with Section 194C of the Indian Income Tax Act.")
      },
      confirmButton = {
        TextButton(onClick = { showTaxDialog = false }) {
          Text("Download Summary")
        }
      }
    )
  }

  if (showHelpDialog) {
    AlertDialog(
      onDismissRequest = { showHelpDialog = false },
      title = { Text("Billing & Bank Support") },
      text = {
        Text("Payouts are settled every 3 days. For delayed settlements or updating UPI ID, contact partner-finance@cleankr.in.")
      },
      confirmButton = {
        TextButton(onClick = { showHelpDialog = false }) {
          Text("Close")
        }
      }
    )
  }
}

@Composable
fun BarColumn(
  month: String,
  heightDp: Int,
  isHighlight: Boolean
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    Box(
      modifier = Modifier
        .width(18.dp)
        .height(heightDp.dp)
        .background(
          color = if (isHighlight) Color(0xFF004D40) else Color(0xFFA5D6A7),
          shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
        )
    )

    Text(
      text = month,
      fontSize = 12.sp,
      fontWeight = if (isHighlight) FontWeight.Black else FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      color = if (isHighlight) Color(0xFF004D40) else Color(0xFF616161)
    )

    if (isHighlight) {
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
fun BankTransferCard(
  amount: String,
  dateRange: String,
  status: String,
  statusBg: Color,
  statusTextColor: Color
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    modifier = Modifier.width(150.dp)
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = amount,
        fontSize = 18.sp,
        fontWeight = FontWeight.Black,
        color = Color(0xFF1E1E1E)
      )

      Text(
        text = dateRange,
        fontSize = 12.sp,
        fontStyle = FontStyle.Italic,
        color = Color(0xFF616161)
      )

      Surface(
        shape = RoundedCornerShape(6.dp),
        color = statusBg
      ) {
        Text(
          text = status,
          color = statusTextColor,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
      }
    }
  }
}
