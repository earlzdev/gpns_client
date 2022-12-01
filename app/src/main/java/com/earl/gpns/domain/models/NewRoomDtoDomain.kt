package com.earl.gpns.domain.models

import com.earl.gpns.domain.mappers.NewRoomDomainToDataMapper

interface NewRoomDtoDomain {

    fun <T> map(mapper: NewRoomDomainToDataMapper<T>) :  T

    class Base(
        private val roomId: String,
        private val name: String,
        private val image: String,
        private val author: String,
        private val contact: String,
        private val lastMessage: String,
        private val lastMessageAuthor: String
    ) : NewRoomDtoDomain {
        override fun <T> map(mapper: NewRoomDomainToDataMapper<T>) =
            mapper.map(roomId, name, image, author, contact, lastMessage, lastMessageAuthor)
    }
}