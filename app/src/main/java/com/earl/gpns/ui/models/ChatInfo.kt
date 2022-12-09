package com.earl.gpns.ui.models

data class ChatInfo(
    val roomId: String?,
    val chatTitle: String,
    val chatImage: String,
    val userOnline: Int,
    val userLastAuth: String
)
