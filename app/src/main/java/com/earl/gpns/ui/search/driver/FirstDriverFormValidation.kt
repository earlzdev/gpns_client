package com.earl.gpns.ui.search.driver

import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

interface FirstDriverFormValidation {

    fun validate() : Boolean

    class Base(
        private val from: Spinner,
        private val fromEditText: EditText,
        private val to: Spinner,
        private val toEditText: EditText,
        private val actualTripTime: Spinner,
        private val actualTripTimeEditText: EditText
    ) :FirstDriverFormValidation {

        private val context = from.context

        override fun validate(): Boolean {
            return if (from.selectedItem == "Другой район" && fromEditText.text.isEmpty()) {
                Toast.makeText(context, "Выберите место, откуда Вы выезжаете", Toast.LENGTH_SHORT).show()
                false
            } else if (to.selectedItem == "Другое" && toEditText.text.isEmpty()) {
                Toast.makeText(context, "Выберите место, до которого доезжаете", Toast.LENGTH_SHORT).show()
                false
            } else if (actualTripTime.selectedItem == "Другое" && actualTripTimeEditText.text.isEmpty()) {
                Toast.makeText(context, "Выберите время, в течение которого Вы будете ездить по этому маршруту", Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }
    }
}