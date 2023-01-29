package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.RoomDataToDomainMapper

interface RoomData {

    fun <T> map(mapper: RoomDataToDomainMapper<T>) : T

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
        private val contactLastAuth: String,
        private val lastMsgTimestamp: String
    ) : RoomData {
        override fun <T> map(mapper: RoomDataToDomainMapper<T>) =
            mapper.map(roomId, image, title, lastMessage, lastMessageAuthor, deletable, unreadMsgCounter, lastMsgRead, contactIsOnline, contactLastAuth, lastMsgTimestamp)
    }
}