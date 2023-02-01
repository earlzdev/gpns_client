package com.earl.gpns.ui.models

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.earl.gpns.R
import com.earl.gpns.ui.core.CurrentDateAndTimeGiver
import com.earl.gpns.ui.core.Same
import com.makeramen.roundedimageview.RoundedImageView

interface UserUi : Same<UserUi> {

    override fun same(value: UserUi) = this == value

    fun recyclerDetails(
        imageView: RoundedImageView,
        name: TextView,
        lastSeen: TextView,
        onlineIndicator: ImageView
    )

    fun companionGroupRecyclerDetails(
        imageView: RoundedImageView,
        name: TextView,
        role: TextView
    )

    fun chatInfo() : ChatInfo

    fun provideName() : String

    fun provideImage() : String

    fun provideId() : String

    class Base(
        private val userId: String,
        private val image: String,
        private val username: String,
        private val online: Int,
        private val lastAuth: String,
        private val tripRole: String
    ) : UserUi {

        override fun recyclerDetails(
            imageView: RoundedImageView,
            name: TextView,
            lastSeen: TextView,
            onlineIndicator: ImageView
        ) {
            val context = imageView.context
            name.text = username
            if (online == 0) {
                lastSeen.text = context.resources.getString(R.string.was_online_on, CurrentDateAndTimeGiver().initDateTime(lastAuth))
                onlineIndicator.isVisible = false
                lastSeen.setTextColor(context.getColor(R.color.grey_tab_unselected))
            } else {
                onlineIndicator.isVisible = true
                lastSeen.text = context.getString(R.string.online_string)
                lastSeen.setTextColor(context.getColor(R.color.green))
            }
        }

        override fun companionGroupRecyclerDetails(
            imageView: RoundedImageView,
            name: TextView,
            role: TextView
        ) {
            val context = imageView.context
            name.text = username
            role.text = if (tripRole == COMPANION) context.getString(R.string.comp) else context.getString(
                R.string.driver)
        }

        override fun chatInfo() = ChatInfo(null, username, image, online, lastAuth, username)

        override fun provideId() = userId

        override fun provideName() = username

        override fun provideImage() = image

        companion object {
            private const val COMPANION = "Companion"
        }
    }
}