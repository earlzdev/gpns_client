package com.earl.gpns.core

import com.earl.gpns.domain.models.NewLastMessageInRoomDomain

interface UpdateLastMessageInRoomCallback {
    fun updateLastMessage(newLastMessage: NewLastMessageInRoomDomain)
}