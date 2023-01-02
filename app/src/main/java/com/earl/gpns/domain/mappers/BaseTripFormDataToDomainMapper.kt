package com.earl.gpns.domain.mappers

import com.earl.gpns.data.mappers.CompanionFormDetailsDataToDomainMapper
import com.earl.gpns.data.mappers.DriverFormDetailsDataToDomainMapper
import com.earl.gpns.data.mappers.TripFormDataToDomainMapper
import com.earl.gpns.data.models.CompanionFormDetailsData
import com.earl.gpns.data.models.DriverFormDetailsData
import com.earl.gpns.domain.models.CompanionFormDetailsDomain
import com.earl.gpns.domain.models.DriverFormDetailsDomain
import com.earl.gpns.domain.models.TripFormDomain
import javax.inject.Inject

class BaseTripFormDataToDomainMapper @Inject constructor(
    private val driverFormDetailsDataToDomainMapper: DriverFormDetailsDataToDomainMapper<DriverFormDetailsDomain>,
    private val companionFormDetailsDataToDomainMapper: CompanionFormDetailsDataToDomainMapper<CompanionFormDetailsDomain>
) : TripFormDataToDomainMapper<TripFormDomain> {

    override fun mapDriversDetails(
        username: String,
        userImage: String,
        companionRole: String,
        from: String,
        to: String,
        schedule: String,
        details: DriverFormDetailsData,
        active: Int
    ) = TripFormDomain.Base(username, userImage, companionRole, from, to ,schedule, details.map(driverFormDetailsDataToDomainMapper), active)

    override fun mapCompanionDetails(
        username: String,
        userImage: String,
        companionRole: String,
        from: String,
        to: String,
        schedule: String,
        details: CompanionFormDetailsData,
        active: Int
    ) = TripFormDomain.Base(username, userImage, companionRole, from, to ,schedule, details.mapToDomain(companionFormDetailsDataToDomainMapper), active)
}