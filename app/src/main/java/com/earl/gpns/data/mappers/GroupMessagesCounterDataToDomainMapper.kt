package com.earl.gpns.data.mappers

interface GroupMessagesCounterDataToDomainMapper<T> {

    fun map(
        groupId: String,
        counter: Int
    ) : T
}