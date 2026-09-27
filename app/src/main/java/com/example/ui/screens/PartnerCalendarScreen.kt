package com.example.ui.screens

import androidx.compose.runtime.Composable
import com.example.ui.PartnerViewModel

/**
 * PartnerCalendarScreen - Clean wrapper / alias for the integrated CalendarScreen.
 * Provides consistent access to partner availability, working shift slots,
 * and leave requests under the expected naming convention.
 */
@Composable
fun PartnerCalendarScreen(viewModel: PartnerViewModel) {
  CalendarScreen(viewModel = viewModel)
}
