package com.earl.gpns.ui.models

data class GroupInfo(
    val groupId: String,
    val title: String,
    val counter: Int,
    val lastMessageAuthor: String,
    val isCompanionGroup: Boolean
)
