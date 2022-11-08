package com.hjw.textrecognition

import android.graphics.Bitmap
import android.media.Image
import android.util.Log
import android.view.Surface
import com.blankj.utilcode.util.ImageUtils
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

object TextRecognitionHelper {
    private val tag = javaClass.simpleName

    private val recognizer =
        TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())

    fun recognize(bitmap: Bitmap) {
        Observable.create {
            // 超过720P，开始缩放成720P
            val resultBitmap: Bitmap = if (bitmap.width > 720) {
                val targetWidth = 720
                val targetHeight = bitmap.height * targetWidth / bitmap.width
                ImageUtils.scale(bitmap, targetWidth, targetHeight)
            } else {
                bitmap
            }

            it.onNext(resultBitmap)
            it.onComplete()
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Bitmap> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Bitmap) {
                    recognizeInner(bitmap)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
    }

    private fun recognizeInner(bitmap: Bitmap) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(inputImage)
            .addOnSuccessListener {
                for (block in it.textBlocks) {
                    val blockFrame = block.boundingBox
                    Log.i(tag, "blockFrame=${blockFrame}")
                }
            }
            .addOnFailureListener {
                Log.e(tag, "文字识别错误：", it)
            }
    }
}