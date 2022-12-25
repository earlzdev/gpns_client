package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.CompanionFormDetailsDomainToUiMapper
import com.earl.gpns.ui.models.CompanionFormDetailsUi
import javax.inject.Inject

class BaseCompanionFormDetailsDomainToUiMapper @Inject constructor(): CompanionFormDetailsDomainToUiMapper<CompanionFormDetailsUi> {

    override fun map(
        from: String,
        to: String,
        schedule: String,
        actualTripTime: String,
        ableToPay: String,
        comment: String
    ) = CompanionFormDetailsUi.Base(from, to, schedule, actualTripTime, ableToPay, comment)
}