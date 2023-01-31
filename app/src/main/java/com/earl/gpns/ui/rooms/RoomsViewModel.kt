package com.earl.gpns.ui.rooms

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.domain.*
import com.earl.gpns.domain.mappers.GroupDomainToUiMapper
import com.earl.gpns.domain.mappers.GroupMessagesCounterDomainToUimapper
import com.earl.gpns.domain.mappers.MessageDomainToUiMapper
import com.earl.gpns.domain.mappers.RoomDomainToUiMapper
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.domain.webSocketActions.services.RoomsObservingSocketService
import com.earl.gpns.domain.SocketOperationResultListener
import com.earl.gpns.ui.mappers.RoomDomainToNewRoomDomainMapper
import com.earl.gpns.ui.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RoomsViewModel @Inject constructor(
    private val interactor: Interactor,
    private val roomDomainToUiMapper: RoomDomainToUiMapper<RoomUi>,
    private val roomDomainToNewRoomDomainMapper: RoomDomainToNewRoomDomainMapper<NewRoomDtoDomain>,
    private val messageDomainToUiMapper: MessageDomainToUiMapper<MessageUi>,
    private val groupDomainToUiMapper: GroupDomainToUiMapper<GroupUi>,
    private val groupMessagesCounterDomainToUiMapper: GroupMessagesCounterDomainToUimapper<GroupMessagesCounterUi>
) : ViewModel() {

    private val rooms: MutableStateFlow<List<RoomUi>> = MutableStateFlow(emptyList())
    val _rooms = rooms.asStateFlow()
    private val groupsLiveData = MutableLiveData<List<GroupUi>>()
    private val groups: MutableStateFlow<List<GroupUi>> = MutableStateFlow(emptyList())
    val _groups = groups.asStateFlow()

    private fun fetchRooms(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchRooms(token).map { it.map(roomDomainToUiMapper) }
            withContext(Dispatchers.Main) {
                if (list.isNotEmpty()) {
                    rooms.value = list
                }
            }
        }
    }

    private fun fetchGroups(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchGroups(token).map { it.mapToUi(groupDomainToUiMapper) }.onEach {
                updateActualUnreadMessagesCounterInGroup(it)
            }
//            val list = interactor.fetchGroups(token).map { it.mapToUi(groupDomainToUiMapper) }
            withContext(Dispatchers.Main) {
//                groupsLiveData.value = list
                groups.value += list
            }
        }
    }

    fun initChatSocket(
        token: String,
        roomsService: RoomsObservingSocketService
    ) {
        fetchRooms(token)
        fetchGroups(token)
        viewModelScope.launch(Dispatchers.IO) {
            when(interactor.initChatSocketSession(token)) {
                is SocketOperationResultListener.Success -> {
                    interactor.observeNewRooms(roomsService).onEach { room ->
                        if (room != null) {
                            rooms.value += room.map(roomDomainToUiMapper)
                            val roomsListFromLocalDb = interactor.fetchRoomsListFromLocalDb()
                                .map { it.map(roomDomainToUiMapper) }
                                .map { it.provideId() }
                            if (!roomsListFromLocalDb.contains(room.provideId())) {
                                interactor.insertRoomIntoLocalDb(room.mapToNewRoomDto(roomDomainToNewRoomDomainMapper))
                            }
                        }
                    }.collect()
                }
                is SocketOperationResultListener.Error -> {
                    Log.d("tag", "initChatSocket: fail")
                }
            }
        }
    }

    fun deleteRoom(userid: String, roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deleteRoom(userid, roomId)
        }
    }

    fun removeDeletedByAnotherUserRoomFromDb(roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deleteRoomFromDb(roomId)
            val deletedRoom = rooms.value.find { it.chatInfo().roomId == roomId }
            if (deletedRoom != null) {
                rooms.value -= deletedRoom
            }
        }
    }

    fun insertGroupMessagesReadCounter(groupId: String, counter: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val readCounter = interactor.fetchGroupMessagesCounter(groupId)
                ?.map(groupMessagesCounterDomainToUiMapper)?.provideCounter()
            if (readCounter != null) {
                val newCounter = readCounter + counter
                interactor.insertNewMessagesCounterInGroup(groupId, newCounter)
            }
        }
    }

    fun increaseMessagesReadCounterInGroup(groupId: String, counter: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val readCounter = interactor.fetchGroupMessagesCounter(groupId)
                ?.map(groupMessagesCounterDomainToUiMapper)?.provideCounter()
            if (readCounter == null) {
                insertGroupMessagesReadCounter(groupId, 0)
            } else {
                val unread = counter + readCounter
                interactor.updateReadMessagesCounterInGroup(groupId, unread)
            }
            val group = groups.value.find { it.sameId(groupId) }
            group?.updateMessagesCounter(0)
            Log.d("tag", "increaseMessagesReadCounterInGroup:RoomvViewModel group -> $group")
//            groupsLiveData.value?.find { it.sameId(groupId) }?.updateMessagesCounter(0)
        }
    }

    fun clearDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.clearDatabase()
        }
    }

    fun joinRoom(token: String, chatInfo: ChatInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            markAuthoredMessageAsRead(token, chatInfo.roomId!!, chatInfo.chatTitle)
            updateLastMsgReadState(token, chatInfo.roomId)
        }
    }

    private fun updateActualUnreadMessagesCounterInGroup(group: GroupUi) {
        viewModelScope.launch(Dispatchers.IO) {
            val readMessagesCounter = interactor.fetchGroupMessagesCounter(group.provideId())
                ?.map(groupMessagesCounterDomainToUiMapper)?.provideCounter()
            if (readMessagesCounter == null) {
                interactor.insertNewMessagesCounterInGroup(group.provideId(), 0)
            } else {
                val allMessagesCount = group.provideGroupMessagesCounter()
                val unread = allMessagesCount - readMessagesCounter
                group.updateMessagesCounter(unread)
                Log.d("tag", "updateActualUnreadMessagesCounterInGroup: before -> $readMessagesCounter")
            }
        }
    }

    fun insertNewGroup(group: GroupUi) {
        viewModelScope.launch(Dispatchers.IO) {
            groups.value += group
        }
    }

    fun removeDeletedGroup(groupUi: GroupUi) {
        viewModelScope.launch(Dispatchers.IO) {
            val groupToDelete = groups.value.find { it.provideId() == groupUi.provideId() }
            if (groupToDelete != null) {
                groups.value -= groupToDelete
            }
        }
    }

    private fun markAuthoredMessageAsRead(token: String, roomId: String, authorName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.markAuthoredMessageAsRead(token, roomId, authorName)
        }
    }

    fun markMessagesAsReadInGroup(token: String, groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.markMessagesAsReadInGroup(token, groupId)
        }
    }

    private fun updateLastMsgReadState(token: String, roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.updateLastMsgReadState(token, roomId)
        }
    }
}