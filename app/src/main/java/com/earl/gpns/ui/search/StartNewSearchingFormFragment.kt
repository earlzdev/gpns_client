package com.earl.gpns.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.databinding.FragmentStartNewSearchingFormBinding

class StartNewSearchingFormFragment : BaseFragment<FragmentStartNewSearchingFormBinding>() {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentStartNewSearchingFormBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
        binding.searchCompanionForm.setOnClickListener {
            navigator.startCompanionForm()
        }
        binding.searchDriverForm.setOnClickListener {
            navigator.startFirstDriverForm()
        }
    }

    companion object {

        fun newInstance() = StartNewSearchingFormFragment()
    }
}