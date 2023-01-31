package com.earl.gpns.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.MessageDomainToUiMapper
import com.earl.gpns.domain.models.MessageDomain
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.domain.models.TypingMessageDtoDomain
import com.earl.gpns.domain.webSocketActions.services.RoomsMessagingSocketActionsService
import com.earl.gpns.ui.mappers.MessageUiToDomainMapper
import com.earl.gpns.ui.mappers.NewRoomUiToDomainMapper
import com.earl.gpns.ui.mappers.TypingMessageDtoUiToDomainMapper
import com.earl.gpns.ui.models.MessageUi
import com.earl.gpns.ui.models.NewRoomDtoUi
import com.earl.gpns.ui.models.TypingMessageDtoUi
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
class RoomMessangerViewModel @Inject constructor(
    private val interactor: Interactor,
    private val newRoomUiToDomainMapper: NewRoomUiToDomainMapper<NewRoomDtoDomain>,
    private val messageDomainToUiMapper: MessageDomainToUiMapper<MessageUi>,
    private val messageUiToDomainMapper: MessageUiToDomainMapper<MessageDomain>,
    private val typingMessageUiToDomainMapper: TypingMessageDtoUiToDomainMapper<TypingMessageDtoDomain>
): ViewModel() {

    private val messages: MutableStateFlow<List<MessageUi>> = MutableStateFlow(emptyList())
    val _messages = messages.asStateFlow()

    fun addRoom(token: String, newRoomUi: NewRoomDtoUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.addRoom(token, newRoomUi.map(newRoomUiToDomainMapper))
        }
    }

    fun addNewRoomToLocalDatabase(room: NewRoomDtoUi) {
        viewModelScope.launch {
            interactor.insertRoomIntoLocalDb(room.map(newRoomUiToDomainMapper))
        }
    }

    private fun fetchMessagesForRoom(token: String, roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchMessagesForRoom(token, roomId).map { it.mapToUi(messageDomainToUiMapper) }
            withContext(Dispatchers.Main) {
                messages.value = list
            }
        }
    }

    fun sendMessage(messageUi: MessageUi, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.sendMessage(messageUi.mapToDomain(messageUiToDomainMapper), token)
        }
    }

    fun initMessagingSocket(
        token: String,
        roomId: String,
        service: RoomsMessagingSocketActionsService
    ) {
        fetchMessagesForRoom(token, roomId)
        viewModelScope.launch(Dispatchers.IO) {
            interactor.initMessagingSocket(token, roomId)
            interactor.observeNewMessages(service)
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

    fun sendTypeMessageResponse(token: String, response: TypingMessageDtoUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.sendTypingMessageRequest(
                token,
                response.map(typingMessageUiToDomainMapper)
            )
        }
    }

    fun closeMessagingSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.closeMessagingSocket()
        }
    }
}