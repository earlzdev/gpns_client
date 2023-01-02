package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.CompanionFormDataToDomainMapper
import com.earl.gpns.domain.models.CompanionFormDomain
import javax.inject.Inject

class BaseCompanionFormDataToDomainMapper @Inject constructor() : CompanionFormDataToDomainMapper<CompanionFormDomain> {

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
    ) = CompanionFormDomain.Base(
        username, userImage, from, to, schedule, actualTripTime, ableToPay, comment, active
    )
}