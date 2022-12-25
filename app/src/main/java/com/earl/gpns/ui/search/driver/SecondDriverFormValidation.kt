package com.earl.gpns.ui.search.driver

import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

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
            return if (auto.selectedItem == "Другая" && autoEditText.text.isEmpty()) {
                Toast.makeText(context, "Укажите марку Вашего автомобиля", Toast.LENGTH_SHORT).show()
                false
            } else if (autoColor.selectedItem == "Другой" && autoColorEditText.text.isEmpty()) {
                Toast.makeText(context, "Укажите цвет Вашего автомобиля", Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }
    }
}