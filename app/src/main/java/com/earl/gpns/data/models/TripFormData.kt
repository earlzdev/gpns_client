package com.earl.gpns.data.models

import com.earl.gpns.data.mappers.TripFormDataToDomainMapper
import com.earl.gpns.domain.TripFormDetails

interface TripFormData {

    fun <T> map(mapper: TripFormDataToDomainMapper<T>) : T

    class Base (
        private val username: String,
        private val userImage: String,
        private val companionRole: String,
        private val from: String,
        private val to: String,
        private val schedule: String,
        private val details: TripFormDetails,
        private val active: Int
    ) : TripFormData {

        override fun <T> map(mapper: TripFormDataToDomainMapper<T>): T {
            return if (companionRole == COMPANION_ROLE) {
                mapper.mapCompanionDetails(username, userImage, companionRole, from, to, schedule, details as CompanionFormDetailsData, active)
            } else {
                mapper.mapDriversDetails(username, userImage, companionRole, from, to, schedule, details as DriverFormDetailsData, active)
            }
        }
    }
    companion object {
        private const val COMPANION_ROLE = "COMPANION_ROLE"
    }
}






