package com.earl.gpns.core

interface DeleteRoomCallback {
    fun removeRoom(roomId: String, contactName: String)
}