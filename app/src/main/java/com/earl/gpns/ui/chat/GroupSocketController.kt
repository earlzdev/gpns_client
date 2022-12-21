package com.earl.gpns.ui.chat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface GroupSocketController {

    fun initRecyclerAdapter(adapter: GroupMessagingRecyclerAdapter)

    fun updateLastMessageAuthorImageInGroup()

    fun markMessagesAsReadInGroup()

    class Base(

    ) : GroupSocketController {

        private var recyclerAdapter: GroupMessagingRecyclerAdapter? = null

        override fun initRecyclerAdapter(adapter: GroupMessagingRecyclerAdapter) {
            recyclerAdapter = adapter
        }

        override fun updateLastMessageAuthorImageInGroup() {
            CoroutineScope(Dispatchers.Main).launch {
                recyclerAdapter?.updateLastMessageAuthorImage()
            }
        }

        override fun markMessagesAsReadInGroup() {
            CoroutineScope(Dispatchers.Main).launch {
                recyclerAdapter?.markMessagesAsRead()
            }
        }
    }
}