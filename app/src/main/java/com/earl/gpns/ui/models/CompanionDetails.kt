package com.earl.gpns.ui.models

import com.earl.gpns.ui.SearchFormsDetails

data class CompanionDetails(
    val username: String,
    val userImage: String,
    val from: String,
    val to: String,
    val schedule: String,
    val actualTripTime: String,
    val ableToPay: String,
    val comment: String
) : SearchFormsDetails
