package com.hjw.accessibilitylib

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.hjw.accessibilitylib.service.ScreenCaptureService

object ServiceHelper {
    private var screenCaptureService: ScreenCaptureService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            LogUtils.i("onServiceConnected!!!")
            screenCaptureService = (service as ScreenCaptureService.MyBinder).getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            LogUtils.i("onServiceDisconnected!!!")
            screenCaptureService = null
        }
    }

    fun initCaptureService(context: Context, resultCode: Int, data: Intent?) {
        context.bindService(Intent(context, ScreenCaptureService::class.java).apply {
            putExtra(ScreenCaptureService.INTENT_CODE, resultCode)
            putExtra(ScreenCaptureService.INTENT_DATA, data)
        }, serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)
    }

    fun startCapture(): Bitmap? {
        return screenCaptureService?.startCapture()
    }
}