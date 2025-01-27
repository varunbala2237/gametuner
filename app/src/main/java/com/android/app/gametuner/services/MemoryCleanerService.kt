package com.android.app.gametuner.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.android.app.gametuner.R
import com.android.app.gametuner.StateStorage
import com.android.app.gametuner.shizuku.ShizukuHelperForServices

class MemoryCleanerService : Service() {
    private val channelId = "memory_cleaner_service_channel"
    private val channelName = "Memory Cleaner Service"
    private val notificationId = 1

    // Initialize StateStorage with application context
    private val stateStorage by lazy { StateStorage(applicationContext) }

    // Use the Handler with Looper.getMainLooper()
    private val handler = Handler(Looper.getMainLooper())
    private val delay: Long = 10000

    // Essentials
    private lateinit var selectedPackageName: String
    private lateinit var excludedPackages: List<String>
    private lateinit var command: String

    // Runnable that will repeatedly execute the termination command
    private val memoryCleanerRunnable = object : Runnable {
        override fun run() {
            // Execute the shell command via ShizukuHelperForServices
            ShizukuHelperForServices.executeShellCommands(command)

            // Schedule the next execution
            handler.postDelayed(this, delay)
        }
    }

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
        selectedPackageName = intent?.getStringExtra("selectedPackageName") ?: ""

        // Declare the exclusion list once here
        excludedPackages = listOf(selectedPackageName, packageName)

        // Build the command once
        command = buildShellCommand(excludedPackages)

        val notification = createServiceNotification()
        startForeground(notificationId, notification)

        // Start periodic execution of the memory cleaner
        handler.post(memoryCleanerRunnable)

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

    private fun buildShellCommand(excludedPackages: List<String>): String {
        // Build the command to force-stop apps excluding the selected game package and the GameTuner app
        // `am force-stop` stops the app, but excludes the selected package and GameTuner app
        val excludedPackagesPattern = excludedPackages.joinToString(" | grep -v ") { it }
        return "pm list packages | grep -vE '$excludedPackagesPattern' | awk -F: '{print \$2}' | xargs -r am force-stop"
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