package com.hjw.textrecognition

import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions

object TextRecognitionHelper {
     private val recognizer = TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())


}