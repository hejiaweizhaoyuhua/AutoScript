package com.hjw.accessibilitylib.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
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