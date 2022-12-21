package com.earl.gpns.domain.webSocketActions.services

import com.earl.gpns.domain.webSocketActions.UpdateLastMessageAuthorImage

interface GroupMessagingSocketActionsService : UpdateLastMessageAuthorImage {
    fun updateTypingMessageStatusInGroup(username: String, typingStatus: Int)
    fun markMessagesAsReadInGroup(groupId: String)
}