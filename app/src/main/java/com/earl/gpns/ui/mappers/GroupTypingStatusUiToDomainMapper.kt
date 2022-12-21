package com.earl.gpns.ui.mappers

interface GroupTypingStatusUiToDomainMapper <T> {

    fun map(
        groupId: String,
        username: String,
        status: Int
    ) : T
}