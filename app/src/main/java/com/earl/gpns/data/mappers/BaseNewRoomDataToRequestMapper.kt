package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.remote.requests.NewRoomRequest
import javax.inject.Inject

class BaseNewRoomDataToRequestMapper @Inject constructor(): NewRoomDataToRequestMapper<NewRoomRequest> {

    override fun map(
        roomId: String,
        name: String,
        image: String,
        author: String,
        contact: String,
        lastMessage: String,
        lastMessageAuthor: String,
        contactIsOnline: Int,
        contactLastAuth: String
    ) = NewRoomRequest(roomId, name, image, author, contact, lastMessage, lastMessageAuthor, contactIsOnline, contactLastAuth)
}