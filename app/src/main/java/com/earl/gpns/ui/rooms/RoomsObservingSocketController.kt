package com.earl.gpns.ui.rooms

import com.earl.gpns.core.Keys
import com.earl.gpns.core.SharedPreferenceManager
import com.earl.gpns.domain.mappers.NewLastMessageInRoomDomainToUiMapper
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.ui.models.NewLastMessageInRoomUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface RoomsObservingSocketController {

    fun setRecyclerAdapter(recyclerAdapter: RoomsRecyclerAdapter)

    fun setViewModel(roomsViewModel: RoomsViewModel)

    fun setPreferenceManager(manager: SharedPreferenceManager)

    fun removeDeletedByAnotherUserRoomFromDb(roomId: String, contactName: String)

    fun updateLastMessageInRoomReadState(roomId: String)

    fun updateLastMessageInRoom(newLastMessage: NewLastMessageInRoomDomain)

    fun updateUserOnlineInRoomObserving(roomId: String, online: Int, lastAuthDate: String)

    class Base @Inject constructor(
        private val newLastMsgInRoomDomainToUiMapper: NewLastMessageInRoomDomainToUiMapper<NewLastMessageInRoomUi>
    ) : RoomsObservingSocketController {

        private var adapter: RoomsRecyclerAdapter? = null
        private var viewModel: RoomsViewModel? = null
        private var preferenceManager: SharedPreferenceManager? = null

        override fun setRecyclerAdapter(recyclerAdapter: RoomsRecyclerAdapter) {
            adapter = recyclerAdapter
        }

        override fun setViewModel(roomsViewModel: RoomsViewModel) {
            viewModel = roomsViewModel
        }

        override fun setPreferenceManager(manager: SharedPreferenceManager) {
            preferenceManager = manager

        }

        override fun removeDeletedByAnotherUserRoomFromDb(roomId: String, contactName: String) {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel?.removeDeletedByAnotherUserRoomFromDb(roomId)
            }
        }

        override fun updateLastMessageInRoomReadState(roomId: String) {
            CoroutineScope(Dispatchers.Main).launch {
                adapter?.hideMessageAuthorUnreadIndicator(roomId)
            }
        }

        override fun updateLastMessageInRoom(newLastMessage: NewLastMessageInRoomDomain) {
            CoroutineScope(Dispatchers.Main).launch {
                val newLastMsgUi = newLastMessage.mapToUi(newLastMsgInRoomDomainToUiMapper)
                val room = adapter?.currentList?.find { it.sameId(newLastMsgUi.provideRoomId()) }
                val currentPosition = adapter?.currentList?.indexOf(room)
                if (room != null) {
                    adapter?.updateLastMessage(newLastMsgUi.lastMessageForUpdate(), currentPosition!!)
                    adapter?.swap(currentPosition!!)
                    if (!newLastMsgUi.isMessageRead()
                        && newLastMsgUi.isAuthoredMessage(preferenceManager?.getString(Keys.KEY_NAME) ?: "")) {
                        adapter?.showMessageUnreadIndicator(currentPosition!!)
                    } else if (!newLastMsgUi.isMessageRead()
                        && !newLastMsgUi.isAuthoredMessage(preferenceManager?.getString(Keys.KEY_NAME) ?: "")) {
                        adapter?.updateCounter(currentPosition!!)
                    } else if (newLastMsgUi.isMessageRead()){
                        adapter?.clearCounter(newLastMsgUi.provideRoomId())
                    }
                }
            }
        }

        override fun updateUserOnlineInRoomObserving(
            roomId: String,
            online: Int,
            lastAuthDate: String
        ) {
            CoroutineScope(Dispatchers.Main).launch {
                adapter?.changeUserOnlineInRoom(roomId, online, lastAuthDate)
            }
        }
    }
}