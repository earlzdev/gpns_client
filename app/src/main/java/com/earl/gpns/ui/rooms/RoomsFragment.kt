package com.earl.gpns.ui.rooms

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentRoomsBinding
import com.earl.gpns.ui.models.ChatInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RoomsFragment : BaseFragment<FragmentRoomsBinding>(), OnRoomClickListener {

    private lateinit var viewModel: RoomsViewModel

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRoomsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RoomsViewModel::class.java]
        initSession()
        recycler()
        binding.newMsgBtn.setOnClickListener {
            navigator.showProgressBar()
            navigator.usersFragment()
        }
        binding.chatChapter.setOnClickListener {
            viewModel.clearDatabase()
            Toast.makeText(requireContext(), "Database cleared", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initSession() {
        navigator.showProgressBar()
        viewModel.initChatSocket(preferenceManager.getString(Keys.KEY_JWT) ?: "")
    }

    override fun joinRoom(chatInfo: ChatInfo) {
        navigator.chat(chatInfo)
    }

    override fun deleteRoom(chatInfo: ChatInfo) {
        viewModel.deleteRoom(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            chatInfo.roomId!!
        )
        Toast.makeText(requireContext(), "Room deleted", Toast.LENGTH_SHORT).show()
    }

    private fun recycler() {
        val adapter = RoomsRecyclerAdapter(this)
        binding.chatRecycler.adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel._rooms
                .onEach {
                        rooms ->
                    Log.d("tag", "recycler: new room $rooms")
                    adapter.submitList(rooms)
                }
                .collect()
        }
        navigator.hideProgressBar()
    }

    companion object {

        fun newInstance() = RoomsFragment()
    }
}