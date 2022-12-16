package com.earl.gpns.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentDriverFormSecondBinding

class SecondDriverFormFragment : BaseFragment<FragmentDriverFormSecondBinding>() {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDriverFormSecondBinding.inflate(inflater, container, false)

    companion object {

        fun newInstance() = SecondDriverFormFragment()
    }
}