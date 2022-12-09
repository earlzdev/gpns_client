package com.earl.gpns.core

interface UpdateOnlineInRoomCallback {
    fun updateOnline(roomId: String, online: Int, lastAuthDate: String)
}