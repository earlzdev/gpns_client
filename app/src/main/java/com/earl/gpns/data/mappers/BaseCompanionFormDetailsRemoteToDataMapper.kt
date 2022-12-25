package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.CompanionFormDetailsData
import javax.inject.Inject

class BaseCompanionFormDetailsRemoteToDataMapper @Inject constructor() : CompanionTripFormDetailsRemoteToDataMapper<CompanionFormDetailsData> {

    override fun map(
        from: String,
        to: String,
        schedule: String,
        actualTripTime: String,
        ableToPay: String,
        comment: String
    ) = CompanionFormDetailsData.Base(from, to, schedule, actualTripTime, ableToPay, comment)
}