package com.earl.gpns.ui.rooms

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.core.UpdateLastMessageInRoomCallback
import com.earl.gpns.databinding.FragmentRoomsBinding
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.ui.models.ChatInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomsFragment: BaseFragment<FragmentRoomsBinding>(), OnRoomClickListener, UpdateLastMessageInRoomCallback {

    private lateinit var viewModel: RoomsViewModel
    private lateinit var adapter: RoomsRecyclerAdapter

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRoomsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RoomsViewModel::class.java]
        initSession()
        recycler()
        backPressedCallback()
        binding.testUsername.text = preferenceManager.getString(Keys.KEY_NAME) ?: ""
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
        viewModel.initChatSocket(preferenceManager.getString(Keys.KEY_JWT) ?: "", this)
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
        adapter = RoomsRecyclerAdapter(this)
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

    override fun update(newLastMessage: NewLastMessageInRoomDomain) {
        Log.d("tag", "update: new last msg -> $newLastMessage")
        lifecycleScope.launch(Dispatchers.Main) {
            adapter.updateLastMessage(newLastMessage)
            adapter.swapElements(newLastMessage)
        }
    }

    private fun RoomsRecyclerAdapter.updateLastMessage(newLastMessage: NewLastMessageInRoomDomain) {
        val room = this.currentList.find { it.sameId(newLastMessage.provideRoomId()) }
        if (room != null) {
            val position = this.currentList.indexOf(room)
            this.updateLastMessage(newLastMessage, position)
        }
    }

    private fun RoomsRecyclerAdapter.swapElements(newLastMessage: NewLastMessageInRoomDomain) {
        val room = this.currentList.find { it.sameId(newLastMessage.provideRoomId()) }
        if (room != null) {
            val currentPosition = this.currentList.indexOf(room)
            this.swap(currentPosition, room)
        }
    }

    private fun backPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigator.exit()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

    companion object {

        fun newInstance() = RoomsFragment()
        private const val FIRST_POSITION = 1
    }
}