package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.CompanionFormDetailsDataToDomainMapper
import com.earl.gpns.domain.models.CompanionFormDetailsDomain
import javax.inject.Inject

class BaseCompanionFormDetailsDataToDomainMapper @Inject constructor() : CompanionFormDetailsDataToDomainMapper<CompanionFormDetailsDomain> {

    override fun map(
        actualTripTime: String,
        ableToPay: String,
        comment: String
    ) = CompanionFormDetailsDomain.Base(
        actualTripTime,
        ableToPay,
        comment
    )
}