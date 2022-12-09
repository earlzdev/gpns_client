package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.TypingMessageDtoData
import com.earl.gpns.domain.mappers.TypingMessageDtoDomainToDataMapper
import javax.inject.Inject

class BaseTypingMessageDtoDomainToDataMapper @Inject constructor(): TypingMessageDtoDomainToDataMapper<TypingMessageDtoData> {

    override fun map(roomId: String, username: String, typing: Int) =
        TypingMessageDtoData.Base(roomId, username, typing)
}