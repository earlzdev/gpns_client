package com.earl.gpns.ui.chat

import com.earl.gpns.core.SharedPreferenceManager

interface ChatSocketController {

    fun setAdapter(recyclerAdapter: ChatRecyclerAdapter)

    fun setViewModel(chatViewModel: ChatViewModel)

    fun setPreferenceManager(manager: SharedPreferenceManager)

    fun updateUserTypingMessageState(typingState: Int)

    fun updateUserOnlineStatusInChat(onlineStatus: Int)

    fun markMessagesAsReadInChat()

    class Base(

    ) : ChatSocketController {

        private var adapter : ChatRecyclerAdapter? = null
        private var viewModel : ChatViewModel? = null
        private var preferenceManager : SharedPreferenceManager? = null

        override fun setAdapter(recyclerAdapter: ChatRecyclerAdapter) {
            adapter = recyclerAdapter
        }

        override fun setViewModel(chatViewModel: ChatViewModel) {
            viewModel = chatViewModel
        }

        override fun setPreferenceManager(manager: SharedPreferenceManager) {
            preferenceManager = manager
        }

        override fun updateUserTypingMessageState(typingState: Int) {
            TODO("Not yet implemented")
        }

        override fun updateUserOnlineStatusInChat(onlineStatus: Int) {
            TODO("Not yet implemented")
        }

        override fun markMessagesAsReadInChat() {
            TODO("Not yet implemented")
        }
    }
}