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
    fun click(service: AccessibilityService, x: Float, y: Float) {
        val builder = GestureDescription.Builder()
        val path = Path()
        path.moveTo(x, y)
        path.lineTo(x, y)
        builder.addStroke(StrokeDescription(path, 0, 1))
        val gesture = builder.build()
        service.dispatchGesture(gesture, object : GestureResultCallback() {
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
}