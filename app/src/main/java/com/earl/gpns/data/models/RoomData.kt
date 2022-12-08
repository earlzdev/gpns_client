package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.RoomDataToDomainMapper

interface RoomData {

    fun <T> map(mapper: RoomDataToDomainMapper<T>) : T

    class Base(
        val roomId: String,
        val image: String,
        val title: String,
        val lastMessage: String,
        val lastMessageAuthor: String,
        val deletable: Boolean,
        val unreadMsgCounter: Int,
        private val lastMsgRead: Int
    ) : RoomData {
        override fun <T> map(mapper: RoomDataToDomainMapper<T>) =
            mapper.map(roomId, image, title, lastMessage, lastMessageAuthor, deletable, unreadMsgCounter, lastMsgRead)
    }
}