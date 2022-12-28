package com.earl.gpns.ui.models

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.earl.gpns.R
import com.earl.gpns.core.Same
import com.earl.gpns.domain.TripFormDetails
import com.earl.gpns.ui.SearchFormsDetails
import com.makeramen.roundedimageview.RoundedImageView

interface TripFormUi : Same<TripFormUi> {

    override fun same(value: TripFormUi) = this == value

    fun recyclerDetails(
        name: TextView,
        image: RoundedImageView,
        tripRole: TextView,
        driverRole: ImageView,
        compRole: ImageView,
        fromTv: TextView,
        toTv: TextView,
        scheduleTv: TextView
    )

    fun provideDetails() : SearchFormsDetails

    fun provideTripRole() : String

    fun sameUsername(name: String) : Boolean

    class Base(
        private val username: String,
        private val userImage: String,
        private val companionRole: String,
        private val from: String,
        private val to: String,
        private val schedule: String,
        private val details: TripFormDetails
    ) : TripFormUi {

        override fun recyclerDetails(
            name: TextView,
            image: RoundedImageView,
            tripRole: TextView,
            driverRole: ImageView,
            compRole: ImageView,
            fromTv: TextView,
            toTv: TextView,
            scheduleTv: TextView
        ) {
            val context = name.context

            name.text = username
            if (companionRole == COMPANION_ROLE) {
                tripRole.text = context.resources.getString(R.string.compa)
                compRole.isVisible = true
                driverRole.isVisible = false
            } else {
                tripRole.text = context.resources.getString(R.string.driv)
                compRole.isVisible = false
                driverRole.isVisible = true
            }
            fromTv.text = from
            toTv.text = to
            scheduleTv.text = schedule
        }

        override fun provideDetails() : SearchFormsDetails {
            return if (companionRole == COMPANION_ROLE) {
                details as CompanionFormDetailsUi
                details.provideCompanionFormDetails(username, userImage, from, to, schedule)
            } else {
                details as DriverFormDetailsUi
                details.provideDriverDetails(username, userImage, from, to, schedule)
            }
        }

        override fun provideTripRole() = companionRole

        override fun sameUsername(name: String) = name == username
    }

    companion object {
        private const val COMPANION_ROLE = "COMPANION_ROLE"
    }
}