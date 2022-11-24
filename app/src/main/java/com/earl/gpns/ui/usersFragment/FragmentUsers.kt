package com.earl.gpns.ui.usersFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentUsersBinding

class FragmentUsers : BaseFragment<FragmentUsersBinding>() {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUsersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator.hideProgressBar()
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
    }

    companion object {

        fun newInstance() = FragmentUsers()
    }
}