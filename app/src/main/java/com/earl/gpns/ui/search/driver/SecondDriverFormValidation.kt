package com.earl.gpns.ui.search.driver

import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.earl.gpns.R

interface SecondDriverFormValidation {

    fun validate() : Boolean

    class Base(
        private val auto: Spinner,
        private val autoEditText: EditText,
        private val autoColor: Spinner,
        private val autoColorEditText: EditText,
    ) : SecondDriverFormValidation {

        private val context = auto.context

        override fun validate(): Boolean {
            return if (auto.selectedItem == context.getString(R.string.anothera) && autoEditText.text.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.car_mark), Toast.LENGTH_SHORT).show()
                false
            } else if (autoColor.selectedItem == context.getString(R.string.another) && autoColorEditText.text.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.cal_color), Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }
    }
}