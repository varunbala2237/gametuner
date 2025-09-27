package com.android.app.gametuner

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.android.app.gametuner.shizuku.ShizukuHelper.isShizukuInstalled
import com.android.app.gametuner.shizuku.ShizukuMissingDialog
import com.android.app.gametuner.shizuku.ShizukuPermissionDialog
import com.android.app.gametuner.ui.GameTunerApp
import com.android.app.gametuner.ui.theme.GameTunerTheme
import rikka.shizuku.Shizuku

class MainActivity : ComponentActivity() {
    private val requestPermissionCode = 1001

    private fun requestShizukuPermission(): Boolean {
        return try {
            Shizuku.requestPermission(requestPermissionCode)
            true
        } catch (e: Throwable) {
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GameTunerTheme {
                CompositionLocalProvider(LocalMainActivity provides this) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        GameTunerApp()

                        if(!isShizukuInstalled(this) && !Shizuku.pingBinder()) {
                            ShizukuMissingDialog(
                                onInstall = { openOrInstall() },
                                onCancel = { finish() }
                            )
                        } else {
                            ShizukuPermissionDialog(
                                onOpen = { openOrInstall() },
                                onGrant = { requestShizukuPermission() }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun openOrInstall() {
        // Open Shizuku app or Play Store
        val intent = packageManager.getLaunchIntentForPackage("moe.shizuku.privileged.api")
            ?: Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=moe.shizuku.privileged.api")
            )
        startActivity(intent)
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