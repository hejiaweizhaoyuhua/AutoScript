package com.hjw.accessibilitylib.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.hjw.accessibilitylib.AccessibilityHelper
import com.hjw.accessibilitylib.ServiceHelper
import com.hjw.textrecognition.TextRecognitionHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

class MyAccessibilityService : AccessibilityService() {
    private val tag = javaClass.simpleName

    private var disposable: Disposable? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(tag, "onServiceConnected")

        // 启动截屏
        if (disposable == null) {
            Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (ServiceHelper.isStartScript()) {
                        Log.i(tag, "开始进行文字识别！")
                        val bitmap = ServiceHelper.startCaptureBitmap()
                        bitmap?.let {
                            TextRecognitionHelper.recognize(bitmap)
                        }
                    }
                }
        }
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        Log.i(tag, "onAccessibilityEvent")
    }

    override fun onInterrupt() {
        Log.i(tag, "onInterrupt")
    }

    override fun onDestroy() {
        super.onDestroy()

        if (disposable != null) {
            disposable?.dispose()
            disposable = null
        }
    }
}