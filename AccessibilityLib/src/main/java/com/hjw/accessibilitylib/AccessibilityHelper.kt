package com.hjw.accessibilitylib

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GestureResultCallback
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Path
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import com.blankj.utilcode.util.ScreenUtils
import java.nio.ByteBuffer


object AccessibilityHelper {
    private var imageReader: ImageReader? = null
    private var virtualDisplay: VirtualDisplay? = null

    fun click(service: AccessibilityService, x: Float, y: Float) {
        val builder = GestureDescription.Builder()
        val path = Path()
        path.moveTo(x, y)
        path.lineTo(x, y)
        builder.addStroke(StrokeDescription(path, 0, 1))
        val gesture = builder.build()
        service.dispatchGesture(gesture, object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
            }
        }, null)
    }

    private fun swipe(
        service: AccessibilityService,
        fx1: Float,
        fy1: Float,
        fx2: Float,
        fy2: Float,
        duration: Int
    ) {
        val builder = GestureDescription.Builder()
        val p = Path()
        p.moveTo(fx1, fy1)
        p.lineTo(fx2, fy2)
        builder.addStroke(StrokeDescription(p, 0L, duration.toLong()))
        val gesture = builder.build()
        service.dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
            }
        }, null)
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

        // 计算截图大小~默认为720p
//        val actualWidth = 720
//        val actualHeight =
//            actualWidth * ScreenUtils.getScreenHeight() / ScreenUtils.getScreenWidth()

        imageReader = ImageReader.newInstance(
            ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(),
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

    fun release() {
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