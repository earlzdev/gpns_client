package com.earl.gpns.ui.search.companion

import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

interface CompanionFormValidation {

    fun validate() : Boolean

    class Base(
        private val from: Spinner,
        private val fromEd: EditText,
        private val to: Spinner,
        private val toEd: EditText,
        private val tripActualTime: Spinner,
        private val tripActualTimeEd: EditText
    ) : CompanionFormValidation {

        val context = from.context

        override fun validate(): Boolean {
            return if (from.selectedItem == "Другой район" && fromEd.text.isEmpty()) {
                Toast.makeText(context, "Выберите район, откуда Вас можно забрать", Toast.LENGTH_SHORT).show()
                false
            } else if (to.selectedItem == "Другое" && toEd.text.isEmpty()) {
                Toast.makeText(context, "Выберите место, до которого Вам нужно доехать", Toast.LENGTH_SHORT).show()
                false
            } else if (tripActualTime.selectedItem == "Другое" && tripActualTimeEd.text.isEmpty()) {
                Toast.makeText(context, "Выберите время, в течение которого Вам нужно будет ездить по этому маршруту", Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }
    }

}