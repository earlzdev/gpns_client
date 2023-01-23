package com.earl.gpns.ui.chat

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.data.models.remote.requests.TypingStatusInGroupRequest
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.GroupMessageDomainToUiMapper
import com.earl.gpns.domain.mappers.GroupMessagesCounterDomainToUimapper
import com.earl.gpns.domain.models.GroupMessageDomain
import com.earl.gpns.domain.models.GroupTypingStatusDomain
import com.earl.gpns.domain.webSocketActions.services.GroupMessagingSocketActionsService
import com.earl.gpns.ui.mappers.GroupMessageUiToDomainMapper
import com.earl.gpns.ui.mappers.GroupTypingStatusUiToDomainMapper
import com.earl.gpns.ui.models.GroupMessageUi
import com.earl.gpns.ui.models.GroupMessagesCounterUi
import com.earl.gpns.ui.models.GroupTypingStatusUi
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
class GroupMessagingViewModel @Inject constructor(
    private val interactor: Interactor,
    private val groupMessageDomainToUiMapper: GroupMessageDomainToUiMapper<GroupMessageUi>,
    private val groupMessageUiToDomainMapper: GroupMessageUiToDomainMapper<GroupMessageDomain>,
    private val groupTypingStatusUiToDomainMapper: GroupTypingStatusUiToDomainMapper<GroupTypingStatusDomain>,
    private val groupMessagesCounterDomainToUimapper: GroupMessagesCounterDomainToUimapper<GroupMessagesCounterUi>
): ViewModel() {

    private val messages: MutableStateFlow<List<GroupMessageUi>> = MutableStateFlow(emptyList())
    val _messages = messages.asStateFlow()

    fun initGroupMessagingSocket(token: String, groupId: String, socketActionsService: GroupMessagingSocketActionsService) {
        fetchAllMessagesInGroup(token, groupId)
        viewModelScope.launch(Dispatchers.IO) {
            interactor.initGroupMessagingSocket(token, groupId)
            interactor.observeGroupMessaging(socketActionsService)
                .onEach { message ->
                    if (message != null) {
                        Log.d("tag", "initGroupMessagingSocket: new message")
                        val newMessage = message.mapToUi(groupMessageDomainToUiMapper)
                        socketActionsService.updateLastMessageAuthorImageInGroup()
                        messages.value += newMessage
                    }
                }.collect()
        }
    }

    fun deleteGroupMessagesCounter(groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deleteMessagesCounterInGroup(groupId)
        }
    }

    fun increaseReadMessagesCounterInGroup(groupId: String, counter: Int) {
        viewModelScope.launch(Dispatchers.IO) {
//            interactor.updateReadMessagesCounterInGroup(groupId, counter)
            val readCounter = interactor.fetchGroupMessagesCounter(groupId)
                ?.map(groupMessagesCounterDomainToUimapper)?.provideCounter()
            if (readCounter != null) {
                val newCounter = readCounter + counter
                Log.d("tag", "insertGroupMessagesReadCounter: counter -> $newCounter")
                interactor.updateReadMessagesCounterInGroup(groupId, newCounter)
            }
        }
    }

    private fun fetchAllMessagesInGroup(token: String, groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchAllMessagesInGroup(token, groupId)
            Log.d("tag", "fetchAllMessagesInGroup: list -> $list")
            withContext(Dispatchers.Main) {
                messages.value += list.map { it.mapToUi(groupMessageDomainToUiMapper) }
            }
        }
    }

    fun sendMessageInGroup(token: String, groupMessage: GroupMessageUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.sendMessageInGroup(token, groupMessage.mapToDomain(groupMessageUiToDomainMapper))
        }
    }

    fun sendGroupTypingMessageStatus(token: String, request: GroupTypingStatusUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.sendGroupTypingMessageStatus(token, request.map(groupTypingStatusUiToDomainMapper))
        }
    }

    fun closeGroupMessagingSocketConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.closeGroupMessagingSocket()
        }
    }
}