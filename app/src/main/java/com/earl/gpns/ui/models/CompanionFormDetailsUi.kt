package com.earl.gpns.ui.models

import com.earl.gpns.domain.TripFormDetails

interface CompanionFormDetailsUi : TripFormDetails {

    fun provideCompanionFormDetails(
        username: String,
        userImage: String,
        from: String,
        to: String,
        schedule: String
    ) : CompanionDetailsUi

    class Base(
        private val actualTripTime: String,
        private val ableToPay: String,
        private val comment: String
    ) : CompanionFormDetailsUi {

        override fun provideCompanionFormDetails(
            username: String,
            userImage: String,
            from: String,
            to: String,
            schedule: String
        ) = CompanionDetailsUi(
            username,
            userImage,
            from,
            to,
            schedule,
            actualTripTime,
            ableToPay,
            comment
        )
    }
}