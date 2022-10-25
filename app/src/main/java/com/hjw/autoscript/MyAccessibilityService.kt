package com.hjw.autoscript

import android.accessibilityservice.AccessibilityService
import android.media.ImageReader
import android.util.Log
import android.view.Display
import android.view.accessibility.AccessibilityEvent
import com.hjw.accessibilitylib.AccessibilityHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer
import java.util.concurrent.TimeUnit

class MyAccessibilityService : AccessibilityService() {
    private val tag = javaClass.simpleName

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(tag, "onServiceConnected")

        Observable.interval(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

            }
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        Log.i(tag, "onAccessibilityEvent")
    }

    override fun onInterrupt() {
        Log.i(tag, "onInterrupt")
    }
}