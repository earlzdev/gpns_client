package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.NewRoomDataToDbMapper
import com.earl.gpns.data.mappers.NewRoomDataToRequestMapper

interface NewRoomDtoData {

    fun <T> mapToRequest(mapper: NewRoomDataToRequestMapper<T>) : T

    fun <T> mapToDb(mapper: NewRoomDataToDbMapper<T>) : T

    class Base(
        private val roomId: String,
        private val name: String,
        private val image: String,
        private val author: String,
        private val contact: String,
        private val lastMessage: String,
        private val lastMessageAuthor: String
    ) : NewRoomDtoData {
        override fun <T> mapToRequest(mapper: NewRoomDataToRequestMapper<T>) =
            mapper.map(roomId, name, image, author, contact, lastMessage, lastMessageAuthor)

        override fun <T> mapToDb(mapper: NewRoomDataToDbMapper<T>) =
            mapper.map(roomId, name, image, author, contact, lastMessage, lastMessageAuthor)
    }
}