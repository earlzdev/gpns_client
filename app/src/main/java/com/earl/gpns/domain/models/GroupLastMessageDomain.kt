package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.GroupLastMessageDomainToUiMapper

interface GroupLastMessageDomain {

    fun <T> map(mapper: GroupLastMessageDomainToUiMapper<T>) : T

    class Base(
        private val groupsId: String,
        private val authorName: String,
        private val authorImage: String,
        private val msgText: String,
        private val timestamp: String,
        private val read: Int
    ) : GroupLastMessageDomain {
        override fun <T> map(mapper: GroupLastMessageDomainToUiMapper<T>) =
            mapper.map(groupsId, authorName, authorImage, msgText, timestamp, read)
    }
}