package com.earl.gpns.ui.mappers

import com.earl.gpns.domain.mappers.CompanionFormDetailsDomainToUiMapper
import com.earl.gpns.domain.mappers.DriverFormDetailsDomainToUiMapper
import com.earl.gpns.domain.mappers.TripFormDomainToUiMapper
import com.earl.gpns.domain.models.CompanionFormDetailsDomain
import com.earl.gpns.domain.models.DriverFormDetailsDomain
import com.earl.gpns.ui.models.CompanionFormDetailsUi
import com.earl.gpns.ui.models.DriverFormDetailsUi
import com.earl.gpns.ui.models.TripFormUi
import javax.inject.Inject

class BaseTripFormDomainToUiMapper @Inject constructor(
    private val driverFormDetailsDomainToUiMapper: DriverFormDetailsDomainToUiMapper<DriverFormDetailsUi>,
    private val companionFormDetailsDomainToUiMapper: CompanionFormDetailsDomainToUiMapper<CompanionFormDetailsUi>
) : TripFormDomainToUiMapper<TripFormUi> {

    override fun mapDriversDetails(
        username: String,
        userImage: String,
        companionRole: String,
        details: DriverFormDetailsDomain
    ) = TripFormUi.Base(username, userImage, companionRole, details.map(driverFormDetailsDomainToUiMapper))

    override fun mapCompanionDetails(
        username: String,
        userImage: String,
        companionRole: String,
        details: CompanionFormDetailsDomain
    ) = TripFormUi.Base(username, userImage, companionRole, details.map(companionFormDetailsDomainToUiMapper))
}