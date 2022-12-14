package com.earl.gpns.ui.models

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.earl.gpns.R
import com.earl.gpns.core.Same
import com.makeramen.roundedimageview.RoundedImageView

interface UserUi : Same<UserUi> {

    override fun same(value: UserUi) = this == value

    fun recyclerDetails(
        imageView: RoundedImageView,
        name: TextView,
        lastSeen: TextView,
        onlineIndicator: ImageView
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
                lastSeen.text = lastAuth
                onlineIndicator.isVisible = false
                lastSeen.setTextColor(context.getColor(R.color.grey_tab_unselected))
                Log.d("tag", "recyclerDetails: user is not online")
            } else {
                onlineIndicator.isVisible = true
                lastSeen.text = context.getString(R.string.online_string)
                lastSeen.setTextColor(context.getColor(R.color.green))
                Log.d("tag", "recyclerDetails: user is online")
            }
        }

        override fun chatInfo() = ChatInfo(null, username, image, online, lastAuth)

        override fun provideId() = userId

        override fun provideName() = username

        override fun provideImage() = image
    }
}