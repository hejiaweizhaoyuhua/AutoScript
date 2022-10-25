package com.hjw.autoscript.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.projection.MediaProjectionManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.hjw.accessibilitylib.AccessibilityHelper
import com.hjw.library.extension.Extension.default

class ScreenCaptureService : Service() {
    private val tag = javaClass.simpleName

    private val mBinder = MyBinder()

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    inner class MyBinder : Binder() {
        fun getService(): ScreenCaptureService {
            return this@ScreenCaptureService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val resultCode = intent?.getIntExtra(INTENT_CODE, -1).default()
        val data = intent?.getParcelableExtra<Intent>(INTENT_DATA)

        val mediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val mediaProjection = data?.let {
            mediaProjectionManager.getMediaProjection(resultCode, it)
        }

        if (mediaProjection == null) {
            Log.e(tag, "mediaProjection is null!!!")
            return super.onStartCommand(intent, flags, startId)
        }

        AccessibilityHelper.initImageLoader()
        AccessibilityHelper.initVirtualDisplay(mediaProjection)

        return START_NOT_STICKY
    }

    fun startCapture(): Bitmap? {
        return AccessibilityHelper.cutoutFrame()
    }

    companion object {
        const val INTENT_DATA = "data"
        const val INTENT_CODE = "code"
    }
}