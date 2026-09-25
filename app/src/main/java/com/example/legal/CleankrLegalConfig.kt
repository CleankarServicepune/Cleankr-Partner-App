package com.example.legal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent

/**
 * Legal & Compliance Configuration for Cleankr Partner App.
 *
 * Official Contact: cleankarservice@gmail.com
 */
object CleankrLegalConfig {

  /**
   * Clearly named configuration constant for the published Cleankr Privacy Policy URL.
   * If a live hosted URL is supplied, Android Custom Tabs opens it directly.
   * Leave empty or supply the official URL when available.
   */
  const val PUBLISHED_CLEANKR_PRIVACY_POLICY_URL: String = ""

  const val OFFICIAL_SUPPORT_EMAIL: String = "cleankarservice@gmail.com"
  const val OFFICIAL_GRIEVANCE_OFFICER: String = "Cleankr Operations & Compliance Desk"
  const val JURISDICTION: String = "Mumbai / Pune, Maharashtra, India"

  /**
   * Opens the published Privacy Policy URL using Android Custom Tabs or secure fallback.
   * If the URL constant is not yet set, returns false so callers can display the in-app policy viewer.
   */
  fun openPublishedPrivacyPolicy(context: Context): Boolean {
    if (PUBLISHED_CLEANKR_PRIVACY_POLICY_URL.isBlank()) {
      return false
    }

    return try {
      val uri = Uri.parse(PUBLISHED_CLEANKR_PRIVACY_POLICY_URL)
      val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()
      customTabsIntent.launchUrl(context, uri)
      true
    } catch (e: Exception) {
      try {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(PUBLISHED_CLEANKR_PRIVACY_POLICY_URL))
        context.startActivity(browserIntent)
        true
      } catch (ex: Exception) {
        Toast.makeText(context, "Could not open browser. Please contact $OFFICIAL_SUPPORT_EMAIL", Toast.LENGTH_LONG).show()
        false
      }
    }
  }

  fun sendEmailInquiry(context: Context, subject: String) {
    try {
      val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$OFFICIAL_SUPPORT_EMAIL")
        putExtra(Intent.EXTRA_SUBJECT, subject)
      }
      context.startActivity(intent)
    } catch (e: Exception) {
      Toast.makeText(context, "Contact: $OFFICIAL_SUPPORT_EMAIL", Toast.LENGTH_LONG).show()
    }
  }
}
