package com.earl.gpns.ui.models

data class LastMessageForUpdateInGroup(
    val authorName: String,
    val authorImage: String,
    val msgText: String,
    val timestamp: String,
    val read: Int
)
