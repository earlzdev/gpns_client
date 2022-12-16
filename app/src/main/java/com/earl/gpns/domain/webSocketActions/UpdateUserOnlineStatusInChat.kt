package com.earl.gpns.domain.webSocketActions

interface UpdateUserOnlineStatusInChat {
    fun updateUserOnlineInChat(online: Int, lastAuth: String)
}