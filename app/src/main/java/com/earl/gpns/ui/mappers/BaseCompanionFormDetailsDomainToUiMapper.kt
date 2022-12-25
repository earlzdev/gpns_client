package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.CompanionFormDetailsDomainToUiMapper
import com.earl.gpns.ui.models.CompanionFormDetailsUi
import javax.inject.Inject

class BaseCompanionFormDetailsDomainToUiMapper @Inject constructor(): CompanionFormDetailsDomainToUiMapper<CompanionFormDetailsUi> {

    override fun map(
        actualTripTime: String,
        ableToPay: String,
        comment: String
    ) = CompanionFormDetailsUi.Base(actualTripTime, ableToPay, comment)
}