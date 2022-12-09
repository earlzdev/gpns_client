package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.remote.responses.TypingMessageDtoResponse
import javax.inject.Inject

class BaseTypingMessageDataToResponseMapper @Inject constructor(): TypingMessageDataToResponseMapper<TypingMessageDtoResponse> {

    override fun map(roomId: String, username: String, typing: Int) =
        TypingMessageDtoResponse(roomId, username, typing)
}