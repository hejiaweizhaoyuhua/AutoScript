package com.hjw.gamelogic

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint

@SuppressLint("StaticFieldLeak")
object GameLogicController {
    private var accessibilityService: AccessibilityService? = null

    fun onAccessibilityConnect(service: AccessibilityService) {
        accessibilityService = service
    }

    fun onAccessibilityUnbind() {
        accessibilityService = null
    }

    fun startScript() {

    }

    fun stopScript() {

    }

    fun pauseScript() {

    }

    private fun checkIsInTheGame() {

    }
}