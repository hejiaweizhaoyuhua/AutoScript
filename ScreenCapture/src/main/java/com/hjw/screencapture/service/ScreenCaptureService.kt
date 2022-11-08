package com.hjw.screencapture.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.projection.MediaProjectionManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.hjw.library.extension.Extension.default
import com.hjw.screencapture.R
import com.hjw.screencapture.ScreenCaptureHelper

class ScreenCaptureService : Service() {
    private val tag = javaClass.simpleName

    private val mBinder = MyBinder()

    override fun onBind(intent: Intent?): IBinder {
        initMediaProjection(intent)

        return mBinder
    }

    inner class MyBinder : Binder() {
        fun getService(): ScreenCaptureService {
            return this@ScreenCaptureService
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        ScreenCaptureHelper.releaseCapture()
    }

    private fun initMediaProjection(intent: Intent?) {
        Log.i(tag, "initMediaProjection!!!")
        val resultCode = intent?.getIntExtra(INTENT_CODE, -1).default()
        val data = intent?.getParcelableExtra<Intent>(INTENT_DATA)

        createNotification()

        val mediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val mediaProjection = data?.let {
            mediaProjectionManager.getMediaProjection(resultCode, it)
        }

        if (mediaProjection == null) {
            Log.e(tag, "mediaProjection is null!!!")
            return
        }

        ScreenCaptureHelper.initImageLoader()
        ScreenCaptureHelper.initVirtualDisplay(mediaProjection)
    }

    private fun createNotification() {
        // 适配8.0及以上 创建渠道
        val mNormalNotificationId = 1001
        val channelId = "channel_id_1001"
        val channelName = "channel_name_screen_capture"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "正在检测游戏状态"
            }
            notificationManager.createNotificationChannel(channel)
        }
        // 点击意图
        val intent = Intent(this, ScreenCaptureService::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        // 构建配置
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.mipmap.ic_launcher
                    )
                )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("正在检测游戏状态")
                .setContentIntent(pendingIntent)
        val notification = notificationBuilder.build()
        // 发起通知
        startForeground(mNormalNotificationId, notification)
    }

    fun startCaptureBitmap(): Bitmap? {
        return ScreenCaptureHelper.cutoutFrame()
    }

    companion object {
        const val INTENT_DATA = "data"
        const val INTENT_CODE = "code"
    }
}