package com.earl.gpns.ui.models

import com.earl.gpns.ui.mappers.NewRoomUiToDomainMapper

interface NewRoomDtoUi {

    fun <T> map(mapper: NewRoomUiToDomainMapper<T>) : T

    class Base(
        private val roomId: String,
        private val name: String,
        private val image: String,
        private val author: String,
        private val contact: String,
        private val lastMessage: String,
        private val lastMessageAuthor: String
    ) : NewRoomDtoUi {
        override fun <T> map(mapper: NewRoomUiToDomainMapper<T>) =
            mapper.map(roomId, name, image, author, contact, lastMessage, lastMessageAuthor)
    }
}