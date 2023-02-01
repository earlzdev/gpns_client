package com.earl.gpns.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.earl.gpns.databinding.FragmentPrivacyPolicyBinding
import com.earl.gpns.ui.core.BaseFragment

class FragmentPolicy : BaseFragment<FragmentPrivacyPolicyBinding>() {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener { navigator.back() }
    }

    companion object {
        fun newInstance() = FragmentPolicy()
    }
}