package com.earl.gpns.domain.mappers

interface GroupMessagesCounterDomainToUimapper<T> {

    fun map(
        groupId: String,
        counter: Int
    ) : T
}