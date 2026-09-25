package com.example.data.firebase

import android.content.Context
import android.util.Log
import com.example.data.model.ApprovalStatus
import com.example.data.model.Job
import com.example.data.model.JobStatus
import com.example.data.model.ServiceChangeRequest
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

/**
 * Service bridge between Cleankr Partner App and Firebase Project cleankr-724ce.
 * Enforces server-authoritative transitions, Partner role restrictions, and audit logs.
 */
class CleankrFirebaseService(private val context: Context) {

  private val tag = "CleankrFirebase"

  fun isAvailable(): Boolean {
    return CleankrFirebaseConfig.checkStatus(context) == CleankrFirebaseConfig.FirebaseConfigStatus.CONNECTED_AND_VERIFIED
  }

  /**
   * Listen to assigned bookings for this partner in real-time from Firestore.
   */
  fun listenToPartnerBookings(
    partnerId: String,
    onBookingsChanged: (List<Job>) -> Unit,
    onError: (Exception) -> Unit
  ): ListenerRegistration? {
    val firestore = CleankrFirebaseConfig.getFirestore() ?: return null

    return firestore.collection(CleankrFirebaseConfig.Collections.BOOKINGS)
      .whereEqualTo(CleankrFirebaseConfig.Fields.PARTNER_ID, partnerId)
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          Log.w(tag, "Error listening to bookings: ${error.message}")
          onError(error)
          return@addSnapshotListener
        }

