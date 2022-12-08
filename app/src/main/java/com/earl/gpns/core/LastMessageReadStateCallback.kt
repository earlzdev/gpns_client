package com.earl.gpns.core

interface LastMessageReadStateCallback {
    fun markAuthoredMessageAsRead(roomId: String)
}