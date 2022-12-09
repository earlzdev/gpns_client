package com.earl.gpns.ui.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.core.MarkMessageAsReadCallback
import com.earl.gpns.core.UpdateOnlineInChatCallback
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.MessageDomainToUiMapper
import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.ui.mappers.MessageUiToDomainMapper
import com.earl.gpns.ui.mappers.NewRoomUiToDomainMapper
import com.earl.gpns.ui.models.MessageUi
import com.earl.gpns.ui.models.NewRoomDtoUi
import com.earl.gpns.ui.models.RoomUi
import com.earl.gpns.ui.models.UserUi
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
class ChatViewModel @Inject constructor(
    private val interactor: Interactor,
    private val newRoomUiToDomainMapper: NewRoomUiToDomainMapper<NewRoomDtoDomain>,
    private val messageDomainToUiMapper: MessageDomainToUiMapper<MessageUi>,
    private val messageUiToDomainMapper: MessageUiToDomainMapper<MessageDomain>,
): ViewModel() {

    private val messages: MutableStateFlow<List<MessageUi>> = MutableStateFlow(emptyList())
    val _messages = messages.asStateFlow()
    private val contactDetailsLiveData = MutableLiveData<UserUi>()

    fun addRoom(token: String, newRoomUi: NewRoomDtoUi) {
        Log.d("tag", "addRoom remote: viewmodel")
        viewModelScope.launch(Dispatchers.IO) {
            interactor.addRoom(token, newRoomUi.map(newRoomUiToDomainMapper))
        }
    }

    fun addNewRoomToLocalDatabase(room: NewRoomDtoUi) {
        Log.d("tag", "addRoomto db: viewmodel")
        viewModelScope.launch {
            interactor.insertRoomIntoLocalDb(room.map(newRoomUiToDomainMapper))
        }
    }

    private fun fetchMessagesForRoom(token: String, roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchMessagesForRoom(token, roomId).map { it.mapToUi(messageDomainToUiMapper) }
            Log.d("tag", "fetchMessagesForRoom: $list")
            withContext(Dispatchers.Main) {
                messages.value = list
            }
        }
    }

    fun sendMessage(messageUi: MessageUi, token: String) {
        Log.d("tag", "sendMessage: viewmodel")
        viewModelScope.launch(Dispatchers.IO) {
            interactor.sendMessage(messageUi.mapToDomain(messageUiToDomainMapper), token)
        }
    }

    fun initMessagingSocket(
        token: String,
        roomId: String,
        callback: MarkMessageAsReadCallback,
        updateOnlineCallback: UpdateOnlineInChatCallback
    ) {
        fetchMessagesForRoom(token, roomId)
        viewModelScope.launch(Dispatchers.IO) {
            interactor.initMessagingSocket(token, roomId)
            interactor.observeNewMessages(callback, updateOnlineCallback)
                .onEach { message ->
                    if (message != null) {
                        messages.value += message.mapToUi(messageDomainToUiMapper)
                    }
                }.collect()
        }
    }

    fun markMessagesAsRead(token: String, roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.markMessagesAsRead(token, roomId)
        }
    }

    fun closeMessagingSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.closeMessagingSocket()
        }
    }
}