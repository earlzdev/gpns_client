package com.earl.gpns.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.domain.AuthResultListener
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.Keys
import com.earl.gpns.data.models.remote.requests.LoginRequest
import com.earl.gpns.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(), AuthResultListener {

    private lateinit var viewModel: LoginViewModel

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
            navigator.showProgressBar()
            login()
        }
    }

    private fun login() {
        viewModel.login(
            LoginRequest(
            binding.emailInput.text.toString(),
            binding.passwordInput.text.toString()
        ), this)
    }

    override fun <T> authorized(value: T) {
        preferenceManager.putString(Keys.KEY_JWT, value.toString())
        preferenceManager.putBoolean(Keys.KEY_IS_SIGNED_UP, true)
        navigator.hideProgressBar()
        navigator.mainFragment()
    }

    override fun unauthorized(e: HttpException) {
        navigator.hideProgressBar()
    }

    override fun unknownError(e: Exception) {
        navigator.hideProgressBar()
    }

    companion object {

        fun newInstance() = LoginFragment()
    }
}