package com.earl.gpns.ui.search

import android.content.Context
import android.widget.ArrayAdapter

interface SpinnerInterfaceInitializator {
    fun initSpinnerAdapter(textResource: Int, context: Context) : ArrayAdapter<CharSequence>
}