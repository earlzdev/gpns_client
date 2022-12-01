package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.RoomDomainToUiMapper
import com.earl.gpns.ui.mappers.RoomDomainToNewRoomDomainMapper

interface RoomDomain {

    fun <T> map(mapper: RoomDomainToUiMapper<T>) : T

    fun <T> mapToNewRoomDto(mapper: RoomDomainToNewRoomDomainMapper<T>) : T

    class Base(
        val roomId: String,
        val image: String,
        val title: String,
        val lastMessage: String,
        val lastMessageAuthor: String,
        val deletable: Boolean
    ) : RoomDomain {
        override fun <T> map(mapper: RoomDomainToUiMapper<T>) =
            mapper.map(roomId, image, title, lastMessage, lastMessageAuthor, deletable)

        override fun <T> mapToNewRoomDto(mapper: RoomDomainToNewRoomDomainMapper<T>) =
            mapper.map(roomId, image, title, lastMessage, lastMessageAuthor, deletable)
    }
}