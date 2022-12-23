package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.CompanionFormData
import com.earl.gpns.domain.mappers.CompanionFormDomainToDataMapper
import javax.inject.Inject

class BaseCompanionFormDomainToDataMapper @Inject constructor(): CompanionFormDomainToDataMapper<CompanionFormData> {

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