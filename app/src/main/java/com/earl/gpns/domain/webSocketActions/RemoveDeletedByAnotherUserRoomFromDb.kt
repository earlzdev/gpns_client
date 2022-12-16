package com.earl.gpns.domain.webSocketActions

interface RemoveDeletedByAnotherUserRoomFromDb {
    fun removeDeletedByAnotherUserRoomFromDb(roomId: String, contactName: String)
}