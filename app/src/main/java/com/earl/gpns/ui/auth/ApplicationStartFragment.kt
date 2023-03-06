package com.earl.gpns.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.earl.gpns.databinding.FragmentApplicationStartBinding
import com.earl.gpns.ui.core.BaseFragment

class ApplicationStartFragment : BaseFragment<FragmentApplicationStartBinding>() {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentApplicationStartBinding.inflate(inflater, container, false)

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

        fun newInstance() = ApplicationStartFragment()
    }
}