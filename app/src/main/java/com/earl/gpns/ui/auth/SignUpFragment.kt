package com.earl.gpns.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.domain.AuthResultListener
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.Keys
import com.earl.gpns.data.models.remote.requests.RegisterRequest
import com.earl.gpns.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentRegistrationBinding>(), AuthResultListener {

    private lateinit var viewModel: SignUpViewModel

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
            navigator.showProgressBar()
            register()
        }
    }

    private fun register() {
        viewModel.register(
            RegisterRequest(
             binding.emailInput.text.toString(),
            binding.userNameInput.text.toString(),
            binding.userPasswordInput.text.toString()
            ), this)
    }

    override fun <T> authorized(value: T) {
        navigator.log("token ${value.toString()}")
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

        fun newInstance() = SignUpFragment()
    }
}