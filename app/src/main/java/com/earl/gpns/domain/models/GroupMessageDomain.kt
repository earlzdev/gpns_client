package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.GroupMessageDomainToDataMapper
import com.earl.gpns.domain.mappers.GroupMessageDomainToUiMapper

interface GroupMessageDomain {

    fun <T> mapToUi(mapper: GroupMessageDomainToUiMapper<T>) : T

    fun <T> mapToData(mapper: GroupMessageDomainToDataMapper<T>) : T

    class Base(
        private val groupId: String,
        private val messageId: String,
        private val authorName: String,
        private val authorImage: String,
        private val timestamp: String,
        private val messageText: String,
        private val read: Int
    ) : GroupMessageDomain {
        override fun <T> mapToUi(mapper: GroupMessageDomainToUiMapper<T>) =
            mapper.map(groupId, messageId, authorName, authorImage, timestamp, messageText, read)

        override fun <T> mapToData(mapper: GroupMessageDomainToDataMapper<T>) =
            mapper.map(groupId, messageId, authorName, authorImage, timestamp, messageText, read)
    }
}