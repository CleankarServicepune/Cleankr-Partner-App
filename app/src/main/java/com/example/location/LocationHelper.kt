package com.example.location

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.data.model.Job

object LocationHelper {

  /**
   * Opens native turn-by-turn navigation only when partner is actively on the way.
   * Minimal tracking: Does NOT run continuous background services or GPS polling.
   */
  fun openNavigationToCustomer(context: Context, job: Job): Boolean {
    return try {
      val uri = Uri.parse("geo:${job.latitude},${job.longitude}?q=${Uri.encode(job.address)}")
      val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
      }
      context.startActivity(mapIntent)
      true
    } catch (e: Exception) {
      // Fallback to web maps URL
      try {
        val browserIntent = Intent(
          Intent.ACTION_VIEW,
          Uri.parse("https://maps.google.com/?q=${Uri.encode(job.address)}")
        ).apply {
          flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(browserIntent)
        true
      } catch (e2: Exception) {
        false
      }
    }
  }
}
