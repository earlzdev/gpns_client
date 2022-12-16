package com.earl.gpns.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentCompanionFormDetailsBinding

class CompanionFormDetailsFragment : BaseFragment<FragmentCompanionFormDetailsBinding>() {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompanionFormDetailsBinding.inflate(inflater, container, false)

    companion object {

        fun newInstance() = CompanionFormDetailsFragment()
    }
}