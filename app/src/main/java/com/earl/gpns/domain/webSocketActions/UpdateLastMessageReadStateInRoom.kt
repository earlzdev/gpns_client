package com.earl.gpns.domain.webSocketActions

interface UpdateLastMessageReadStateInRoom {
    fun updateLastMessageInRoomReadState(roomId: String)
}