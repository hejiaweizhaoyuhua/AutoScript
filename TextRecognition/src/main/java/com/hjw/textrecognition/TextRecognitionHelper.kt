package com.hjw.textrecognition

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions

object TextRecognitionHelper {
    private val tag = javaClass.simpleName

    private val recognizer =
        TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())

    fun recognize(bitmap: Bitmap, listener: (textBlocks: List<Text.TextBlock>) -> Unit) {
        recognizeInner(bitmap, listener)
    }

    private fun recognizeInner(
        bitmap: Bitmap,
        listener: (textBlocks: List<Text.TextBlock>) -> Unit
    ) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(inputImage)
            .addOnSuccessListener {
                for (block in it.textBlocks) {
                    val text = block.text
                    Log.i(tag, "text=${text}")
                }

                listener.invoke(it.textBlocks)
            }
            .addOnFailureListener {
                Log.e(tag, "文字识别错误：", it)
            }
    }
}