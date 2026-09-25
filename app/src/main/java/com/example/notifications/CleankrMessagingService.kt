package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.data.firebase.CleankrFirebaseConfig
import com.example.data.repository.PartnerRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CleankrMessagingService : FirebaseMessagingService() {

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    Log.d(TAG, "New FCM Token received: $token")
    syncTokenToBackend(token)
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)
    Log.d(TAG, "From: ${remoteMessage.from}")

    val data = remoteMessage.data
    val notification = remoteMessage.notification

    val title = notification?.title ?: data["title"] ?: "Cleankr Partner Notification"
    val body = notification?.body ?: data["body"] ?: "You have an update from Cleankr Operations"
    val notificationType = data["type"] ?: "GENERAL"
    val bookingId = data["bookingId"]

    // Record notification in local database/repository
    try {
      val repository = PartnerRepository(applicationContext)
      repository.addNotification(
        title = title,
        body = body,
        type = notificationType
      )
    } catch (e: Exception) {
      Log.e(TAG, "Error saving FCM notification to repository", e)
    }

    sendNotification(title, body, notificationType, bookingId)
  }

  private fun syncTokenToBackend(token: String) {
    try {
      val firestore = CleankrFirebaseConfig.getFirestore() ?: return
      val auth = CleankrFirebaseConfig.getAuth()
      val partnerId = auth?.currentUser?.uid ?: "CK-PT-9041"

      firestore.collection(CleankrFirebaseConfig.Collections.PARTNERS)
        .document(partnerId)
        .update(
          mapOf(
            CleankrFirebaseConfig.Fields.FCM_TOKEN to token,
            CleankrFirebaseConfig.Fields.UPDATED_AT to System.currentTimeMillis()
          )
        )
        .addOnSuccessListener { Log.d(TAG, "FCM token synced to Firestore for partner $partnerId") }
        .addOnFailureListener { Log.w(TAG, "Failed to sync FCM token to Firestore", it) }
    } catch (e: Exception) {
      Log.w(TAG, "Exception during token sync: ${e.message}")
    }
  }

  companion object {
    private const val TAG = "CleankrFCM"
    const val CHANNEL_DISPATCH = "cleankr_dispatch_channel"
    const val CHANNEL_EARNINGS = "cleankr_earnings_channel"
    const val CHANNEL_ALERTS = "cleankr_alerts_channel"
    const val CHANNEL_GENERAL = "cleankr_general_channel"

    /**
     * Safely request FCM token if Google Play Services and network are available.
     * Prevents unhandled exceptions on emulators without Play Services.
     */
    fun fetchTokenSafely(context: Context, onTokenReceived: ((String) -> Unit)? = null) {
      try {
        val messaging = CleankrFirebaseConfig.getMessaging() ?: return
        messaging.token
          .addOnCompleteListener { task ->
            if (!task.isSuccessful) {
              Log.w(TAG, "FCM registration token retrieval skipped/failed (Play Services/Network unavailable): ${task.exception?.message}")
              return@addOnCompleteListener
            }
            val token = task.result
            Log.d(TAG, "FCM Token retrieved successfully: $token")
            onTokenReceived?.invoke(token)
          }
      } catch (e: Exception) {
        Log.w(TAG, "Could not fetch FCM token safely: ${e.message}")
      }
    }

    /**
     * Cleans up / deactivates device push token association from partner profile upon logout or account deletion.
     */
    fun deactivateTokenOnLogout(partnerId: String) {
      try {
        val firestore = CleankrFirebaseConfig.getFirestore() ?: return
        firestore.collection(CleankrFirebaseConfig.Collections.PARTNERS)
          .document(partnerId)
          .update(
            mapOf(
              "fcmToken" to null,
              "fcmTokenDeactivatedAt" to System.currentTimeMillis()
            )
          )
          .addOnSuccessListener {
            Log.d(TAG, "FCM Token deactivated successfully for partner $partnerId")
          }
          .addOnFailureListener { e ->
            Log.w(TAG, "Failed to deactivate FCM token on logout: ${e.message}")
          }
      } catch (e: Exception) {
        Log.w(TAG, "Exception during token deactivation: ${e.message}")
      }
    }
  }

  private fun sendNotification(
    title: String,
    messageBody: String,
    type: String,
    bookingId: String?
  ) {
    val intent = Intent(this, MainActivity::class.java).apply {
      addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
      putExtra("notification_type", type)
      bookingId?.let { putExtra("booking_id", it) }
    }

    val pendingIntent = PendingIntent.getActivity(
      this,
      System.currentTimeMillis().toInt(),
      intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val channelId = when (type) {
      "NEW_JOB", "JOB_ASSIGNMENT", "JOB_ACCEPTED" -> CHANNEL_DISPATCH
      "PAYMENT", "EARNINGS", "SERVICE_CHANGE_APPROVED" -> CHANNEL_EARNINGS
      "JOB_CANCELLED", "RESCHEDULE", "ADMIN_BROADCAST" -> CHANNEL_ALERTS
      else -> CHANNEL_GENERAL
    }

    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
      .setSmallIcon(android.R.drawable.ic_dialog_info)
      .setContentTitle(title)
      .setContentText(messageBody)
      .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
      .setAutoCancel(true)
      .setSound(defaultSoundUri)
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setContentIntent(pendingIntent)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannels(notificationManager)
    }

    val notificationId = (System.currentTimeMillis() % 100000).toInt()
    notificationManager.notify(notificationId, notificationBuilder.build())
  }

  private fun createNotificationChannels(manager: NotificationManager) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channels = listOf(
        NotificationChannel(
          CHANNEL_DISPATCH,
          "Job Dispatches & Assignments",
          NotificationManager.IMPORTANCE_HIGH
        ).apply {
          description = "Urgent alerts for new bookings and immediate dispatch offers"
          enableVibration(true)
        },
        NotificationChannel(
          CHANNEL_EARNINGS,
          "Payments & Service Approvals",
          NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
          description = "Payout confirmations, earnings updates, and approved service changes"
        },
        NotificationChannel(
          CHANNEL_ALERTS,
          "Urgent Schedule & Admin Messages",
          NotificationManager.IMPORTANCE_HIGH
        ).apply {
          description = "Customer reschedules, booking cancellations, and operations notices"
        },
        NotificationChannel(
          CHANNEL_GENERAL,
          "General Announcements",
          NotificationManager.IMPORTANCE_LOW
        ).apply {
          description = "Safety guidelines, training tips, and platform updates"
        }
      )
      channels.forEach { manager.createNotificationChannel(it) }
    }
  }
}

