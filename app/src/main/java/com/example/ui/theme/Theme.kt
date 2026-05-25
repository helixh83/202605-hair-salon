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
    primary = SleekSecondary,
    secondary = SleekPrimary,
    tertiary = Purple80,
    background = Color(0xFF14121A), // Sleek deep purple dark
    surface = Color(0xFF211F26), // Elevated dark container
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    primaryContainer = SleekPrimaryContainer,
    onPrimaryContainer = SleekOnPrimaryContainer
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SleekPrimary,
    secondary = SleekSecondary,
    tertiary = SleekTertiary,
    background = SleekBackground,
    surface = SleekSurface,
    onPrimary = Color.White,
    onSecondary = SleekOnBackground,
    onBackground = SleekOnBackground,
    onSurface = SleekOnBackground,
    primaryContainer = SleekPrimaryContainer,
    onPrimaryContainer = SleekOnPrimaryContainer,
    secondaryContainer = SleekSecondaryContainer,
    onSecondaryContainer = SleekOnSecondaryContainer,
    surfaceVariant = SleekSurfaceVariant,
    onSurfaceVariant = SleekOnSurfaceVariant
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
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
