package com.earl.gpns.ui.rooms

import android.util.Log
import com.earl.gpns.ui.core.Keys
import com.earl.gpns.ui.core.SharedPreferenceManager
import com.earl.gpns.domain.mappers.GroupDomainToUiMapper
import com.earl.gpns.domain.mappers.GroupLastMessageDomainToUiMapper
import com.earl.gpns.domain.mappers.NewLastMessageInRoomDomainToUiMapper
import com.earl.gpns.domain.models.GroupDomain
import com.earl.gpns.domain.models.GroupLastMessageDomain
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.ui.models.GroupLastMessageUi
import com.earl.gpns.ui.models.GroupUi
import com.earl.gpns.ui.models.NewLastMessageInRoomUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface RoomsObservingSocketController {

    fun setRoomsRecyclerAdapter(recyclerAdapter: RoomsRecyclerAdapter)

    fun setGroupsRecyclerAdapter(recyclerAdapter: GroupsRecyclerAdapter)

    fun setViewModel(roomsViewModel: RoomsViewModel)

    fun setPreferenceManager(manager: SharedPreferenceManager)

    fun removeDeletedByAnotherUserRoomFromDb(roomId: String, contactName: String)

    fun updateLastMessageInRoomReadState(roomId: String)

    fun updateLastMessageInRoom(newLastMessage: NewLastMessageInRoomDomain)

    fun updateUserOnlineInRoomObserving(roomId: String, online: Int, lastAuthDate: String)

    fun updateLastMessageInGroup(newLastMessageInGroup: GroupLastMessageDomain)

    fun markAuthoredMessagesAsReadInGroup(groupId: String)

    fun addNewGroup(group: GroupDomain)

    class Base @Inject constructor(
        private val newLastMsgInRoomDomainToUiMapper: NewLastMessageInRoomDomainToUiMapper<NewLastMessageInRoomUi>,
        private val groupLastMessageDomainToUiMapper: GroupLastMessageDomainToUiMapper<GroupLastMessageUi>,
        private val groupDomainToUiMapper: GroupDomainToUiMapper<GroupUi>
    ) : RoomsObservingSocketController {

        private var roomsRecyclerAdapter: RoomsRecyclerAdapter? = null
        private var groupsRecyclerAdapter: GroupsRecyclerAdapter? = null
        private var viewModel: RoomsViewModel? = null
        private var preferenceManager: SharedPreferenceManager? = null

        override fun setRoomsRecyclerAdapter(recyclerAdapter: RoomsRecyclerAdapter) {
            roomsRecyclerAdapter = recyclerAdapter
        }

        override fun setGroupsRecyclerAdapter(recyclerAdapter: GroupsRecyclerAdapter) {
            groupsRecyclerAdapter = recyclerAdapter
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
                roomsRecyclerAdapter?.hideMessageAuthorUnreadIndicator(roomId)
            }
        }

        override fun updateLastMessageInRoom(newLastMessage: NewLastMessageInRoomDomain) {
            CoroutineScope(Dispatchers.Main).launch {
                val newLastMsgUi = newLastMessage.mapToUi(newLastMsgInRoomDomainToUiMapper)
                val room = roomsRecyclerAdapter?.currentList?.find { it.sameId(newLastMsgUi.provideRoomId()) }
                val currentPosition = roomsRecyclerAdapter?.currentList?.indexOf(room)
                if (room != null) {
                    roomsRecyclerAdapter?.updateLastMessage(newLastMsgUi.lastMessageForUpdate(), currentPosition!!)
                    roomsRecyclerAdapter?.swap(currentPosition!!)
                    if (!newLastMsgUi.isMessageRead()
                        && newLastMsgUi.isAuthoredMessage(preferenceManager?.getString(Keys.KEY_NAME) ?: "")) {
                        roomsRecyclerAdapter?.showMessageUnreadIndicator(currentPosition!!)
                    } else if (!newLastMsgUi.isMessageRead()
                        && !newLastMsgUi.isAuthoredMessage(preferenceManager?.getString(Keys.KEY_NAME) ?: "")) {
                        roomsRecyclerAdapter?.updateCounter(currentPosition!!)
                    } else if (newLastMsgUi.isMessageRead()){
                        roomsRecyclerAdapter?.clearCounter(newLastMsgUi.provideRoomId())
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
                roomsRecyclerAdapter?.changeUserOnlineInRoom(roomId, online, lastAuthDate)
            }
        }

        override fun updateLastMessageInGroup(newLastMessageInGroup: GroupLastMessageDomain) {
            CoroutineScope(Dispatchers.Main).launch {
                val lastMsgUi = newLastMessageInGroup.map(groupLastMessageDomainToUiMapper)
                if (lastMsgUi.isLastMessageRead()) {
                    viewModel?.increaseMessagesReadCounterInGroup(lastMsgUi.provideId(), 1)
                }
                val group = groupsRecyclerAdapter?.currentList?.find { it.sameId(lastMsgUi.provideId()) }
                val currentPosition = groupsRecyclerAdapter?.currentList?.indexOf(group)
                if (group != null && currentPosition != null) {
                    groupsRecyclerAdapter?.updateLastMessage(lastMsgUi.provideLastMessageForUpdateInGroup(), currentPosition)
                }
            }
        }

        override fun markAuthoredMessagesAsReadInGroup(groupId: String) {
            CoroutineScope(Dispatchers.Main).launch {
                groupsRecyclerAdapter?.markAuthoredMessagesAsRead(groupId)
            }
        }

        override fun addNewGroup(group: GroupDomain) {
            CoroutineScope(Dispatchers.Main).launch {}
        }
    }
}