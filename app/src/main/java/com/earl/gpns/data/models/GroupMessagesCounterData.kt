package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.GroupMessagesCounterDataToDomainMapper

interface GroupMessagesCounterData {

    fun <T> map(mapper: GroupMessagesCounterDataToDomainMapper<T>) : T

    class Base(
        private val groupId: String,
        private val counter: Int
    ) : GroupMessagesCounterData {
        override fun <T> map(mapper: GroupMessagesCounterDataToDomainMapper<T>) =
            mapper.map(groupId, counter)
    }
}