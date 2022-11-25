package com.hjw.floatview

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button

class FloatViewManager {
    private lateinit var windowManager: WindowManager
    private var floatRootView: View? = null //悬浮窗View

    var isShowing = false
    var listener: OnItemListener? = null

    fun setItemListener(listener: OnItemListener) {
        this.listener = listener
    }

    fun showWindow(context: Context) {
        if (isShowing)
            return

        // 设置LayoutParam
        // 获取WindowManager服务
        windowManager =
            context.getSystemService(AccessibilityService.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        val layoutParam = WindowManager.LayoutParams()
        layoutParam.apply {
            //显示的位置
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            format = PixelFormat.TRANSPARENT
        }
        floatRootView = LayoutInflater.from(context).inflate(R.layout.floatview_accessibility, null)
//        floatRootView?.setOnTouchListener(ItemViewTouchListener(layoutParam, windowManager))
        windowManager.addView(floatRootView, layoutParam)

        isShowing = true

        floatRootView?.let {
            val pauseScript: Button = it.findViewById(R.id.pause_script)
            val stopScript: Button = it.findViewById(R.id.stop_script)

            pauseScript.setOnClickListener {
                if (pauseScript.text.contains("暂停")) {
                    pauseScript.text = "开始脚本"
                    listener?.onPauseScript()
                } else if (pauseScript.text.contains("开始")) {
                    pauseScript.text = "暂停脚本"
                    listener?.onStartScript()
                }
            }
            stopScript.setOnClickListener {
                removeWindow()
                listener?.onStopScript()
            }
        }
    }

    fun removeWindow() {
        if (isShowing) {
            windowManager.removeView(floatRootView)
            isShowing = false
        }
    }

    interface OnItemListener {
        fun onPauseScript()

        fun onStartScript()

        fun onStopScript()
    }
}