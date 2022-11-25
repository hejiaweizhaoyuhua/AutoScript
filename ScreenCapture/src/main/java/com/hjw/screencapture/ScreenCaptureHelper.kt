package com.hjw.screencapture

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.hjw.screencapture.service.ScreenCaptureService
import java.nio.ByteBuffer

object ScreenCaptureHelper {
    private var imageReader: ImageReader? = null
    private var virtualDisplay: VirtualDisplay? = null

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

    fun stopCaptureService(context: Context) {
        context.unbindService(serviceConnection)
    }

    fun isStartScript(): Boolean {
        return screenCaptureService != null
    }

    fun startCaptureIntent(activity: Activity, requestCode: Int) {
        val mediaProjectionManager =
            activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val intent = mediaProjectionManager.createScreenCaptureIntent()
        activity.startActivityForResult(intent, requestCode)
    }

    fun initImageLoader() {
        if (imageReader != null) {
            imageReader?.close()
            imageReader = null
        }

        imageReader = ImageReader.newInstance(
            ScreenUtils.getScreenWidth(),
            ScreenUtils.getScreenHeight(),
            PixelFormat.RGBA_8888, 2
        )
    }

    fun initVirtualDisplay(mediaProjection: MediaProjection) {
        virtualDisplay = mediaProjection.createVirtualDisplay(
            "screen",
            ScreenUtils.getScreenWidth(),
            ScreenUtils.getScreenHeight(),
            Resources.getSystem().displayMetrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader?.surface,
            null, null
        )
    }

    fun cutoutFrame(): Bitmap? {
        val image: Image = imageReader?.acquireLatestImage() ?: return null
        val width: Int = image.width
        val height: Int = image.height
        val planes: Array<Image.Plane> = image.planes
        val buffer: ByteBuffer = planes[0].buffer
        val pixelStride: Int = planes[0].pixelStride
        val rowStride: Int = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * width
        val bitmap = Bitmap.createBitmap(
            width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888
        )
        bitmap.copyPixelsFromBuffer(buffer)
        image.close()

        return bitmap
    }

    fun releaseCapture() {
        if (imageReader != null) {
            imageReader?.close()
            imageReader = null
        }

        if (virtualDisplay != null) {
            virtualDisplay?.release()
            virtualDisplay = null
        }
    }
}