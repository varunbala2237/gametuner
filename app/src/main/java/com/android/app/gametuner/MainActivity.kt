package com.android.app.gametuner

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.android.app.gametuner.ui.GameTunerApp
import com.android.app.gametuner.ui.theme.GameTunerTheme
import rikka.shizuku.Shizuku

class MainActivity : ComponentActivity() {

    private var requestPermissionCode = 1001

    private val requestPermissionResultListener =
        Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
            val granted = grantResult == PackageManager.PERMISSION_GRANTED
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // Add the permission result listener
            Shizuku.addRequestPermissionResultListener(requestPermissionResultListener)
        } catch (e: Exception) {
            // Show Toast if Shizuku is not ready
            Toast.makeText(this, "Shizuku is not ready", Toast.LENGTH_LONG).show()
            // Auto-close the app
            finish()
            return
        }

        setContent {
            GameTunerTheme {
                CompositionLocalProvider(LocalMainActivity provides this) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        GameTunerApp()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the permission result listener
        Shizuku.removeRequestPermissionResultListener(requestPermissionResultListener)
    }

    fun getInstalledGames(): List<PackageInfo> {
        val packageManager = packageManager
        @Suppress("WARNINGS")
        return packageManager.getInstalledPackages(PackageManager.GET_META_DATA).filter { pkgInfo ->
            val appInfo = pkgInfo.applicationInfo
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                // For API 26 and above, use the category property
                appInfo.category == ApplicationInfo.CATEGORY_GAME
            } else {
                @Suppress("DEPRECATION")
                appInfo.flags and ApplicationInfo.FLAG_IS_GAME != 0
            }
        }
    }

    fun launchGame(packageName: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(launchIntent)
    }
}