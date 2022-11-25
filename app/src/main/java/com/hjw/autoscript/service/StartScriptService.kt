package com.hjw.autoscript.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.hjw.autoscript.utils.PreferenceUtils
import com.hjw.gamelogic.GameLogicController

class StartScriptService : Service() {
    private val mBinder = MyBinder()

    inner class MyBinder : Binder() {
        fun getService(): StartScriptService {
            return this@StartScriptService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    fun saveDanrenPlan(isEnable: Boolean) {
        PreferenceUtils.saveDanrenPlan(isEnable)
    }

    fun startScript() {
        GameLogicController.startScript()
    }
}