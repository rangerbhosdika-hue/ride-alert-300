package com.paypanel.ridealert

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.Locale

class RideAccessibilityService : AccessibilityService() {
    private val channelId = "ride_alert_300"
    private var lastAlertKey = ""
    private var lastAlertAt = 0L

    override fun onServiceConnected() {
        super.onServiceConnected()
        createChannel()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || event.packageName?.toString() != "com.ubercab.driver") return
        val root = rootInActiveWindow ?: return
        val text = StringBuilder()
        collectText(root, text)
        val content = text.toString()
        val fare = findFare(content) ?: return
        if (fare < 300) return

        // Avoid repeated alerts while the same offer card remains on screen.
        val key = "$fare|${content.hashCode()}"
        val now = System.currentTimeMillis()
        if (key == lastAlertKey && now - lastAlertAt < 120_000L) return

        lastAlertKey = key
        lastAlertAt = now
        showAlert(fare)
    }

    private fun collectText(node: AccessibilityNodeInfo?, out: StringBuilder) {
        if (node == null) return
        node.text?.toString()?.takeIf { it.isNotBlank() }?.let {
            out.append(it).append(' ')
        }
        node.contentDescription?.toString()?.takeIf { it.isNotBlank() }?.let {
            out.append(it).append(' ')
        }
        for (i in 0 until node.childCount) collectText(node.getChild(i), out)
    }

    private fun findFare(text: String): Int? {
        // Prefer explicit rupee amounts; this avoids treating trip distances/times as fares.
        val regex = Regex("₹\\s*([0-9,]+)")
        return regex.findAll(text).mapNotNull {
            it.groupValues.getOrNull(1)?.replace(",", "")?.toIntOrNull()
        }.maxOrNull()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "₹300+ Uber ride alerts", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts when a visible Uber Driver offer shows ₹300 or more."
                enableVibration(true)
            }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun showAlert(fare: Int) {
        val openApp = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("₹300+ Uber offer detected")
            .setContentText("Visible offer fare: ₹${String.format(Locale.US, "%,d", fare)}. Check the trip and decide yourself.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
            .setAutoCancel(true)
            .setContentIntent(openApp)
            .build()
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(300, notification)
    }

    override fun onInterrupt() {}
}
