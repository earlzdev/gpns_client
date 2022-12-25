package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.CompanionFormDetailsData
import javax.inject.Inject

class BaseCompanionFormDetailsRemoteToDataMapper @Inject constructor() : CompanionTripFormDetailsRemoteToDataMapper<CompanionFormDetailsData> {
    override fun map(
        actualTripTime: String,
        ableToPay: String,
        comment: String
    ) = CompanionFormDetailsData.Base(actualTripTime, ableToPay, comment)
}