package com.android.app.gametuner

import androidx.compose.runtime.compositionLocalOf

val LocalMainActivity = compositionLocalOf<MainActivity> {
    error("MainActivity context not provided")
}