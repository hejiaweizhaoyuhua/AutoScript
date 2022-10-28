package com.hjw.library.extension

object Extension {
    fun Int?.default(defaultValue: Int = 0) : Int {
        return this ?: defaultValue
    }
}