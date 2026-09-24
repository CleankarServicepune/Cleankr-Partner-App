package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CleankrDarkColorScheme = darkColorScheme(
  primary = CleankrCoral,
  onPrimary = Color.White,
  primaryContainer = CleankrMagentaDark,
  onPrimaryContainer = Color.White,
  secondary = CleankrMagenta,
  onSecondary = Color.White,
  secondaryContainer = DarkSurfaceVariant,
  onSecondaryContainer = CleankrPeach,
  tertiary = CleankrViolet,
  onTertiary = Color.White,
  background = DarkBackground,
  onBackground = Color(0xFFEDE7F2),
  surface = DarkSurface,
  onSurface = Color(0xFFEDE7F2),
  surfaceVariant = DarkSurfaceElevated,
  onSurfaceVariant = Color(0xFFC7BED2),
  outline = DarkBorder,
  error = CleankrRed,
  onError = Color.White
)

private val CleankrLightColorScheme = lightColorScheme(
  primary = CleankrMagenta,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFFCE4EC),
  onPrimaryContainer = CleankrMagentaDark,
  secondary = CleankrCoral,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFFFEBE5),
  onSecondaryContainer = CleankrCoralDark,
  tertiary = CleankrViolet,
  onTertiary = Color.White,
  background = LightBackground,
  onBackground = Color(0xFF1E1A22),
  surface = LightSurface,
  onSurface = Color(0xFF1E1A22),
  surfaceVariant = LightSurfaceElevated,
  onSurfaceVariant = Color(0xFF554E5D),
  outline = LightBorder,
  error = CleankrRed,
  onError = Color.White
)

@Composable
fun CleankrPartnerTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use intentional brand colors
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> CleankrDarkColorScheme
    else -> CleankrLightColorScheme
  }

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      window?.let {
        WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = !darkTheme
      }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
