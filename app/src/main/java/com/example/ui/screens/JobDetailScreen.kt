package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApprovalStatus
import com.example.data.model.Job
import com.example.data.model.JobStatus
import com.example.ui.AppScreen
import com.example.ui.PartnerViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CleankrAmber
import com.example.ui.theme.CleankrAmberContainer
import com.example.ui.theme.CleankrCoral
import com.example.ui.theme.CleankrCyan
import com.example.ui.theme.CleankrGreen
import com.example.ui.theme.CleankrMagenta
import com.example.ui.theme.CleankrPeach
import com.example.ui.theme.CleankrRed
import com.example.ui.theme.CleankrViolet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
  viewModel: PartnerViewModel,
  jobId: String,
  onBack: () -> Unit
) {
  val allJobs by viewModel.allJobs.collectAsState()
  val job = allJobs.find { it.id == jobId }

  if (job == null) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("Job not found (#$jobId)")
    }
    return
  }

  // Camera Launchers for Before & After photos
  var capturedBitmapsBefore by remember { mutableStateOf<List<Bitmap>>(emptyList()) }
  var capturedBitmapsAfter by remember { mutableStateOf<List<Bitmap>>(emptyList()) }

  val cameraLauncherBefore = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicturePreview()
  ) { bitmap: Bitmap? ->
    bitmap?.let {
      capturedBitmapsBefore = capturedBitmapsBefore + it
      val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
      viewModel.uploadJobPhoto(job.id, "Before Photo • $timestamp", isBefore = true)
    }
  }

  val cameraLauncherAfter = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicturePreview()
  ) { bitmap: Bitmap? ->
    bitmap?.let {
      capturedBitmapsAfter = capturedBitmapsAfter + it
      val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
      viewModel.uploadJobPhoto(job.id, "After Photo • $timestamp", isBefore = false)
    }
  }

  // Dialog states
  var showServiceChangeDialog by remember { mutableStateOf(false) }
  var serviceChangeItem by remember { mutableStateOf("Heavy Balcony Jet Wash & Degrease") }
  var serviceChangeAmount by remember { mutableStateOf("650") }
  var serviceChangeReason by remember { mutableStateOf("Excessive pigeon droppings & oil buildup requiring industrial chemicals") }

  var showCancelDialog by remember { mutableStateOf(false) }
  var cancellationReason by remember { mutableStateOf("Customer unavailable / Door locked after 3 calls") }

  var showRescheduleDialog by remember { mutableStateOf(false) }
  var rescheduleDate by remember { mutableStateOf("Tomorrow 10:00 AM") }

  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Job #${job.id}", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("job_detail_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          StatusBadge(status = job.status, modifier = Modifier.padding(end = 12.dp))
        }
      )
    },
    bottomBar = {
      // Primary Action Bar based on Lifecycle
      Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          when (job.status) {
            JobStatus.ASSIGNED -> {
              Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                  onClick = { viewModel.rejectJob(job.id, "Partner unavailable") },
                  colors = ButtonDefaults.outlinedButtonColors(contentColor = CleankrRed),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.weight(1f).testTag("action_reject_button")
                ) {
                  Text("Decline")
                }
                Button(
                  onClick = { viewModel.acceptJob(job.id) },
                  colors = ButtonDefaults.buttonColors(containerColor = CleankrCoral),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.weight(1.5f).testTag("action_accept_button")
                ) {
                  Text("Accept Booking", fontWeight = FontWeight.Bold)
                }
              }
            }
            JobStatus.ACCEPTED -> {
              Button(
                onClick = { viewModel.advanceJobStatus(job.id, job.status) },
                colors = ButtonDefaults.buttonColors(containerColor = CleankrViolet),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("action_on_the_way_button")
              ) {
                Icon(Icons.Default.Directions, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Trip (On the Way)", fontWeight = FontWeight.Bold)
              }
            }
            JobStatus.ON_THE_WAY -> {
              Button(
                onClick = { viewModel.advanceJobStatus(job.id, job.status) },
                colors = ButtonDefaults.buttonColors(containerColor = CleankrCoral),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("action_arrived_button")
              ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("I Have Arrived at Location", fontWeight = FontWeight.Bold)
              }
            }
            JobStatus.ARRIVED -> {
              Button(
                onClick = { viewModel.advanceJobStatus(job.id, job.status) },
                colors = ButtonDefaults.buttonColors(containerColor = CleankrMagenta),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("action_start_job_button")
              ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Cleaning Service", fontWeight = FontWeight.Bold)
              }
            }
            JobStatus.STARTED -> {
              Button(
                onClick = { viewModel.advanceJobStatus(job.id, job.status) },
                colors = ButtonDefaults.buttonColors(containerColor = CleankrGreen),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("action_complete_job_button")
              ) {
                Icon(Icons.Default.Verified, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Complete Service & Collect Payment", fontWeight = FontWeight.Bold)
              }
            }
            JobStatus.COMPLETED -> {
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = CleankrGreen.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
              ) {
                Text(
                  text = "✓ Job Completed • ₹${job.estimatedEarnings.toInt()} credited to Partner Wallet",
                  color = CleankrGreen,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  modifier = Modifier.padding(12.dp)
                )
              }
            }
            JobStatus.CANCELLED -> {
              Text(
                text = "Job Cancelled: ${job.cancellationReason ?: "No reason"}",
                color = CleankrRed,
                fontWeight = FontWeight.Bold
              )
            }
            JobStatus.RESCHEDULED -> {
              Text(
                text = "Job Rescheduled to: ${job.rescheduledDate ?: "Pending slot"}",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp)
        .verticalScroll(scrollState),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Service & Pricing Header Card
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = job.serviceTitle,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = job.packageType,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Slot & Date", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("${job.date} • ${job.timeSlot}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text("Your Earnings", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("₹${job.estimatedEarnings.toInt()}", fontSize = 20.sp, fontWeight = FontWeight.Black, color = CleankrGreen)
              Text("Company Price: ₹${job.companyPrice.toInt()}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
          ) {
            Text(
              text = "Payment Mode: " + job.paymentMode,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }

      // 2. Customer Contact & Masked Telephony Relay
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Customer", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(job.customerName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = CleankrCyan.copy(alpha = 0.15f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(Icons.Default.Security, contentDescription = null, tint = CleankrCyan, modifier = Modifier.size(14.dp))
                Text("Masked Telephony Relay", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CleankrCyan)
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Virtual Relay ID: ${job.customerPhoneMasked}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "Personal phone numbers are strictly protected and never displayed or stored.",
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 10.dp)
          )

          Button(
            onClick = { viewModel.startMaskedCall(job) },
            colors = ButtonDefaults.buttonColors(containerColor = CleankrMagenta),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().testTag("job_detail_masked_call_button")
          ) {
            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Call Customer via Secure Relay", fontWeight = FontWeight.Bold)
          }
        }
      }

      // 3. Address & Navigation (Minimum tracking on-demand)
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Service Location", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(
            text = job.address,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 4.dp)
          )

          if (job.instructions.isNotEmpty()) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = CleankrAmberContainer,
              modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = CleankrAmber, modifier = Modifier.size(18.dp))
                Text(
                  text = "Special Instructions: " + job.instructions,
                  fontSize = 12.sp,
                  color = Color.Black
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Button(
            onClick = { viewModel.navigateToCustomer(job) },
            colors = ButtonDefaults.buttonColors(containerColor = CleankrCyan),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().testTag("job_detail_navigate_button")
          ) {
            Icon(Icons.Default.Directions, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Turn-by-Turn GPS Navigation", fontWeight = FontWeight.Bold, color = Color.Black)
          }
          Text(
            text = "GPS is queried on-demand only for navigation. No background tracking.",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
          )
        }
      }

      // 4. Before & After Photo Proof
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Service Photos & Verification Proof",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Capture timestamped photos before starting and after finishing the service.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Before Photo Button
            OutlinedButton(
              onClick = { cameraLauncherBefore.launch(null) },
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f).testTag("capture_before_photo_button")
            ) {
              Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Before (${job.beforePhotos.size})", fontSize = 12.sp)
            }

            // After Photo Button
            OutlinedButton(
              onClick = { cameraLauncherAfter.launch(null) },
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f).testTag("capture_after_photo_button")
            ) {
              Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("After (${job.afterPhotos.size})", fontSize = 12.sp)
            }
          }

          if (capturedBitmapsBefore.isNotEmpty() || capturedBitmapsAfter.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(capturedBitmapsBefore) { bm ->
                Image(
                  bitmap = bm.asImageBitmap(),
                  contentDescription = "Before Photo",
                  modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, CleankrCoral, RoundedCornerShape(8.dp))
                )
              }
              items(capturedBitmapsAfter) { bm ->
                Image(
                  bitmap = bm.asImageBitmap(),
                  contentDescription = "After Photo",
                  modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, CleankrGreen, RoundedCornerShape(8.dp))
                )
              }
            }
          }
        }
      }

      // 5. Service Change Request (Admin Approval Required)
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Service Scope Adjustment",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = CleankrAmber.copy(alpha = 0.15f)
            ) {
              Text(
                text = "Admin Approval Required",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = CleankrAmber,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Text(
            text = "Partners cannot directly modify company pricing. Any on-site scope change requires Operations Admin approval.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 6.dp)
          )

          // Existing Service Change Requests
          job.serviceChangeRequests.forEach { req ->
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(req.serviceItem, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  Text("+₹${req.requestedAmount.toInt()}", fontWeight = FontWeight.Black, color = CleankrGreen, fontSize = 12.sp)
                }
                Text("Reason: ${req.partnerReason}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(
                  modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "Status: ${req.status.name.replace("_", " ")}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (req.status == ApprovalStatus.APPROVED_BY_ADMIN) CleankrGreen else CleankrAmber
                  )

                  if (req.status == ApprovalStatus.PENDING_APPROVAL) {
                    TextButton(
                      onClick = { viewModel.simulateAdminApprove(job.id, req.id) }
                    ) {
                      Text("Simulate Admin Approval", fontSize = 10.sp, color = CleankrCyan)
                    }
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Button(
            onClick = { showServiceChangeDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = CleankrPeach),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().testTag("request_service_change_button")
          ) {
            Icon(Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Request Additional Service / Scope Change", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      // 6. Reschedule / Cancellation options
      if (job.status != JobStatus.COMPLETED && job.status != JobStatus.CANCELLED) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = { showRescheduleDialog = true },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f).testTag("reschedule_job_button")
          ) {
            Text("Reschedule", fontSize = 12.sp)
          }
          OutlinedButton(
            onClick = { showCancelDialog = true },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = CleankrRed),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f).testTag("cancel_job_button")
          ) {
            Text("Cancel Job", fontSize = 12.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(36.dp))
    }
  }

  // Service Change Request Dialog
  if (showServiceChangeDialog) {
    AlertDialog(
      onDismissRequest = { showServiceChangeDialog = false },
      title = { Text("Request Service Scope Adjustment") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "Select or enter additional service discovered on-site. Admin approval required.",
            fontSize = 12.sp
          )
          OutlinedTextField(
            value = serviceChangeItem,
            onValueChange = { serviceChangeItem = it },
            label = { Text("Additional Service Item") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = serviceChangeAmount,
            onValueChange = { serviceChangeAmount = it },
            label = { Text("Customer Extra Cost (₹)") },
            prefix = { Text("₹") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = serviceChangeReason,
            onValueChange = { serviceChangeReason = it },
            label = { Text("Justification for Admin") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val amount = serviceChangeAmount.toDoubleOrNull() ?: 0.0
            viewModel.submitServiceChange(job.id, serviceChangeItem, amount, serviceChangeReason)
            showServiceChangeDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = CleankrMagenta)
        ) {
          Text("Submit to Admin")
        }
      },
      dismissButton = {
        TextButton(onClick = { showServiceChangeDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  // Cancel Job Dialog
  if (showCancelDialog) {
    AlertDialog(
      onDismissRequest = { showCancelDialog = false },
      title = { Text("Cancel Active Job") },
      text = {
        Column {
          Text("Customer will be notified and this cancellation will be recorded in your partner audit logs.")
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = cancellationReason,
            onValueChange = { cancellationReason = it },
            label = { Text("Cancellation Reason") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.cancelJob(job.id, cancellationReason)
            showCancelDialog = false
            onBack()
          },
          colors = ButtonDefaults.buttonColors(containerColor = CleankrRed)
        ) {
          Text("Confirm Cancellation")
        }
      },
      dismissButton = {
        TextButton(onClick = { showCancelDialog = false }) {
          Text("Back")
        }
      }
    )
  }

  // Reschedule Dialog
  if (showRescheduleDialog) {
    AlertDialog(
      onDismissRequest = { showRescheduleDialog = false },
      title = { Text("Reschedule Booking") },
      text = {
        Column {
          Text("Enter proposed reschedule date & slot agreed with customer:")
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = rescheduleDate,
            onValueChange = { rescheduleDate = it },
            label = { Text("New Date & Time") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.rescheduleJob(job.id, rescheduleDate)
            showRescheduleDialog = false
            onBack()
          },
          colors = ButtonDefaults.buttonColors(containerColor = CleankrCoral)
        ) {
          Text("Reschedule")
        }
      },
      dismissButton = {
        TextButton(onClick = { showRescheduleDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
