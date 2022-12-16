package com.earl.gpns.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentCompanionFormBinding

class CompanionFormFragment : BaseFragment<FragmentCompanionFormBinding>() {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompanionFormBinding.inflate(inflater, container, false)

    companion object {

        fun newInstance() = CompanionFormFragment()
    }
}