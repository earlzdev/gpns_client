package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.CompanionFormData
import javax.inject.Inject

class BaseCompanionFormRemoteToDataMapper @Inject constructor() : CompanionFormRemoteToDataMapper<CompanionFormData> {

    override fun map(
        username: String,
        userImage: String,
        from: String,
        to: String,
        schedule: String,
        actualTripTime: String,
        ableToPay: String?,
        comment: String
    ) = CompanionFormData.Base(
        username, userImage, from, to, schedule, actualTripTime, ableToPay, comment
    )
}