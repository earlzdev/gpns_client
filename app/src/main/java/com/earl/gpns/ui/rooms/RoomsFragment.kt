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
import com.earl.gpns.R
import com.earl.gpns.core.*
import com.earl.gpns.databinding.FragmentRoomsBinding
import com.earl.gpns.domain.mappers.NewLastMessageInRoomDomainToUiMapper
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.ui.models.ChatInfo
import com.earl.gpns.ui.models.NewLastMessageInRoomUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RoomsFragment : BaseFragment<FragmentRoomsBinding>(),
    OnRoomClickListener,
    UpdateLastMessageInRoomCallback,
    LastMessageReadStateCallback,
    DeleteRoomCallback,
    UpdateOnlineInRoomCallback {

    private lateinit var viewModel: RoomsViewModel
    private lateinit var adapter: RoomsRecyclerAdapter
    @Inject
    lateinit var newLastMsgInRoomDomainToUiMapper: NewLastMessageInRoomDomainToUiMapper<NewLastMessageInRoomUi>

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRoomsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RoomsViewModel::class.java]
        initSession()
        recycler()
        backPressedCallback()
        binding.testUsername.text = preferenceManager.getString(Keys.KEY_NAME)
        binding.newMsgBtn.setOnClickListener {
            navigator.showProgressBar()
            navigator.usersFragment()
        }
        binding.chatChapter.setOnClickListener {
            // todo refactor !!!
            viewModel.clearDatabase()
            Toast.makeText(requireContext(), "Database cleared", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initSession() {
        navigator.showProgressBar()
        viewModel.initChatSocket(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            this,
            this,
            this,
            this
        )
    }

    override fun joinRoom(chatInfo: ChatInfo) {
        navigator.chat(chatInfo)
        adapter.clearCounter(chatInfo.roomId ?: "")
        viewModel.markAuthoredMessageAsRead(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            chatInfo.roomId ?: "",
            chatInfo.chatTitle
        )
        viewModel.updateLastMsgReadState(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            chatInfo.roomId ?: ""
        )
    }

    override fun deleteRoom(chatInfo: ChatInfo) {
        viewModel.deleteRoom(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            chatInfo.roomId!!
        )
        Toast.makeText(requireContext(), getString(R.string.room_with_this_user_successfully_removed), Toast.LENGTH_SHORT).show()
    }

    private fun recycler() {
        adapter = RoomsRecyclerAdapter(this)
        binding.chatRecycler.adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel._rooms
                .onEach {
                        rooms ->
                    if (rooms.isNotEmpty()) {
                        rooms.forEach {
                            if (it.isLastMessageAuthorEqualsCurrentUser() && !it.isLastMsgRead()) {
                                it.showUnreadMsgIndicator()
                                it.clearUnreadMsgCounter()
                            }
                        }
                    }
                    adapter.submitList(rooms)
                }
                .collect()
        }
        navigator.hideProgressBar()
    }

    override fun markAuthoredMessageAsRead(roomId: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            adapter.hideMessageAuthorUnreadIndicator(roomId)
        }
    }

    override fun updateLastMessage(newLastMessage: NewLastMessageInRoomDomain) {
        val newLastMsgUi = newLastMessage.mapToUi(newLastMsgInRoomDomainToUiMapper)
        lifecycleScope.launch(Dispatchers.Main) {
            val room = adapter.currentList.find { it.sameId(newLastMsgUi.provideRoomId()) }
            val currentPosition = adapter.currentList.indexOf(room)
            if (room != null) {
                adapter.updateLastMessage(newLastMsgUi.lastMessageForUpdate(), currentPosition)
                adapter.swap(currentPosition)
                if (!newLastMsgUi.isMessageRead()
                    && newLastMsgUi.isAuthoredMessage(preferenceManager.getString(Keys.KEY_NAME) ?: "")) {
                    adapter.showMessageUnreadIndicator(currentPosition)
                    navigator.log("updateLastMessage authored unread message got")
                } else if (!newLastMsgUi.isMessageRead()
                    && !newLastMsgUi.isAuthoredMessage(preferenceManager.getString(Keys.KEY_NAME) ?: "")) {
                    adapter.updateCounter(currentPosition)
                    navigator.log("updateLastMessage not authored unread message got")
                } else if (newLastMsgUi.isMessageRead()){
                    adapter.clearCounter(newLastMsgUi.provideRoomId())
                    navigator.log("updateLastMessage read message got")
                }
            }
        }
    }

    override fun removeRoom(roomId: String, contactName: String) {
        viewModel.removeRoomFromDb(roomId)
        lifecycleScope.launch(Dispatchers.Main) {
            if (preferenceManager.getString(Keys.KEY_NAME) != contactName) {
                Toast.makeText(requireContext(), "Пользователь $contactName удалил с Вами диалог", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun updateOnline(roomId: String, online: Int, lastAuthDate: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            adapter.changeUserOnlineInRoom(roomId, online, lastAuthDate)
            Log.d("tag", "updateOnline: in room fragment done -> $online")
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
    }
}