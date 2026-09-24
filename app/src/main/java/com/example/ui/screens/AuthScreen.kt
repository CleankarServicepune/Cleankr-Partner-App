package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.PartnerViewModel
import com.example.ui.components.CleankrMonogram
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrCoralDark
import com.example.ui.theme.CleankrCyan
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrRed

@Composable
fun AuthScreen(viewModel: PartnerViewModel) {
  val phone by viewModel.phoneNumberInput.collectAsState()
  val otp by viewModel.otpInput.collectAsState()
  val isOtpSent by viewModel.isOtpSent.collectAsState()
  val timer by viewModel.otpTimerSeconds.collectAsState()
  val errorMsg by viewModel.authErrorMessage.collectAsState()
  val securityState by viewModel.securityState.collectAsState()

  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(scrollState)
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxWidth()
    ) {
      Spacer(modifier = Modifier.height(24.dp))

      // Logo & Brand Mark
      CleankrMonogram(sizeDp = 84.dp)
      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "CLEANKR",
        fontSize = 28.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 2.sp,
        color = CleankrCoral
      )
      Text(
        text = "PARTNER PORTAL",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = CleankrMagenta
      )

      Text(
        text = "Professional Home-Services & Cleaning Network",
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
      )

      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = if (!isOtpSent) "Partner Login" else "Verify One-Time Password",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = if (!isOtpSent)
              "Enter registered mobile number to receive secure OTP"
            else
              "Enter 4-digit code sent to +91 $phone",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
          )

          if (!isOtpSent) {
            OutlinedTextField(
              value = phone,
              onValueChange = { if (it.length <= 10) viewModel.updatePhoneInput(it) },
              label = { Text("Mobile Number") },
              prefix = { Text("+91 ") },
              leadingIcon = {
                Icon(Icons.Default.Phone, contentDescription = "Phone", tint = CleankrCoral)
              },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_phone_input"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CleankrCoral,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
              )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
              onClick = { viewModel.sendOtp() },
              modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("send_otp_button"),
              colors = ButtonDefaults.buttonColors(containerColor = CleankrCoral),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Get Secure OTP", fontWeight = FontWeight.Bold)
            }
          } else {
            OutlinedTextField(
              value = otp,
              onValueChange = { if (it.length <= 4) viewModel.updateOtpInput(it) },
              label = { Text("4-Digit OTP") },
              leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "OTP", tint = CleankrMagenta)
              },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_otp_input"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CleankrMagenta,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
              )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Demo Helper helper pill
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = CleankrMagenta.copy(alpha = 0.1f),
              modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
              Text(
                text = "Demo Partner OTP: 4821 (or 1234)",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = CleankrMagenta,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(6.dp)
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = { viewModel.verifyOtp() },
              modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("verify_otp_button"),
              colors = ButtonDefaults.buttonColors(containerColor = CleankrMagenta),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("Verify & Continue", fontWeight = FontWeight.Bold)
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              TextButton(
                onClick = { viewModel.sendOtp() },
                modifier = Modifier.testTag("resend_otp_button")
              ) {
                Text("Resend Code", fontSize = 12.sp, color = CleankrCoral)
              }
              TextButton(
                onClick = { viewModel.updateOtpInput("") }
              ) {
                Text("Change Mobile", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }

          AnimatedVisibility(visible = errorMsg != null) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = CleankrRed.copy(alpha = 0.12f),
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
            ) {
              Text(
                text = errorMsg ?: "",
                color = CleankrRed,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(10.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Cyber Security Badge
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            Icons.Default.Security,
            contentDescription = "Shield",
            tint = CleankrCyan,
            modifier = Modifier.size(20.dp)
          )
          Column {
            Text(
              text = "Enterprise Anti-Theft & Token Security Active",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Brute-force lockout • TLS 1.3 Pinning • Zero plaintext storage",
              fontSize = 10.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}
