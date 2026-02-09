package com.android.app.gametuner.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun lightenSurfaceColor(color: Color): Color {
    val lightenedColor = ColorUtils.blendARGB(color.toArgb(), Color.White.toArgb(), 0.05f)
    return Color(lightenedColor)
}

fun darkenSurfaceColor(color: Color): Color {
    val darkenedColor = ColorUtils.blendARGB(color.toArgb(), Color.Black.toArgb(), 0.05f)
    return Color(darkenedColor)
}
