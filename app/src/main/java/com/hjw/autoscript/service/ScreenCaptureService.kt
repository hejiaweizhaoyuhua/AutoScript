package com.hjw.autoscript.service

import android.app.*
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
import com.hjw.accessibilitylib.AccessibilityHelper
import com.hjw.autoscript.R
import com.hjw.library.extension.Extension.default


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

    private fun initMediaProjection(intent: Intent?) {
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

        AccessibilityHelper.initImageLoader()
        AccessibilityHelper.initVirtualDisplay(mediaProjection)
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Call Start foreground with notification
            val notificationIntent = Intent(this, ScreenCaptureService::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                1,
                notificationIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_IMMUTABLE
                else
                    PendingIntent.FLAG_ONE_SHOT
            )
            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, "channel_id_1001")
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            resources,
                            R.drawable.ic_launcher_foreground
                        )
                    )
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("正在检测游戏状态")
                    .setContentIntent(pendingIntent)
            val notification = notificationBuilder.build()
            val channel = NotificationChannel(
                "channel_id_1001",
                "channel_name_screen_capture",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "正在检测游戏状态"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            startForeground(
                1001,
                notification
            ) //必须使用此方法显示通知，不能使用notificationManager.notify，否则还是会报上面的错误
        }
    }

    fun startCapture(): Bitmap? {
        return AccessibilityHelper.cutoutFrame()
    }

    companion object {
        const val INTENT_DATA = "data"
        const val INTENT_CODE = "code"
    }
}