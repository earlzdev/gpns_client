package com.earl.gpns.ui.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

class BitmapFromStringDecoder {

    fun decode(input: String) : Bitmap {
        val bytes = Base64.decode(input, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}