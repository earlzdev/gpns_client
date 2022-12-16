package com.earl.gpns.domain.webSocketActions

interface UpdateUserOnlineInRoom {
    fun updateUserOnlineInRoomObserving(roomId: String, online: Int, lastAuthDate: String)
}