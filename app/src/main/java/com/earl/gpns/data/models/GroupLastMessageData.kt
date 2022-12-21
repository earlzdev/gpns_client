package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.GroupLastMessageDataToDomainMapper

interface GroupLastMessageData {

    fun <T> map(mapper: GroupLastMessageDataToDomainMapper<T>) : T

    class Base(
        private val groupsId: String,
        private val authorName: String,
        private val authorImage: String,
        private val msgText: String,
        private val timestamp: String,
        private val read: Int
    ) : GroupLastMessageData {

        override fun <T> map(mapper: GroupLastMessageDataToDomainMapper<T>) =
            mapper.map(groupsId, authorName, authorImage, msgText, timestamp, read)
    }
}