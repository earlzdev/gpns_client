package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.UserData
import javax.inject.Inject

class BaseUserResponseToDataMapper @Inject constructor() : UserResponseToDataMapper<UserData> {

    override fun map(userId: String, image: String, username: String, online: String) =
        UserData.Base(userId, image, username, online)
}