package com.earl.gpns.ui.usersFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.Keys
import com.earl.gpns.databinding.FragmentUsersBinding
import com.earl.gpns.ui.models.ChatInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentUsers : BaseFragment<FragmentUsersBinding>(), UserClickListener {

    private lateinit var viewModel: UsersViewModel

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUsersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[UsersViewModel::class.java]
        fetchUsersList()
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
    }

    private fun fetchUsersList() {
        val adapter = UsersRecyclerAdapter(this)
        binding.usersRecycler.adapter = adapter
        viewModel.fetchUsers(preferenceManager.getString(Keys.KEY_JWT) ?: "")
        viewModel.observeUsersListLiveData(this) {
            adapter.submitList(it)
        }
        navigator.hideProgressBar()
    }

    override fun joinChat(chatInfo: ChatInfo) {
        navigator.showProgressBar()
        val existedRooms = viewModel.provideExistedRoomsList()
        val isRoomExists = existedRooms?.find { it.chatInfo().chatTitle == chatInfo.chatTitle }
        if (isRoomExists == null) {
            navigator.chat(chatInfo)
        } else {
            navigator.chat(isRoomExists.chatInfo())
        }
        navigator.hideProgressBar()
    }

    companion object {

        fun newInstance() = FragmentUsers()
    }
}