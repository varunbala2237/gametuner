package com.android.app.gametuner.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.android.app.gametuner.R
import com.android.app.gametuner.StateStorage
import com.android.app.gametuner.global.GlobalLogsManager
import com.android.app.gametuner.shizuku.ShizukuHelper

class MemoryCleanerService : Service() {

    private val channelId = "memory_cleaner_service_channel"
    private val channelName = "Memory Cleaner Service"
    private val notificationId = 1

    private val stateStorage by lazy { StateStorage(applicationContext) }
    private val handler = Handler(Looper.getMainLooper())

    // OEM-safe interval
    private val delay: Long = 15_000L

    private var selectedGamePackage: String? = null

    private val memoryCleanerRunnable = object : Runnable {
        override fun run() {
            ShizukuHelper.executeShellCommandWithShizuku(
                "cmd activity idle-maintenance"
            )

            handler.postDelayed(this, delay)
        }
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        selectedGamePackage = intent?.getStringExtra("selectedPackageName")

        startForeground(notificationId, createServiceNotification())

        if (!selectedGamePackage.isNullOrEmpty()) {
            GlobalLogsManager.addLog(
                "Memory Cleaner started for game: $selectedGamePackage"
            )

            // Safe game prioritization (helper handles permission)
            ShizukuHelper.executeShellCommandWithShizuku(
                "cmd activity set-standby-bucket $selectedGamePackage highest"
            )
        }

        handler.post(memoryCleanerRunnable)
        return START_STICKY
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Running Services")
            .setContentText("Memory Cleaner active")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (!selectedGamePackage.isNullOrEmpty()) {
            GlobalLogsManager.addLog(
                "Memory Cleaner stopped for game: $selectedGamePackage"
            )

            // Restore default bucket (clean exit)
            ShizukuHelper.executeShellCommandWithShizuku(
                "cmd activity set-standby-bucket $selectedGamePackage active"
            )
        }

        stateStorage.saveMemoryCleanerSwitchState(false)
        handler.removeCallbacks(memoryCleanerRunnable)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
