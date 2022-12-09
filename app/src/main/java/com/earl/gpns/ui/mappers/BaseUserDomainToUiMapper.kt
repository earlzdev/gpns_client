package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.UserDomainToUiMapper
import com.earl.gpns.ui.models.UserUi
import javax.inject.Inject

class BaseUserDomainToUiMapper @Inject constructor() : UserDomainToUiMapper<UserUi> {

    override fun map(userId: String, image: String, username: String, online: Int, lastAuth: String) =
        UserUi.Base(userId, image, username, online, lastAuth)
}