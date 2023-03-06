package com.earl.gpns.ui.auth.login

import android.widget.EditText
import com.earl.gpns.R
import com.earl.gpns.ui.core.isEmailValid
import javax.inject.Inject

interface LoginFormValidation {

    fun validate(
        login: EditText,
        password: EditText
    ) : Boolean

    class Base @Inject constructor() : LoginFormValidation {

        override fun validate(login: EditText, password: EditText): Boolean {
            val context = login.context
            var validate = true
            if (login.text.isEmpty()) {
                login.error = context.getString(R.string.field_cannot_be_empty)
                validate = false
            } else if (password.text.isEmpty()) {
                password.error = context.getString(R.string.field_cannot_be_empty)
                validate = false
            } else if (!login.text.toString().isEmailValid()) {
                login.error = context.getString(R.string.incorrect_email)
                validate = false
            } else if (password.text.length < 6) {
                password.error = context.getString(R.string.too_short_password)
                validate = false
            }
            return validate
        }
    }
}