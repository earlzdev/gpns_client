package com.earl.gpns.domain.mappers

interface GroupMessageDomainToDataMapper <T> {

    fun map(
        groupId: String,
        messageId: String,
        authorName: String,
        authorImage: String,
        timestamp: String,
        messageText: String,
        read: Int
    ) : T
}