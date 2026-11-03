package com.paypanel.ridealert

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.provider.Settings
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import android.graphics.Typeface
import android.graphics.Color
import android.view.ViewGroup
import android.content.Context

class MainActivity : Activity() {
    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 10)
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(28), dp(22), dp(24))
            setBackgroundColor(Color.rgb(18, 22, 28))
        }
        val title = TextView(this).apply {
            text = "Ride Alert 300"
            textSize = 28f
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
        }
        val desc = TextView(this).apply {
            text = "Uber Driver ke visible offer card mein ₹300 ya usse zyada fare dikhe, to phone alert dega. App ride accept ya reject nahi karta."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            setPadding(0, dp(18), 0, dp(18))
        }
        val threshold = TextView(this).apply {
            text = "Minimum fare: ₹300  •  Service: Uber Driver"
            textSize = 18f
            setTextColor(Color.rgb(130, 220, 160))
            typeface = Typeface.DEFAULT_BOLD
            setPadding(0, dp(8), 0, dp(22))
        }
        val button = Button(this).apply {
            text = "Enable fare monitoring"
            setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        }
        val instructions = TextView(this).apply {
            text = "\nSetup:\n1. Button dabao.\n2. Accessibility settings mein “Ride Alert 300” / “Uber ride fare alert” kholo.\n3. Service ON karo aur permission confirm karo.\n4. Uber Driver par wapas jao.\n\nPrivacy: Service sirf com.ubercab.driver package ke visible screen text ko inspect karti hai. Koi screenshot, login, OTP, location ya ride action transmit nahi hota. Data internet par nahi bheja jata.\n\nNote: Uber app ke kuch versions offer text accessibility ke liye expose nahi karte. Aise mein alert kaam nahi karega. Notification permission bhi allow honi chahiye."
            textSize = 14f
            setTextColor(Color.LTGRAY)
            setPadding(0, dp(18), 0, 0)
        }
        root.addView(title)
        root.addView(desc)
        root.addView(threshold)
        root.addView(button, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(54)))
        root.addView(instructions)
        setContentView(root)
    }
}
