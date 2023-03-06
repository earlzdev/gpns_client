package com.earl.gpns.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.earl.gpns.R
import com.earl.gpns.data.models.remote.requests.RegisterRequest
import com.earl.gpns.databinding.FragmentRegistrationBinding
import com.earl.gpns.domain.AuthResultListener
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.HttpExceptionMessages
import com.earl.gpns.ui.core.Keys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentRegistrationBinding>(), AuthResultListener {

    private lateinit var viewModel: SignUpViewModel
    @Inject lateinit var registerFormValidation: RegisterFormValidation

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegistrationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
        binding.finishLogin.setOnClickListener {
            register()
        }
    }

    private fun showAttentionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.attention))
            .setMessage(getString(R.string.attentoin_alert_message))
            .setPositiveButton( getString(R.string.i_work_on_d_factory)
            ) { p0, p1 ->
                navigator.showProgressBar()
                viewModel.register(
                    RegisterRequest(
                        binding.emailInput.text.toString().trim(),
                        binding.userNameInput.text.toString(),
                        binding.userPasswordInput.text.toString().trim()
                    ), this)
            }
            .setNegativeButton(getString(R.string.exite)) { p0, p1 ->
                requireActivity().finish()
            }
            .create()
            .show()
    }

    private fun register() {
        val isFormValid = registerFormValidation.validate(
            binding.emailInput,
            binding.userNameInput,
            binding.userPasswordInput,
            binding.userPasswordConfirm
        )
        if (isFormValid) showAttentionDialog()
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

    private fun showErrorAlertDialog(message: String) {
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

    override fun httpError(message: String) {
        when (message) {
            HttpExceptionMessages.userWithSuchEmailIsAlreadyExists -> {
                showErrorAlertDialog(getString(R.string.user_eamil_exists))
            }
            HttpExceptionMessages.userWithSuchUsernameIsAlreadyExists -> {
                showErrorAlertDialog(getString(R.string.user_username_exists))
            }
        }
    }

    companion object {

        fun newInstance() = SignUpFragment()
    }
}