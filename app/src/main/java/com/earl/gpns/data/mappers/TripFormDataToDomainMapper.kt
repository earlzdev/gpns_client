package com.earl.gpns.data.mappers

import com.earl.gpns.data.models.CompanionFormDetailsData
import com.earl.gpns.data.models.DriverFormDetailsData

interface TripFormDataToDomainMapper <T> {

    fun mapDriversDetails(
        username: String,
        userImage: String,
        companionRole: String,
        from: String,
        to: String,
        schedule: String,
        details: DriverFormDetailsData,
        active: Int
    ) : T

    fun mapCompanionDetails(
        username: String,
        userImage: String,
        companionRole: String,
        from: String,
        to: String,
        schedule: String,
        details: CompanionFormDetailsData,
        active: Int
    ) : T
}