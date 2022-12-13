package com.earl.gpns.ui.chat

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.earl.gpns.R
import com.earl.gpns.core.*
import com.earl.gpns.databinding.FragmentChatBinding
import com.earl.gpns.ui.models.ChatInfo
import com.earl.gpns.ui.models.MessageUi
import com.earl.gpns.ui.models.NewRoomDtoUi
import com.earl.gpns.ui.models.TypingMessageDtoUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ChatFragment(
    private val chatInfo: ChatInfo,
) : BaseFragment<FragmentChatBinding>(),
    MarkMessageAsReadCallback,
    UpdateOnlineInChatCallback,
    IsUserTypingMessageCallback
{

    private lateinit var viewModel: ChatViewModel
    private var newRoomId = ""
    private var newRoomFlag = false
    private lateinit var recyclerAdapter: ChatRecyclerAdapter
    private var typingStarted = false

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChatBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        initRoomId()
        initChatInfo()
        initMessagingService()
        recycler()
        backPressedCallback()
        typingMessageListener()
        binding.sendMsgBtn.setOnClickListener {
            if (!binding.msgEdittext.text.isNullOrEmpty()) {
                sendMessage()
                binding.msgEdittext.text.clear()
            }
        }
        binding.backBtn.setOnClickListener {
            viewModel.closeMessagingSocket()
            navigator.back()
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
            newRoomId,
            this,
            this,
            this
        )
    }

    private fun addRoom() {
        val request = NewRoomDtoUi.Base(
            newRoomId,
            chatInfo.chatTitle,
            chatInfo.chatImage,
            preferenceManager.getString(Keys.KEY_NAME) ?: "",
            chatInfo.chatTitle,
            binding.msgEdittext.text.toString(),
            preferenceManager.getString(Keys.KEY_NAME) ?: "",
            chatInfo.userOnline,
            ""
        )
        viewModel.addRoom(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            request
        )
        viewModel.addNewRoomToLocalDatabase(request)
        val currentDate = Date()
        val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val timeText: String = timeFormat.format(currentDate)
        val dateText = dateFormat.format(currentDate)
        val message = MessageUi.Base(
            UUID.randomUUID().toString(),
            newRoomId,
            preferenceManager.getString(Keys.KEY_USER_ID) ?: "",
            timeText,
            binding.msgEdittext.text.toString(),
            dateText,
            MSG_UNREAD_KEY
        )
        viewModel.sendMessage(message, preferenceManager.getString(Keys.KEY_JWT) ?: "")
    }

    private fun recycler() {
        recyclerAdapter = ChatRecyclerAdapter(preferenceManager.getString(Keys.KEY_USER_ID) ?: "")
        binding.messagesRecycler.adapter = recyclerAdapter
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.stackFromEnd = true
        binding.messagesRecycler.layoutManager = linearLayoutManager
        lifecycleScope.launchWhenStarted {
            viewModel._messages
                .onEach { messages ->
                    if (messages.isNotEmpty() && !messages.last().isAuthoredMessage(preferenceManager.getString(Keys.KEY_USER_ID) ?: "")) {
                        val unreadMessagesList = messages.filter { !it.isMessageRead() }
                        if (unreadMessagesList.isNotEmpty()) {
                            markMessagesAsRead(newRoomId)
                        }
                    }
                    recyclerAdapter.submitList(messages)
                    binding.messagesRecycler.layoutManager?.smoothScrollToPosition(
                        binding.messagesRecycler,
                        null,
                        messages.size
                    )
                }
                .collect()
        }
    }

    private fun markMessagesAsRead(roomId: String) {
        viewModel.markMessagesAsRead(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            roomId
        )
    }

    override fun markAsRead() {
        lifecycleScope.launch(Dispatchers.Main) {
            recyclerAdapter.markMessagesAsRead()
        }
    }

    private fun sendMessage() {
        if (newRoomFlag) {
            addRoom()
            newRoomFlag = false
        } else {
            val currentDate = Date()
            val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()) // todo don't need anymore
            val timeText: String = timeFormat.format(currentDate)
            val dateText = dateFormat.format(currentDate)
            val message = MessageUi.Base(
                UUID.randomUUID().toString(),
                newRoomId,
                preferenceManager.getString(Keys.KEY_USER_ID) ?: "",
                timeText,
                binding.msgEdittext.text.toString(),
                dateText,
                MSG_UNREAD_KEY
            )
            viewModel.sendMessage(
                message,
                preferenceManager.getString(Keys.KEY_JWT) ?: ""
            )
        }
    }

    private fun initChatInfo() {
        binding.contactName.text = chatInfo.chatTitle
        binding.userAvatar.setImageResource(R.drawable.default_avatar)
        if (chatInfo.userOnline == 1) {
            binding.onlineIndicator.isVisible = true
            binding.contactLastAuth.isVisible = false
            Log.d("tag", "initChatInfo: last uath : ${chatInfo.userOnline} ${chatInfo.userLastAuth}")
        } else {
            binding.onlineIndicator.isVisible = false
            binding.contactLastAuth.text = chatInfo.userLastAuth
            binding.contactLastAuth.isVisible = true
            Log.d("tag", "initChatInfo: last uath : ${chatInfo.userLastAuth}")
        }
    }

    override fun updateOnline(online: Int, lastAuth: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            if (online == 1) {
                binding.onlineIndicator.isVisible = true
                binding.contactLastAuth.isVisible = false
            } else {
                binding.onlineIndicator.isVisible = false
                binding.contactLastAuth.text = lastAuth
                binding.contactLastAuth.isVisible = true
            }
        }
    }

    override fun isTypingMessage(value: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            if (value == 1) {
                binding.contactLastAuth.isVisible = false
                binding.isTyping.text = "${chatInfo.chatTitle} is typing..."
                binding.isTyping.isVisible = true
            } else {
                binding.isTyping.isVisible = false
            }
        }
    }

    private fun typingMessageListener() {
        binding.msgEdittext.afterTextChangedDelayed {
            navigator.log("TYPING STOPPED $it")
        }
    }

    private fun EditText.afterTextChangedDelayed(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            var timer: CountDownTimer? = null

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!typingStarted) {
                    navigator.log("STARTED TYPING")
                    viewModel.sendTypeMessageResponse(
                        preferenceManager.getString(Keys.KEY_JWT) ?: "",
                        TypingMessageDtoUi.Base(
                            newRoomId,
                            preferenceManager.getString(Keys.KEY_NAME) ?: "",
                            1
                        )
                    )
                    typingStarted = true
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                timer?.cancel()
                timer = object : CountDownTimer(1000, 1500) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        afterTextChanged.invoke(editable.toString())
                        viewModel.sendTypeMessageResponse(
                            preferenceManager.getString(Keys.KEY_JWT) ?: "",
                            TypingMessageDtoUi.Base(
                                newRoomId,
                                preferenceManager.getString(Keys.KEY_NAME) ?: "",
                                0
                            )
                        )
                        typingStarted = false
                    }
                }.start()
            }
        })
    }

    private fun backPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.closeMessagingSocket()
                navigator.back()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

    companion object {

        fun newInstance(chatInfo: ChatInfo) = ChatFragment(chatInfo)
        private const val MSG_UNREAD_KEY = 0
    }
}