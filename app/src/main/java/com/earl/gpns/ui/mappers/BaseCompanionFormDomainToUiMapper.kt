package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.CompanionFormDomainToUiMapper
import com.earl.gpns.ui.models.CompanionFormUi
import javax.inject.Inject

class BaseCompanionFormDomainToUiMapper @Inject constructor() : CompanionFormDomainToUiMapper<CompanionFormUi> {

    override fun map(
        username: String,
        userImage: String,
        from: String,
        to: String,
        schedule: String,
        actualTripTime: String,
        ableToPay: String?,
        comment: String
    ) = CompanionFormUi.Base(
        username, userImage, from, to, schedule, actualTripTime, ableToPay, comment
    )
}