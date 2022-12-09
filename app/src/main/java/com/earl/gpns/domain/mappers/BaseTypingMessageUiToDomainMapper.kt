package com.earl.gpns.domain.mappers

import com.earl.gpns.domain.models.TypingMessageDtoDomain
import com.earl.gpns.ui.mappers.TypingMessageDtoUiToDomainMapper
import javax.inject.Inject

class BaseTypingMessageUiToDomainMapper @Inject constructor() : TypingMessageDtoUiToDomainMapper<TypingMessageDtoDomain> {

    override fun map(roomId: String, username: String, typing: Int) =
        TypingMessageDtoDomain.Base(roomId, username, typing)
}