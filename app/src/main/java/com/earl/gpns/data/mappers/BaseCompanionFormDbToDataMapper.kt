package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.CompanionFormData
import javax.inject.Inject

class BaseCompanionFormDbToDataMapper @Inject constructor() : CompanionFormDbToDataMapper<CompanionFormData> {

    override fun map(
        username: String,
        userImage: String,
        from: String,
        to: String,
        schedule: String,
        actualTripTime: String,
        ableToPay: String?,
        comment: String,
        active: Int
    ) = CompanionFormData.Base(
        username, userImage, from, to, schedule, actualTripTime, ableToPay, comment, 1
    )
}