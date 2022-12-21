package com.earl.gpns.domain.webSocketActions.services

import com.earl.gpns.domain.webSocketActions.MarkMessageAsReadInChat
import com.earl.gpns.domain.webSocketActions.UpdateUserTypingMessageState
import com.earl.gpns.domain.webSocketActions.UpdateUserOnlineStatusInChat

interface RoomsMessagingSocketActionsService :
    UpdateUserTypingMessageState,
    UpdateUserOnlineStatusInChat,
    MarkMessageAsReadInChat