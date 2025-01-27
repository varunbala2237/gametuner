package com.android.app.gametuner.settings

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager

fun getDeviceResolution(context: Context): Pair<Size, Int> {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // For Android 11+ (API 30 and above)
        val metrics = windowManager.currentWindowMetrics
        val bounds = metrics.bounds
        val width = bounds.width()
        val height = bounds.height()
        val densityDpi = context.resources.displayMetrics.densityDpi
        Pair(Size(width, height), densityDpi)
    } else {
        // For Android 10 and below
        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getRealMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val densityDpi = metrics.densityDpi
        Pair(Size(width, height), densityDpi)
    }
}