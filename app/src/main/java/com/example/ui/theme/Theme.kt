package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AgPrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = AgContainerGreen,
    onPrimaryContainer = AgDarkGreen,
    secondary = AgLeafGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD7EED9),
    onSecondaryContainer = AgDarkGreen,
    tertiary = AgGoldAccent,
    onTertiary = Color.White,
    background = AgBackground, // Light Green background
    onBackground = AgTextPrimary,
    surface = AgSurface,
    onSurface = AgTextPrimary,
    surfaceVariant = AgSurfaceVariant,
    onSurfaceVariant = AgTextSecondary,
    outline = AgOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = AgSproutGreen,
    onPrimary = AgDarkGreen,
    primaryContainer = Color(0xFF153320),
    onPrimaryContainer = AgLightSprout,
    secondary = AgLightSprout,
    onSecondary = AgDarkGreen,
    tertiary = AgWarningOrange,
    background = AgBackgroundDark,
    onBackground = Color(0xFFE2E8E3),
    surface = AgSurfaceDark,
    onSurface = Color(0xFFE2E8E3),
    surfaceVariant = AgSurfaceVariantDark,
    onSurfaceVariant = Color(0xFFB0BEB3),
    outline = Color(0xFF384E3F)
)

@Composable
fun PrithviTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

