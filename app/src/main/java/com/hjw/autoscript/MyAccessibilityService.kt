package com.hjw.autoscript

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {
    private val tag = javaClass.simpleName

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(tag, "onServiceConnected")
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        Log.i(tag, "onAccessibilityEvent")
    }

    override fun onInterrupt() {
        Log.i(tag, "onInterrupt")
    }
}