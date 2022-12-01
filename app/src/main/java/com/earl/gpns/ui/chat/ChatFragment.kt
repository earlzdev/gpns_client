package com.earl.gpns.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentChatBinding
import com.earl.gpns.ui.OnBackPressedListener
import com.earl.gpns.ui.models.ChatInfo
import com.earl.gpns.ui.models.MessageUi
import com.earl.gpns.ui.models.NewRoomDtoUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ChatFragment(
    private val chatInfo: ChatInfo,
) : BaseFragment<FragmentChatBinding>(), OnBackPressedListener {

    private lateinit var viewModel: ChatViewModel
    private var newRoomId = ""
    private var newRoomFlag = false

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChatBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        initRoomId()
        initMessagingService()
        recycler()
        binding.testBtn.setOnClickListener {
            if (!binding.testEdttext.text.isNullOrEmpty()) {
                sendMessage()
                binding.testEdttext.text.clear()
            }
        }
    }

    private fun initRoomId() {
        if (chatInfo.roomId == null) {
            newRoomId = UUID.randomUUID().toString()
            newRoomFlag = true
        } else {
            newRoomId = chatInfo.roomId
        }
    }

    private fun initMessagingService() {
        viewModel.initMessagingSocket(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            newRoomId
        )
    }

    private fun addRoom() {
        val request = NewRoomDtoUi.Base(
            newRoomId,
            chatInfo.chatTitle,
            chatInfo.chatImage,
            preferenceManager.getString(Keys.KEY_NAME) ?: "",
            chatInfo.chatTitle,
            binding.testEdttext.text.toString(),
            preferenceManager.getString(Keys.KEY_NAME) ?: ""
        )
        viewModel.addRoom(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            request
        )
        viewModel.addNewRoomToLocalDatabase(request)
        val currentDate = Date()
        val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat: DateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
        val timeText: String = timeFormat.format(currentDate)
        val dateText = dateFormat.format(currentDate)
        val message = MessageUi.Base(
            UUID.randomUUID().toString(),
            newRoomId,
            preferenceManager.getString(Keys.KEY_USER_ID) ?: "",
            timeText,
            binding.testEdttext.text.toString(),
            dateText
        )
        viewModel.sendMessage(message, preferenceManager.getString(Keys.KEY_JWT) ?: "")
    }

    private fun recycler() {
        val adapter = ChatRecyclerAdapter(preferenceManager.getString(Keys.KEY_USER_ID) ?: "")
        binding.messagesRecycler.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.stackFromEnd = true
        binding.messagesRecycler.layoutManager = linearLayoutManager
        lifecycleScope.launchWhenStarted {
            viewModel._messages
                .onEach { messages ->
                    adapter.submitList(messages)
                    binding.messagesRecycler.layoutManager?.smoothScrollToPosition(
                        binding.messagesRecycler,
                        null,
                        messages.size
                    )
                }
                .collect()
        }
    }

    private fun sendMessage() {
        if (newRoomFlag) {
            addRoom()
            newRoomFlag = false
        } else {
            val currentDate = Date()
            val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateFormat: DateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
            val timeText: String = timeFormat.format(currentDate)
            val dateText = dateFormat.format(currentDate)
            val message = MessageUi.Base(
                UUID.randomUUID().toString(),
                newRoomId,
                preferenceManager.getString(Keys.KEY_USER_ID) ?: "",
                timeText,
                binding.testEdttext.text.toString(),
                dateText
            )
            viewModel.sendMessage(
                message,
                preferenceManager.getString(Keys.KEY_JWT) ?: ""
            )
        }
    }

    override fun onBackPressed() {
        viewModel.closeMessagingSocket()
        navigator.back()
    }

    companion object {

        fun newInstance(chatInfo: ChatInfo) = ChatFragment(chatInfo)
    }
}