package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.UserDataToDomainMapper
import com.earl.gpns.domain.models.UserDomain
import javax.inject.Inject

class BaseUserDataToDomainMapper @Inject constructor() : UserDataToDomainMapper<UserDomain> {

    override fun map(userId: String, image: String, username: String, online: String) =
        UserDomain.Base(userId, image, username, online)
}