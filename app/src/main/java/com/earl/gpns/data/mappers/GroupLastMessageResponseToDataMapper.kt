package com.earl.gpns.data.mappers

interface GroupLastMessageResponseToDataMapper <T> {

    fun map(
        groupsId: String,
        authorName: String,
        authorImage: String,
        msgText: String,
        timestamp: String,
        read: Int
    ) : T
}