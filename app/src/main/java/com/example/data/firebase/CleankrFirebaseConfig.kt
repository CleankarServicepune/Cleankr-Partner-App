package com.example.data.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage

/**
 * Authoritative Firebase Configuration & Contract for Cleankr Partner App.
 *
 * Target Firebase Project ID: cleankr-724ce
 * Shared ecosystem: Cleankr Customer App, Cleankr Partner App, and Cleankr Admin Panel.
 */
object CleankrFirebaseConfig {

  const val TARGET_PROJECT_ID = "cleankr-724ce"

  // Canonical Shared Collections
  object Collections {
    const val USERS = "users"
    const val PARTNERS = "partners"
    const val CUSTOMERS = "customers"
    const val SERVICES = "services"
    const val BOOKINGS = "bookings"
    const val SERVICE_CHANGES = "service_changes"
    const val PAYMENTS = "payments"
    const val NOTIFICATIONS = "notifications"
    const val EARNINGS = "earnings"
    const val SUPPORT_TICKETS = "support_tickets"
    const val AUDIT_LOGS = "audit_logs"
  }

  // Common Entity Fields
  object Fields {
    const val CUSTOMER_ID = "customerId"
    const val PARTNER_ID = "partnerId"
    const val BOOKING_ID = "bookingId"
    const val SERVICE_ID = "serviceId"
    const val CREATED_AT = "createdAt"
    const val UPDATED_AT = "updatedAt"
    const val STATUS = "status"
    const val ROLE = "role"
    const val FCM_TOKEN = "fcmToken"
    const val LAST_ACTIVE_AT = "lastActiveAt"
  }

  // Canonical Partner Roles
  object Roles {
    const val PARTNER = "partner"
    const val CUSTOMER = "customer"
    const val ADMIN = "admin"
  }

  enum class FirebaseConfigStatus {
    CONNECTED_AND_VERIFIED,
    MISSING_GOOGLE_SERVICES_JSON,
    MISCONFIGURED_PROJECT_ID
  }

  /**
   * Evaluates current Firebase initialization status without throwing exceptions.
   */
  fun checkStatus(context: Context): FirebaseConfigStatus {
    val apps = FirebaseApp.getApps(context)
    if (apps.isEmpty()) {
      Log.w("CleankrFirebase", "No FirebaseApp initialized. 'google-services.json' for $TARGET_PROJECT_ID is pending in /app directory.")
      return FirebaseConfigStatus.MISSING_GOOGLE_SERVICES_JSON
    }

    val currentProjectId = FirebaseApp.getInstance().options.projectId
    return if (currentProjectId == TARGET_PROJECT_ID) {
      FirebaseConfigStatus.CONNECTED_AND_VERIFIED
    } else {
      Log.w("CleankrFirebase", "Firebase initialized with '$currentProjectId', expected '$TARGET_PROJECT_ID'")
      FirebaseConfigStatus.MISCONFIGURED_PROJECT_ID
    }
  }

  fun getDetectedProjectId(context: Context): String? {
    val apps = FirebaseApp.getApps(context)
    return if (apps.isNotEmpty()) FirebaseApp.getInstance().options.projectId else null
  }

  val isConfiguredForTargetProject: Boolean
    get() {
      return try {
        val app = FirebaseApp.getInstance()
        app.options.projectId == TARGET_PROJECT_ID
      } catch (e: Exception) {
        false
      }
    }

  fun getAuth(): FirebaseAuth? {
    return if (isConfiguredForTargetProject) FirebaseAuth.getInstance() else null
  }

  fun getFirestore(): FirebaseFirestore? {
    return if (isConfiguredForTargetProject) FirebaseFirestore.getInstance() else null
  }

  fun getStorage(): FirebaseStorage? {
    return if (isConfiguredForTargetProject) FirebaseStorage.getInstance() else null
  }

  fun getMessaging(): FirebaseMessaging? {
    return if (isConfiguredForTargetProject) FirebaseMessaging.getInstance() else null
  }
}
