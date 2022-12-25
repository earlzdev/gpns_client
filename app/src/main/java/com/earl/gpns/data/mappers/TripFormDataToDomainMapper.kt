package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.CompanionFormDetailsData
import com.earl.gpns.data.models.DriverFormDetailsData

interface TripFormDataToDomainMapper <T> {

    fun mapDriversDetails(
        username: String,
        userImage: String,
        companionRole: String,
        details: DriverFormDetailsData
    ) : T

    fun mapCompanionDetails(
        username: String,
        userImage: String,
        companionRole: String,
        details: CompanionFormDetailsData
    ) : T
}