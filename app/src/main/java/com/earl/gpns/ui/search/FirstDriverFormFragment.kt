package com.earl.gpns.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentDriverFormFirstBinding

class FirstDriverFormFragment : BaseFragment<FragmentDriverFormFirstBinding>() {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDriverFormFirstBinding.inflate(inflater, container, false)

    companion object {

        fun newInstance() = FirstDriverFormFragment()
    }
}