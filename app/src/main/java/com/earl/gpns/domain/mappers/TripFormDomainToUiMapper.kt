package com.earl.gpns.domain.mappers

import com.earl.gpns.domain.models.CompanionFormDetailsDomain
import com.earl.gpns.domain.models.DriverFormDetailsDomain

interface TripFormDomainToUiMapper<T> {

    fun mapDriversDetails(
        username: String,
        userImage: String,
        companionRole: String,
        details: DriverFormDetailsDomain
    ) : T

    fun mapCompanionDetails(
        username: String,
        userImage: String,
        companionRole: String,
        details: CompanionFormDetailsDomain
    ) : T
}