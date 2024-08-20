package com.dhruv.angularapps.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surface = Color.DarkGray,
    onPrimary = Color(1.0f, 1.0f, 1.0f, 1.0f),
    onSecondary = Color(0.267f, 0.267f, 0.267f, 1.0f),
    primaryContainer = Color(0.0f, 0.0f, 0.0f, 1.0f),
    onPrimaryContainer = Color(0.906f, 0.906f, 0.906f, 1.0f),
    secondaryContainer = Color(0.345f, 0.345f, 0.345f, 1.0f),
    onSecondaryContainer = Color(0.702f, 0.702f, 0.702f, 1.0f),
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    surface = Color.White,    // Example light card background
    onPrimary = Color(0.0f, 0.0f, 0.0f, 1.0f),
    onSecondary = Color(0.722f, 0.722f, 0.722f, 1.0f),
    primaryContainer = Color(0.0f, 0.0f, 0.0f, 1.0f),
    onPrimaryContainer = Color(0.906f, 0.906f, 0.906f, 1.0f),
    secondaryContainer = Color(0.702f, 0.702f, 0.702f, 1.0f),
    onSecondaryContainer = Color(0.255f, 0.255f, 0.255f, 1.0f),
)

// Utility function to check if a color is light or dark
fun Color.isLightColor(): Boolean {
    return this.luminance() > 0.5
}

@Composable
fun AngularAppsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val cardContentColor = if (colorScheme.surface.isLightColor()) {
        Color.Black // Darker text and icon color for light background
    } else {
        Color.White // Lighter text and icon color for dark background
    }

    val typography = Typography.copy()

    CompositionLocalProvider(LocalContentColor provides cardContentColor) {
        MaterialTheme(
            colorScheme = colorScheme.copy(
                onSurface = cardContentColor,
            ),
            typography = typography,
            content = content
        )
    }
}