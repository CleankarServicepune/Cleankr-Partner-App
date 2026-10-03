package com.example.notifications

import android.content.Context
import android.os.Build
import android.util.Log
import com.example.data.firebase.CleankrFirebaseConfig
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CleankrMessagingService : FirebaseMessagingService() {

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    Log.d(TAG, "New FCM Token received: $token")
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)
    Log.d(TAG, "From: ${remoteMessage.from}")
  }

  companion object {
    private const val TAG = "CleankrFCM"

    fun fetchTokenSafely(context: Context, onTokenReceived: ((String) -> Unit)? = null) {
      try {
        val isEmulator = Build.FINGERPRINT.startsWith("generic") ||
          Build.FINGERPRINT.startsWith("unknown") ||
          Build.MODEL.contains("google_sdk") ||
          Build.MODEL.contains("Emulator") ||
          Build.MODEL.contains("Android SDK built for x86") ||
          Build.MANUFACTURER.contains("Genymotion") ||
          (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
          "google_sdk" == Build.PRODUCT

        if (isEmulator) {
          Log.i(TAG, "Running in streaming/cloud emulator. Skipping remote FCM registration to prevent hard failure exceptions.")
          onTokenReceived?.invoke("fcm_token_cloud_emulator_demo")
          return
        }

        try {
          val gmsClass = Class.forName("com.google.android.gms.common.GoogleApiAvailability")
          val getInstanceMethod = gmsClass.getMethod("getInstance")
          val gmsInstance = getInstanceMethod.invoke(null)
          val isAvailableMethod = gmsClass.getMethod("isGooglePlayServicesAvailable", Context::class.java)
          val resultCode = isAvailableMethod.invoke(gmsInstance, context) as Int
          if (resultCode != 0) {
            Log.i(TAG, "Google Play Services not available (code: $resultCode). FCM deferred.")
            return
          }
        } catch (ignored: Throwable) {}

        val messaging = CleankrFirebaseConfig.getMessaging() ?: return
        messaging.token
          .addOnCompleteListener { task ->
            if (task.isSuccessful) {
              val token = task.result
              Log.d(TAG, "FCM Token retrieved: $token")
              onTokenReceived?.invoke(token)
            }
          }
      } catch (e: Throwable) {
        Log.w(TAG, "Could not fetch FCM token safely: ${e.message}")
      }
    }
  }
}
