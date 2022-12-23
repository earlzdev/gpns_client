package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.remote.CompanionFormRemote
import javax.inject.Inject

class BaseCompanionFormDataToRemoteMapper @Inject constructor() : CompanionFormDataToRemoteMapper<CompanionFormRemote> {
    override fun map(
        username: String,
        userImage: String,
        from: String,
        to: String,
        schedule: String,
        actualTripTime: String,
        ableToPay: String?,
        comment: String
    ) = CompanionFormRemote(username, userImage, from, to, schedule, actualTripTime, ableToPay, comment)
}