package com.hjw.gamelogic

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.text.TextUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.mlkit.vision.text.Text
import com.hjw.screencapture.ScreenCaptureHelper
import com.hjw.textrecognition.TextRecognitionHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@SuppressLint("StaticFieldLeak")
object GameLogicController {
    private var accessibilityService: AccessibilityService? = null

    private var inGameDisposable: Disposable? = null
    private var isStandDisposable: Disposable? = null

    private var isStartingScript: Boolean = false

    private var standingStr = ""
    private var standingTime = 0
    private const val STANDING_MAX_TIME = 20

    fun onAccessibilityConnect(service: AccessibilityService) {
        accessibilityService = service
    }

    fun onAccessibilityUnbind() {
        accessibilityService = null
    }

    fun startScript() {
        isStartingScript = true
        // 循环判断是否已经进入了游戏
        inGameDisposable = Observable.interval(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkIsInTheGame(object : OnRecognizeWithTextListener {
                    override fun onSuccess(text: String) {
                        LogUtils.i("已经在游戏中")
                        if (inGameDisposable != null) {
                            inGameDisposable?.dispose()
                        }

                        checkIsStanding(object : OnRecognizeWithTextListener {
                            override fun onSuccess(text: String) {
                                if (standingStr == text) {
                                    standingTime += 1
                                    if (standingTime >= STANDING_MAX_TIME) {
                                        // 释放定时器
                                        if (isStandDisposable != null) {
                                            isStandDisposable?.dispose()
                                        }

                                        // 模拟home键
                                        performHome()
                                    }
                                } else {
                                    standingTime = 0
                                }

                                standingStr = text
                            }

                            override fun onFail() {

                            }
                        })
                    }

                    override fun onFail() {
                        ToastUtils.showShort("请进入游戏")
                    }
                })
            }
    }

    fun stopScript() {

    }

    fun pauseScript() {

    }

    /**
     * 模拟Home键
     */
    fun performHome() {
        accessibilityService?.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
    }

    /**
     * 判断脚本是否执行中
     */
    fun isScriptStarting(): Boolean {
        return isStartingScript
    }

    private fun checkIsInTheGame(listener: OnRecognizeWithTextListener) {
        val bitmap = ScreenCaptureHelper.cutoutFrame()
        bitmap?.let {
            chargeText(
                bitmap,
                arrayOf("技能", "打造"),
                0,
                it.height - it.height / 10,
                it.width,
                it.height / 10
            ) {
                if (!TextUtils.isEmpty(it)) {
                    listener.onSuccess(it)
                } else {
                    listener.onFail()
                }
            }
        }
    }

    private fun checkIsStanding(listener: OnRecognizeWithTextListener) {
        isStandDisposable = Observable.interval(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val bitmap = ScreenCaptureHelper.cutoutFrame()
                bitmap?.let {
                    chargeText(
                        bitmap,
                        arrayOf("白天", "夜晚"),
                        0,
                        0,
                        it.width / 2,
                        it.height / 10
                    ) {
                        if (!TextUtils.isEmpty(it)) {
                            listener.onSuccess(it)
                        } else {
                            listener.onFail()
                        }
                    }
                }
            }
    }

    /**
     * 判断是否还有该文字
     */
    private fun chargeText(
        bitmap: Bitmap?,
        textArr: Array<String>,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        listener: (text: String) -> Unit
    ) {
        if (bitmap != null) {
            Observable.create {
                // 裁剪bitmap
                val clipBitmap = ImageUtils.clip(
                    bitmap, x, y, width, height
                )
                // 字体识别
                TextRecognitionHelper.recognize(clipBitmap) { list ->
                    for (block in list) {
                        for (str in textArr) {
                            if (block.text.contains(str)) {
                                it.onNext(block.text)
                                return@recognize
                            }
                        }
                    }

                    it.onNext("")
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: String) {
                        listener.invoke(t)
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })

        } else {
            ToastUtils.showShort("截取屏幕内容失败")
        }
    }

    interface OnRecognizeWithTextListener {
        fun onSuccess(text: String)
        fun onFail()
    }
}