        if (snapshot != null) {
          val jobs = snapshot.documents.mapNotNull { doc ->
            try {
              val data = doc.data ?: return@mapNotNull null
              val id = doc.id
              val customerName = data["customerName"] as? String ?: "Customer"
              val customerPhoneMasked = data["customerPhoneMasked"] as? String ?: "+91 1800 253 2657"
              val serviceTitle = data["serviceTitle"] as? String ?: "Home Cleaning Service"
              val packageType = data["packageType"] as? String ?: "Standard Package"
              val date = data["date"] as? String ?: ""
              val timeSlot = data["timeSlot"] as? String ?: ""
              val address = data["address"] as? String ?: ""
              val latitude = (data["latitude"] as? Number)?.toDouble() ?: 19.1363
              val longitude = (data["longitude"] as? Number)?.toDouble() ?: 72.8277
              val instructions = data["instructions"] as? String ?: ""
              val statusStr = data["status"] as? String ?: "ASSIGNED"
              val status = try { JobStatus.valueOf(statusStr) } catch (e: Exception) { JobStatus.ASSIGNED }
              val estimatedEarnings = (data["estimatedEarnings"] as? Number)?.toDouble() ?: 0.0
              val companyPrice = (data["companyPrice"] as? Number)?.toDouble() ?: 0.0
              val paymentMode = data["paymentMode"] as? String ?: "Online Prepaid"

              @Suppress("UNCHECKED_CAST")
              val beforePhotos = (data["beforePhotos"] as? List<String>) ?: emptyList()
              @Suppress("UNCHECKED_CAST")
              val afterPhotos = (data["afterPhotos"] as? List<String>) ?: emptyList()

              val cancellationReason = data["cancellationReason"] as? String
              val rescheduledDate = data["rescheduledDate"] as? String
              val assignedTimestamp = (data["assignedTimestamp"] as? Number)?.toLong() ?: System.currentTimeMillis()

              Job(
                id = id,
                customerName = customerName,
                customerPhoneMasked = customerPhoneMasked,
                serviceTitle = serviceTitle,
                packageType = packageType,
                date = date,
                timeSlot = timeSlot,
                address = address,
                latitude = latitude,
                longitude = longitude,
                instructions = instructions,
                status = status,
                estimatedEarnings = estimatedEarnings,
                companyPrice = companyPrice,
                paymentMode = paymentMode,
                beforePhotos = beforePhotos,
                afterPhotos = afterPhotos,
                cancellationReason = cancellationReason,
                rescheduledDate = rescheduledDate,
                assignedTimestamp = assignedTimestamp
              )
            } catch (e: Exception) {
              Log.e(tag, "Failed to parse booking doc ${doc.id}", e)
              null
            }
          }
          onBookingsChanged(jobs)
        }
      }
  }

  /**
   * Updates booking lifecycle state.
   * Allowed transitions for Partner:
   * ASSIGNED -> ACCEPTED -> ON_THE_WAY -> ARRIVED -> STARTED -> COMPLETED
   */
  suspend fun updateBookingStatus(
    bookingId: String,
    partnerId: String,
    newStatus: JobStatus
  ): Result<Unit> {
    val firestore = CleankrFirebaseConfig.getFirestore()
      ?: return Result.failure(IllegalStateException("Firebase is not initialized for ${CleankrFirebaseConfig.TARGET_PROJECT_ID}"))

    return try {
      val docRef = firestore.collection(CleankrFirebaseConfig.Collections.BOOKINGS).document(bookingId)
      val updates = mapOf(
        CleankrFirebaseConfig.Fields.STATUS to newStatus.name,
        CleankrFirebaseConfig.Fields.UPDATED_AT to System.currentTimeMillis()
      )
      docRef.update(updates).await()

      // Log status transition to shared audit_logs collection
      val auditRef = firestore.collection(CleankrFirebaseConfig.Collections.AUDIT_LOGS).document()
      auditRef.set(
        mapOf(
          "bookingId" to bookingId,
          "partnerId" to partnerId,
          "eventType" to "JOB_STATUS_TRANSITION",
          "newStatus" to newStatus.name,
          "timestamp" to System.currentTimeMillis()
        )
      ).await()

      Result.success(Unit)
    } catch (e: Exception) {
      Log.e(tag, "Failed to update booking status in Firestore", e)
      Result.failure(e)
    }
  }

  /**
   * Partner proposes a service change (extra item or scope addition).
   * Status is strictly initialized to PENDING_APPROVAL.
   * Official company price cannot be changed by the partner directly.
   */
  suspend fun submitServiceChangeRequest(
    bookingId: String,
    partnerId: String,
    serviceItem: String,
    requestedAmount: Double,
    partnerReason: String
  ): Result<String> {
    val firestore = CleankrFirebaseConfig.getFirestore()
      ?: return Result.failure(IllegalStateException("Firebase is not initialized for ${CleankrFirebaseConfig.TARGET_PROJECT_ID}"))

    return try {
      val changeDoc = firestore.collection(CleankrFirebaseConfig.Collections.SERVICE_CHANGES).document()
      val changeData = mapOf(
        "id" to changeDoc.id,
        "bookingId" to bookingId,
        "partnerId" to partnerId,
        "serviceItem" to serviceItem,
        "requestedAmount" to requestedAmount,
        "partnerReason" to partnerReason,
        "status" to ApprovalStatus.PENDING_APPROVAL.name,
        "adminRemark" to null,
        "createdAt" to System.currentTimeMillis()
      )
      changeDoc.set(changeData).await()
      Result.success(changeDoc.id)
    } catch (e: Exception) {
      Log.e(tag, "Failed to submit service change to Firestore", e)
      Result.failure(e)
    }
  }

  /**
   * Submits a backend-authorized account deletion request.
   */
  suspend fun submitAccountDeletionRequest(
    partnerId: String,
    reason: String
  ): Result<String> {
    val firestore = CleankrFirebaseConfig.getFirestore()
      ?: return Result.failure(IllegalStateException("Firebase not connected"))

    return try {
      val ticketRef = firestore.collection(CleankrFirebaseConfig.Collections.SUPPORT_TICKETS).document()
      val ticketData = mapOf(
        "id" to ticketRef.id,
        "partnerId" to partnerId,
        "category" to "ACCOUNT_DELETION",
        "reason" to reason,
        "status" to "PENDING_OPS_REVIEW",
        "statutoryRetentionAcknowledged" to true,
        "gracePeriodExpiry" to System.currentTimeMillis() + (30L * 24 * 3600 * 1000),
        "createdAt" to System.currentTimeMillis()
      )
      ticketRef.set(ticketData).await()
      Result.success(ticketRef.id)
    } catch (e: Exception) {
      Log.e(tag, "Failed to submit deletion request", e)
      Result.failure(e)
    }
  }
}
