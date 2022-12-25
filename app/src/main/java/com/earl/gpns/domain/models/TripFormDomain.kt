package com.earl.gpns.domain.models

import com.earl.gpns.domain.TripFormDetails
import com.earl.gpns.domain.mappers.TripFormDomainToUiMapper

interface TripFormDomain {

    fun <T> map(mapper: TripFormDomainToUiMapper<T>) : T

    class Base (
        private val username: String,
        private val userImage: String,
        private val companionRole: String,
        private val details: TripFormDetails
    ) : TripFormDomain {

        override fun <T> map(mapper: TripFormDomainToUiMapper<T>) : T {
            return if (companionRole == COMPANION_ROLE) {
                mapper.mapCompanionDetails(username, userImage, companionRole, details as CompanionFormDetailsDomain)
            } else {
                mapper.mapDriversDetails(username, userImage, companionRole, details as DriverFormDetailsDomain)
            }

        }
        companion object {
            private const val COMPANION_ROLE = "COMPANION_ROLE"
        }
    }
}
