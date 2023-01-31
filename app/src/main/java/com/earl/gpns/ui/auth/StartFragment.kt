package com.earl.gpns.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.databinding.FragmentStartBinding

class StartFragment : BaseFragment<FragmentStartBinding>() {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentStartBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logInButton.setOnClickListener {
            navigator.login()
        }
        binding.signUpButton.setOnClickListener {
            navigator.register()
        }
    }

    companion object {

        fun newInstance() = StartFragment()
    }
}