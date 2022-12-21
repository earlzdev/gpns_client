package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.GroupMessagesCounterDomainToUimapper

interface GroupMessagesCounterDomain {

    fun <T> map(mapper: GroupMessagesCounterDomainToUimapper<T>) : T

    class Base(
        private val groupId: String,
        private val counter: Int
    ) : GroupMessagesCounterDomain {
        override fun <T> map(mapper: GroupMessagesCounterDomainToUimapper<T>) =
            mapper.map(groupId, counter)
    }
}