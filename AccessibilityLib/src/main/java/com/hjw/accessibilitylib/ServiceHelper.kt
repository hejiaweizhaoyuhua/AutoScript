package com.hjw.accessibilitylib

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.provider.Settings

object ServiceHelper {
    fun isServiceOn(context: Context, className: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(100)
        if (runningServices.size <= 0) {
            return false
        }

        runningServices.forEach {
            if (it.service.className.contains(className)) {
                return true
            }
        }

        return false
    }

    fun jumpAccessibilityPage(context: Context) {
        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}