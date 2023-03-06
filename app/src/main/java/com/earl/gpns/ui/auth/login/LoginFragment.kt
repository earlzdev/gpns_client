package com.earl.gpns.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.earl.gpns.R
import com.earl.gpns.data.models.remote.requests.LoginRequest
import com.earl.gpns.databinding.FragmentLoginBinding
import com.earl.gpns.domain.AuthResultListener
import com.earl.gpns.domain.LoginResultListener
import com.earl.gpns.domain.RegisterResultListener
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.HttpExceptionMessages
import com.earl.gpns.ui.core.Keys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(), AuthResultListener {

    private lateinit var viewModel: LoginViewModel
    @Inject lateinit var loginFormValidation: LoginFormValidation

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
        binding.finishLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val isFormValid = loginFormValidation.validate(
            binding.emailInput,
            binding.passwordInput
        )
        if (isFormValid) {
            navigator.showProgressBar()
            viewModel.login(
                LoginRequest(
                    binding.emailInput.text.toString().trim(),
                    binding.passwordInput.text.toString().trim()
                ), this)
        }
    }

    override fun <T> authorized(value: T) {
        preferenceManager.putString(Keys.KEY_JWT, value.toString())
        preferenceManager.putBoolean(Keys.KEY_IS_SIGNED_UP, true)
        navigator.hideProgressBar()
        navigator.mainFragment()
    }

    override fun unauthorized(e: HttpException) {
        navigator.hideProgressBar()
        lifecycleScope.launch {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.error))
                .setMessage(getString(R.string.auth_error))
                .setPositiveButton(getString(R.string.try_again)) { p0, p1 -> }
                .create()
                .show()
        }
    }

    override fun unknownError(e: Exception) {
        navigator.hideProgressBar()
        lifecycleScope.launch {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.error))
                .setMessage(String.format(getString(R.string.erorr), e.message))
                .setPositiveButton(getString(R.string.try_again)) { p0, p1 -> }
                .create()
                .show()
        }
    }

    override fun httpError(message: String) {
        when(message) {
            HttpExceptionMessages.noUserWithSuchEmail -> {
                showAlertDialogWithError(getString(R.string.user_with_such_email_is_not_found))
            }
            HttpExceptionMessages.wrongPassword -> {
                showAlertDialogWithError(getString(R.string.wrond_pas))
            }
        }
    }

    private fun showAlertDialogWithError(message: String) {
        navigator.hideProgressBar()
        lifecycleScope.launch {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.error))
                .setMessage(message)
                .setPositiveButton(getString(R.string.try_again)){ p0, p1 -> }
                .create()
                .show()
        }
    }

    companion object {

        fun newInstance() = LoginFragment()
    }
}