package com.android.app.gametuner.services

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.app.gametuner.R
import com.android.app.gametuner.StateStorage
import com.android.app.gametuner.shizuku.ShizukuHelper

class MemoryCleanerService : Service() {
    private val channelId = "memory_cleaner_service_channel"
    private val channelName = "Memory Cleaner Service"
    private val notificationId = 1

    // Initialize StateStorage with application context
    private val stateStorage by lazy { StateStorage(applicationContext) }

    // Use the Handler with Looper.getMainLooper()
    private val handler = Handler(Looper.getMainLooper())
    private val delay: Long = 5000

    // Essentials
    private lateinit var selectedPackageName: String
    private lateinit var excludedPackages: List<String>

    // Runnable that will repeatedly execute the termination command
    private val memoryCleanerRunnable = object : Runnable {
        override fun run() {
            // Log before cleaning memory
            Log.d("MemoryCleanerService", "Running memory cleaner task.")

            // Start with retrieving the processes
            getUsageStats(applicationContext)

            // Schedule the next execution
            handler.postDelayed(this, delay)
        }
    }

    // Retrieve recently used packages and terminate them
    fun getUsageStats(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val currentTime = System.currentTimeMillis()
            val startTime = currentTime - 1000 * 3600 // 1 hour back

            // Query for usage stats in the last hour
            val usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, startTime, currentTime
            )

            if (usageStatsList != null) {
                for (usageStats in usageStatsList) {
                    val packageName = usageStats.packageName

                    // Skip excluded packages
                    if (!excludedPackages.contains(packageName)) {
                        // Terminate the package if it's not excluded
                        killPackage(context, packageName)
                    }
                }
            }
        }
    }

    // To kill the package (terminate background process)
    private fun killPackage(context: Context, packageName: String) {
        // Use ActivityManager to kill the background processes of the app
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(packageName)
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

    override fun onBind(intent: Intent?): IBinder? {
        // We do not bind to this service, so return null
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clean up if necessary when service is destroyed
        stateStorage.saveMemoryCleanerSwitchState(false)

        // Remove pending tasks
        handler.removeCallbacks(memoryCleanerRunnable)
    }
}