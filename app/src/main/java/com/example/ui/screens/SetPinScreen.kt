package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.CleankrMonogram
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrCyan
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrRed
import com.example.ui.theme.CleankrViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetPinScreen(viewModel: PartnerViewModel) {
  val profile by viewModel.profile.collectAsState()
  val pinInput by viewModel.pinInput.collectAsState()
  val confirmPinInput by viewModel.confirmPinInput.collectAsState()
  val errorMsg by viewModel.authErrorMessage.collectAsState()

  // Track which input is active: 0 = new PIN, 1 = confirm PIN
  var activeStep by remember { mutableStateOf(0) }
  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Setup Security PIN",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        },
        navigationIcon = {
          IconButton(
            onClick = { viewModel.navigateTo(AppScreen.AUTH) },
            modifier = Modifier.testTag("set_pin_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(scrollState)
        .padding(20.dp),
      contentAlignment = Alignment.TopCenter
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Monogram Brand Icon
        CleankrMonogram(sizeDp = 68.dp)
        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Instant PIN Access",
          fontSize = 22.sp,
          fontWeight = FontWeight.Black,
          color = MaterialTheme.colorScheme.onBackground
        )

        Text(
          text = "Set a 4-digit PIN to instantly unlock your partner portal without waiting for SMS OTP every time.",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Partner Info Card
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(CleankrViolet.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Lock, contentDescription = null, tint = CleankrViolet, modifier = Modifier.size(20.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
              Text(profile.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Text("Mobile: ${profile.phoneMasked}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = CleankrGreen.copy(alpha = 0.12f)
            ) {
              Text(
                text = "OTP Verified",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = CleankrGreen,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step Tabs
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (activeStep == 0) CleankrCoral.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
            border = if (activeStep == 0) androidx.compose.foundation.BorderStroke(1.5.dp, CleankrCoral) else null,
            modifier = Modifier
              .weight(1f)
              .clickable { activeStep = 0 }
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text("Step 1", fontSize = 10.sp, color = if (activeStep == 0) CleankrCoral else Color.Gray)
              Text("Enter 4-Digit PIN", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Spacer(modifier = Modifier.height(6.dp))
              PinDotsDisplay(pin = pinInput, isFocused = activeStep == 0)
            }
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (activeStep == 1) CleankrMagenta.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
            border = if (activeStep == 1) androidx.compose.foundation.BorderStroke(1.5.dp, CleankrMagenta) else null,
            modifier = Modifier
              .weight(1f)
              .clickable { activeStep = 1 }
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text("Step 2", fontSize = 10.sp, color = if (activeStep == 1) CleankrMagenta else Color.Gray)
              Text("Confirm PIN", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Spacer(modifier = Modifier.height(6.dp))
              PinDotsDisplay(pin = confirmPinInput, isFocused = activeStep == 1)
            }
          }
        }

        // Error Banner
        AnimatedVisibility(visible = errorMsg != null) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = CleankrRed.copy(alpha = 0.12f),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 10.dp)
          ) {
            Text(
              text = errorMsg ?: "",
              color = CleankrRed,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(10.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Custom Numeric On-Screen Keypad for quick single-handed PIN entry
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          val currentTarget = if (activeStep == 0) pinInput else confirmPinInput
          val onDigitClick: (String) -> Unit = { digit ->
            if (currentTarget.length < 4) {
              val updated = currentTarget + digit
              if (activeStep == 0) {
                viewModel.updatePinInput(updated)
                if (updated.length == 4) {
                  activeStep = 1 // Auto-advance to Confirm PIN step
                }
              } else {
                viewModel.updateConfirmPinInput(updated)
              }
            }
          }
          val onDeleteClick: () -> Unit = {
            if (currentTarget.isNotEmpty()) {
              val updated = currentTarget.dropLast(1)
              if (activeStep == 0) {
                viewModel.updatePinInput(updated)
              } else {
                viewModel.updateConfirmPinInput(updated)
              }
            } else if (activeStep == 1) {
              activeStep = 0
            }
          }

          val rows = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("", "0", "DEL")
          )

          rows.forEach { row ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly
            ) {
              row.forEach { item ->
                when (item) {
                  "" -> Spacer(modifier = Modifier.size(68.dp))
                  "DEL" -> {
                    Box(
                      modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .clickable { onDeleteClick() }
                        .testTag("pin_keypad_delete"),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(Icons.Default.Backspace, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurface)
                    }
                  }
                  else -> {
                    Box(
                      modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), CircleShape)
                        .clickable { onDigitClick(item) }
                        .testTag("pin_keypad_$item"),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = item,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                      )
                    }
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Save & Activate PIN Button
        Button(
          onClick = { viewModel.setupNewPin() },
          enabled = pinInput.length == 4 && confirmPinInput.length == 4,
          colors = ButtonDefaults.buttonColors(containerColor = CleankrCoral),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("save_activate_pin_button")
        ) {
          Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Activate Instant Security PIN", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Skip Button
        TextButton(
          onClick = {
            viewModel.navigateTo(AppScreen.DASHBOARD)
          },
          modifier = Modifier.testTag("skip_pin_button")
        ) {
          Text("Skip for Now (Continue via OTP)", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Security Notice
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.Shield, contentDescription = null, tint = CleankrCyan, modifier = Modifier.size(18.dp))
            Text(
              text = "Secured with SHA-256 local salted encryption. No plaintext storage.",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}

@Composable
fun PinDotsDisplay(pin: String, isFocused: Boolean) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    repeat(4) { index ->
      val isFilled = index < pin.length
      Box(
        modifier = Modifier
          .size(14.dp)
          .clip(CircleShape)
          .background(
            if (isFilled) {
              if (isFocused) CleankrCoral else CleankrMagenta
            } else {
              Color.LightGray.copy(alpha = 0.4f)
            }
          )
      )
    }
  }
}
