package com.earl.gpns.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.core.AuthResultListener
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.core.OperationResultListener
import com.earl.gpns.databinding.FragmentMainBinding
import retrofit2.HttpException

class MainFragment : BaseFragment<FragmentMainBinding>(), OperationResultListener, AuthResultListener {

    private lateinit var viewModel: MainFragmentViewModel

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainFragmentViewModel::class.java]
        authenticate()
        binding.testBtn.setOnClickListener {
            getInfo()
        }
    }

    private fun getInfo() {
        viewModel.getSecretInfo(preferenceManager.getString(Keys.KEY_JWT) ?: "", this)
    }

    private fun authenticate() {
        navigator.showProgressBar()
        viewModel.authenticate(preferenceManager.getString(Keys.KEY_JWT) ?: "", this)
    }

    override fun <T> success(value: T) {
        navigator.log(" MainFragment secces ${value.toString()}")
    }

    override fun fail(e: Exception) {
        navigator.log("MainFragment fail $e")
    }

    override fun <T> authorized(value: T) {
        navigator.log("MainFragment authenticate success")
        navigator.hideProgressBar()
    }

    override fun unauthorized(e: HttpException) {
        navigator.log("MainFragment authenticate fail unauthorized $e")
        navigator.hideProgressBar()
    }

    override fun unknownError(e: Exception) {
        navigator.log("MainFragment authenticate fail unknownError $e")
        navigator.hideProgressBar()
    }

    companion object {

        fun newInstance() = MainFragment()
    }
}