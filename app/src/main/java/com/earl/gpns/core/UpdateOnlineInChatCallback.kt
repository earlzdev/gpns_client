package com.earl.gpns.core

interface UpdateOnlineInChatCallback {
    fun updateOnline(online: Int, lastAuth: String)
}