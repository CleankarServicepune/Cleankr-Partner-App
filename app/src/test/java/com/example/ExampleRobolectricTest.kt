package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.Job
import com.example.data.model.JobStatus
import com.example.security.SecurityManager
import com.example.telephony.MaskedCallRelay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Cleankr Partner", appName)
  }

  @Test
  fun `masked call relay creates token and does not expose real numbers`() {
    val sampleJob = Job(
      id = "CK-TEST-01",
      customerName = "Pooja Sharma",
      customerPhoneMasked = "RELAY-EXT-4921",
      serviceTitle = "Home Deep Cleaning",
      packageType = "Premium",
      date = "2026-09-24",
      timeSlot = "10:00 AM",
      address = "Bandra West, Mumbai",
      instructions = "Ring bell twice",
      status = JobStatus.ACCEPTED,
      estimatedEarnings = 2200.0,
      companyPrice = 2800.0
    )

    val auth = MaskedCallRelay.authorizeMaskedCall(sampleJob)
    assertTrue(auth.authorized)
    assertNotNull(auth.securityToken)
    assertFalse(auth.relayDisplayNumber.contains("real"))
    assertTrue(auth.dialUriString.startsWith("tel:"))
  }

  @Test
  fun `security manager enforces rate limiting on brute force otp`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val sec = SecurityManager(context)

    // Attempt incorrect OTPs
    val (s1, _) = sec.recordOtpAttempt(false)
    assertFalse(s1)
    val (s2, _) = sec.recordOtpAttempt(false)
    assertFalse(s2)
    val (s3, _) = sec.recordOtpAttempt(false)
    assertFalse(s3)

    // After 3 failed attempts, should be rate-limited
    val (canAttempt, waitSec) = sec.canAttemptOtp()
    assertFalse(canAttempt)
    assertTrue(waitSec > 0)
  }
}
