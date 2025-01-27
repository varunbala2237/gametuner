package com.android.app.gametuner.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.android.app.gametuner.R
import com.android.app.gametuner.StateStorage

class MemoryCleanerService : Service() {
    private val channelId = "memory_cleaner_service_channel"
    private val channelName = "Memory Cleaner Service"
    private val notificationId = 1

    // Initialize StateStorage with application context
    private val stateStorage by lazy { StateStorage(applicationContext) }

    override fun onCreate() {
        super.onCreate()

        // Create the notification channel for foreground service (required for Android O and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Retrieve the package name passed via the intent
        val selectedPackageName = intent?.getStringExtra("selectedPackageName") ?: ""

        val notification = createServiceNotification()
        startForeground(notificationId, notification)

        return START_STICKY
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Running Services")
            .setContentText("RAM Cleaner")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true) // Keeps the notification ongoing
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        // We do not bind to this service, so return null
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clean up if necessary when service is destroyed
        stateStorage.saveMemoryCleanerSwitchState(false)
    }
}