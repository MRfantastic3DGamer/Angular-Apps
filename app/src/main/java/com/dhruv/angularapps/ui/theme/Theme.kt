package com.dhruv.angularapps.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = SeedOnPrimary,
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = SeedPrimaryContainer,
    secondary = PurpleGrey80,
    onSecondary = SeedDarkSurface,
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = SeedSecondaryContainer,
    tertiary = Pink80,
    onTertiary = SeedDarkSurface,
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = SeedTertiaryContainer,
    background = SeedDarkBackground,
    onBackground = SeedDarkOnBackground,
    surface = SeedDarkSurface,
    onSurface = SeedDarkOnSurface,
    surfaceVariant = SeedDarkSurfaceVariant,
    onSurfaceVariant = SeedDarkOnSurfaceVariant,
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
)

private val LightColorScheme = lightColorScheme(
    primary = SeedPrimary,
    onPrimary = SeedOnPrimary,
    primaryContainer = SeedPrimaryContainer,
    onPrimaryContainer = SeedOnPrimaryContainer,
    secondary = SeedSecondary,
    onSecondary = SeedOnSecondary,
    secondaryContainer = SeedSecondaryContainer,
    onSecondaryContainer = SeedOnSecondaryContainer,
    tertiary = SeedTertiary,
    onTertiary = SeedOnTertiary,
    tertiaryContainer = SeedTertiaryContainer,
    onTertiaryContainer = SeedOnTertiaryContainer,
    background = SeedBackground,
    onBackground = SeedOnBackground,
    surface = SeedSurface,
    onSurface = SeedOnSurface,
    surfaceVariant = SeedSurfaceVariant,
    onSurfaceVariant = SeedOnSurfaceVariant,
    outline = SeedOutline,
    outlineVariant = SeedOutlineVariant,
)

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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
