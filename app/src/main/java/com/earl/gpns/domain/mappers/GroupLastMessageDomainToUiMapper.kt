package com.earl.gpns.domain.mappers

interface GroupLastMessageDomainToUiMapper<T> {

    fun map(
        groupsId: String,
        authorName: String,
        authorImage: String,
        msgText: String,
        timestamp: String,
        read: Int
    ) : T
}