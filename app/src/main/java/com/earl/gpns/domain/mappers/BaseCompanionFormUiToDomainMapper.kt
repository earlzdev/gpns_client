package com.earl.gpns.domain.mappers

import com.earl.gpns.domain.models.CompanionFormDomain
import com.earl.gpns.ui.mappers.CompanionFormUiToDomainMapper
import javax.inject.Inject

class BaseCompanionFormUiToDomainMapper @Inject constructor() : CompanionFormUiToDomainMapper<CompanionFormDomain> {

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