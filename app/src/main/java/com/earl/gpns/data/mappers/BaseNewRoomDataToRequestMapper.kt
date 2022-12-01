package com.earl.gpns.data.mappers

import com.earl.gpns.data.retrofit.requests.NewRoomRequest
import javax.inject.Inject

class BaseNewRoomDataToRequestMapper @Inject constructor(): NewRoomDataToRequestMapper<NewRoomRequest> {

    override fun map(
        roomId: String,
        name: String,
        image: String,
        author: String,
        contact: String,
        lastMessage: String,
        lastMessageAuthor: String
    ) = NewRoomRequest(roomId, name, image, author, contact, lastMessage, lastMessageAuthor)
}