package com.earl.gpns.ui.auth.register

import android.widget.EditText
import com.earl.gpns.R
import com.earl.gpns.ui.core.isEmailValid
import javax.inject.Inject

interface RegisterFormValidation {

    fun validate(
        email: EditText,
        name: EditText,
        password: EditText,
        confirmPass: EditText
    ) : Boolean

    class Base @Inject constructor() : RegisterFormValidation {

        override fun validate(email: EditText, name: EditText, password: EditText, confirmPass: EditText): Boolean {
            val context = email.context
            var validate = true
            if (email.text.isEmpty()) {
                email.error = context.getString(R.string.field_cannot_be_empty)
                validate = false
            } else if (!email.text.toString().trim().isEmailValid()) {
                email.error = context.getString(R.string.incorrect_email)
                validate = false
            } else if (name.text.isEmpty()) {
                name.error = context.getString(R.string.field_cannot_be_empty)
                validate = false
            } else if (name.text.length < 3) {
                name.error = context.getString(R.string.name_is_too_short)
                validate = false
            } else if (password.text.isEmpty()) {
                password.error = context.getString(R.string.field_cannot_be_empty)
                validate = false
            } else if (password.text.length < 6) {
                password.error = context.getString(R.string.too_short_password)
                validate = false
            } else if (confirmPass.text.toString() != password.text.toString()) {
                confirmPass.error = context.getString(R.string.pass_are_not_equlals)
                validate = false
            }
            return validate
        }
    }
}