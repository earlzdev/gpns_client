package com.earl.gpns.ui.search.driver

import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.earl.gpns.R

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
            return if (from.selectedItem == context.resources.getString(R.string.anither_district) && fromEditText.text.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.place_u_from), Toast.LENGTH_SHORT).show()
                false
            } else if (to.selectedItem == context.resources.getString(R.string.another) && toEditText.text.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.place_u_drive), Toast.LENGTH_SHORT).show()
                false
            } else if (actualTripTime.selectedItem == context.resources.getString(R.string.another) && actualTripTimeEditText.text.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.time_u_drive), Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }
    }
}