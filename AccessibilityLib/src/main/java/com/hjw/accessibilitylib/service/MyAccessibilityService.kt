package com.hjw.accessibilitylib.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.hjw.gamelogic.GameLogicController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

class MyAccessibilityService : AccessibilityService() {
    private val tag = javaClass.simpleName

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(tag, "onServiceConnected")

        GameLogicController.onAccessibilityConnect(this)
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        Log.i(tag, "onAccessibilityEvent")
    }

    override fun onInterrupt() {
        Log.i(tag, "onInterrupt")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        GameLogicController.onAccessibilityUnbind()
        return super.onUnbind(intent)
    }
}