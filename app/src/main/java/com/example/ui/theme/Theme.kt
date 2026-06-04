package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF80CBC4), // Teal 200
    secondary = Color(0xFFB0BEC5), // Blue Grey 200
    tertiary = Color(0xFFFFCC80) // Orange 200
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Color(0xFF00796B), // Teal 700
    secondary = Color(0xFF455A64), // Blue Grey 700
    tertiary = Color(0xFFF57C00), // Orange 700
    background = Color(0xFFF5F5F5),
    onBackground = Color(0xFF212121)
  )

@Composable
fun MwingiTtcTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
