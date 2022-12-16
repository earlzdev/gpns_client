package com.earl.gpns.domain.webSocketActions

import com.earl.gpns.domain.models.NewLastMessageInRoomDomain

interface UpdateLastMessageInRoom {
    fun updateLastMessageInRoom(newLastMessage: NewLastMessageInRoomDomain)
}