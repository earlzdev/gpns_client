package com.earl.gpns.data.mappers

interface GroupMessagesCounterDbToDataMapper<T> {

    fun map(
        groupId: String,
        counter: Int
    ) : T
}