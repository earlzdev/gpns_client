package com.earl.gpns.ui.chat

import com.earl.gpns.ui.core.SharedPreferenceManager

interface RoomMessangerSocketController {

    fun setAdapter(recyclerAdapter: ChatRecyclerAdapter)

    fun setViewModel(roomMessangerViewModel: RoomMessangerViewModel)

    fun setPreferenceManager(manager: SharedPreferenceManager)

    class Base(

    ) : RoomMessangerSocketController {

        private var adapter : ChatRecyclerAdapter? = null
        private var viewModel : RoomMessangerViewModel? = null
        private var preferenceManager : SharedPreferenceManager? = null

        override fun setAdapter(recyclerAdapter: ChatRecyclerAdapter) {
            adapter = recyclerAdapter
        }

        override fun setViewModel(roomMessangerViewModel: RoomMessangerViewModel) {
            viewModel = roomMessangerViewModel
        }

        override fun setPreferenceManager(manager: SharedPreferenceManager) {
            preferenceManager = manager
        }
    }
}