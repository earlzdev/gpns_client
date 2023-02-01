package com.earl.gpns.ui.search.companion

import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.earl.gpns.R

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
            return if (from.selectedItem == context.resources.getString(R.string.anither_district) && fromEd.text.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.choose_district_u_comfortable_to_catch), Toast.LENGTH_SHORT).show()
                false
            } else if (to.selectedItem == context.resources.getString(R.string.another) && toEd.text.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.choose_place_u_need_to_go), Toast.LENGTH_SHORT).show()
                false
            } else if (tripActualTime.selectedItem == context.resources.getString(R.string.another) && tripActualTimeEd.text.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.choose_time), Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }
    }

}