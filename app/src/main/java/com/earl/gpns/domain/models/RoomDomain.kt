package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.RoomDomainToUiMapper
import com.earl.gpns.ui.mappers.RoomDomainToNewRoomDomainMapper

interface RoomDomain {

    fun <T> map(mapper: RoomDomainToUiMapper<T>) : T

    fun <T> mapToNewRoomDto(mapper: RoomDomainToNewRoomDomainMapper<T>) : T

    class Base(
        private val roomId: String,
        private val image: String,
        private val title: String,
        private val lastMessage: String,
        private val lastMessageAuthor: String,
        private val deletable: Boolean,
        private val unreadMsgCounter: Int,
        private val lastMsgRead: Int,
        private val contactIsOnline: Int,
        private val contactLastAuth: String
    ) : RoomDomain {
        override fun <T> map(mapper: RoomDomainToUiMapper<T>) =
            mapper.map(roomId, image, title, lastMessage, lastMessageAuthor, deletable, unreadMsgCounter, lastMsgRead, contactIsOnline, contactLastAuth)

        override fun <T> mapToNewRoomDto(mapper: RoomDomainToNewRoomDomainMapper<T>) =
            mapper.map(roomId, image, title, lastMessage, lastMessageAuthor, deletable, contactIsOnline, contactLastAuth)
    }
}