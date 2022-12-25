package com.earl.gpns.ui.models

import com.earl.gpns.domain.TripFormDetails

interface CompanionFormDetailsUi : TripFormDetails {

    fun provideCompanionFormDetails() : CompanionDetails

    class Base(
        private val actualTripTime: String,
        private val ableToPay: String,
        private val comment: String
    ) : CompanionFormDetailsUi {
        override fun provideCompanionFormDetails() = CompanionDetails(actualTripTime, ableToPay, comment)
    }
}