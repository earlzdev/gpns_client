package com.earl.gpns.core

import com.earl.gpns.domain.models.NewLastMessageInRoomDomain

interface UpdateLastMessageInRoomCallback {
    fun update(newLastMessage: NewLastMessageInRoomDomain)
}