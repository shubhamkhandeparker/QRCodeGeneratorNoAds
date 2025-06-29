package com.example.qrgeneratornoads.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.qrgeneratornoads.ui.theme.Typography
import com.example.qrgeneratornoads.ui.theme.PrimaryBlue
import com.example.qrgeneratornoads.ui.theme.SecondaryButton
import com.example.qrgeneratornoads.ui.theme.OnSurfaceDark
import com.example.qrgeneratornoads.ui.theme.BackgroundDark
import com.example.qrgeneratornoads.ui.theme.SurfaceDark
import com.example.qrgeneratornoads.ui.theme.SurfaceVariantDark
import com.example.qrgeneratornoads.ui.theme.OnSurfaceVariantDark
import androidx.compose.material3.Typography

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    secondary = SecondaryButton,
    onSecondary = OnSurfaceDark,
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark
)

@Composable
fun QRGeneratorNoAdsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme // We're focusing on dark theme only

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}