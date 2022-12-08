package com.earl.gpns.core

interface AuthoredMessageReadListener {
    fun markAuthoredMessageAsRead(roomId: String)
}