package com.earl.gpns.ui.rooms

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.core.SocketOperationResultListener
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.RoomDomainToUiMapper
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.ui.mappers.RoomDomainToNewRoomDomainMapper
import com.earl.gpns.ui.models.RoomUi
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
) : ViewModel() {

    private val rooms: MutableStateFlow<List<RoomUi>> = MutableStateFlow(emptyList())
    val _rooms = rooms.asStateFlow()

    private fun fetchRooms(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchRooms(token).map { it.map(roomDomainToUiMapper) }
            withContext(Dispatchers.Main) {
                rooms.value = list
            }
        }
    }

    fun initChatSocket(token: String) {
        fetchRooms(token)
        viewModelScope.launch(Dispatchers.IO) {
            when(interactor.initChatSocketSession(token)) {
                is SocketOperationResultListener.Success -> {
                    interactor.observeNewRooms().onEach { room ->
                        Log.d("tag", "initChatSocket: new room on rooms $room")
                            rooms.value += room.map(roomDomainToUiMapper)
                            interactor.insertRoomIntoLocalDb(room.mapToNewRoomDto(roomDomainToNewRoomDomainMapper))
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
            val deletedRoom = rooms.value.find { it.chatInfo().roomId == roomId }
            rooms.value -= deletedRoom!!
        }
    }

    fun clearDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.clearDatabase()
        }
    }
}