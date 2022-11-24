package com.earl.gpns.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentChatBinding

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChatBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newMsgBtn.setOnClickListener {
            navigator.showProgressBar()
            navigator.usersFragment()
        }
    }

    companion object {

        fun newInstance() = ChatFragment()
    }
}