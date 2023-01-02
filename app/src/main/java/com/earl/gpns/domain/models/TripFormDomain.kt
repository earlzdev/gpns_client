package com.earl.gpns.domain.models

import com.earl.gpns.domain.TripFormDetails
import com.earl.gpns.domain.mappers.TripFormDomainToUiMapper

interface TripFormDomain {

    fun <T> map(mapper: TripFormDomainToUiMapper<T>) : T

    class Base (
        private val username: String,
        private val userImage: String,
        private val companionRole: String,
        private val from: String,
        private val to: String,
        private val schedule: String,
        private val details: TripFormDetails,
        private val active: Int
    ) : TripFormDomain {

        override fun <T> map(mapper: TripFormDomainToUiMapper<T>) : T {
            return if (companionRole == COMPANION_ROLE) {
                mapper.mapCompanionDetails(username, userImage, companionRole, from, to, schedule, details as CompanionFormDetailsDomain, active)
            } else {
                mapper.mapDriversDetails(username, userImage, companionRole, from, to, schedule, details as DriverFormDetailsDomain, active)
            }

        }
        companion object {
            private const val COMPANION_ROLE = "COMPANION_ROLE"
        }
    }
}
