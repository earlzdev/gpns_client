package com.earl.gpns.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentDriverFormDetailsBinding

class DriverFormDetailsFragment : BaseFragment<FragmentDriverFormDetailsBinding>() {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDriverFormDetailsBinding.inflate(inflater, container, false)

    companion object {

        fun newInstance() = DriverFormDetailsFragment()
    }
}