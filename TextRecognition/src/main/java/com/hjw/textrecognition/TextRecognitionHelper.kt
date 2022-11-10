package com.hjw.textrecognition

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions

object TextRecognitionHelper {
    private val tag = javaClass.simpleName

    private val recognizer =
        TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())

    fun recognize(bitmap: Bitmap) {
        recognizeInner(bitmap)
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