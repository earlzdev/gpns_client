package com.earl.gpns.ui.models

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.earl.gpns.core.Same
import com.makeramen.roundedimageview.RoundedImageView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

interface GroupUi : Same<GroupUi> {

    override fun same(value: GroupUi) = this == value

    fun recyclerDetails(
        username: String,
        groupTitle: TextView,
        img: RoundedImageView,
        lastMsg: TextView,
        lastMsgAuthor: TextView,
        lastMsgAuthorImg: RoundedImageView,
        lastMsgTimestamp: TextView,
        unreadMessagesCounter: TextView,
        unreadIndicator: ImageView,
        readIndicator: ImageView
    )

    fun sameId(id: String) : Boolean

    fun provideGroupInfo() : GroupInfo

    fun updateLastMessage(lstMsg: LastMessageForUpdateInGroup)

    fun isAuthoredMessage(authorName: String) : Boolean

    fun updateMessagesCounter(count: Int)

    fun provideId() : String

    fun provideGroupMessagesCounter() : Int

    fun markAuthoredMessagesAsRead()

    class Base(
        private val groupId: String,
        private val title: String,
        private val image: String,
        private var lastMessage: String,
        private var lastMessageAuthor: String,
        private var lastMessageAuthorImage: String,
        private var lastMessageTimestamp: String,
        private val companionGroup: Int,
        private var messagesCount: Int,
        private var lastMsgRead: Int
    ) : GroupUi {

        override fun recyclerDetails(
            username: String,
            groupTitle: TextView,
            img: RoundedImageView,
            lastMsg: TextView,
            lastMsgAuthor: TextView,
            lastMsgAuthorImg: RoundedImageView,
            lastMsgTimestamp: TextView,
            unreadMessagesCounter: TextView,
            unreadIndicator: ImageView,
            readIndicator: ImageView
        ) {
            if (lastMessageTimestamp != "") {
                val currentDate = Date()
                val simpleDateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
                val standardFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                val dateText = simpleDateFormat.format(currentDate)
                val lastAuthDate = LocalDateTime.parse(lastMessageTimestamp, standardFormatter)
                val dayOfYearFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
                val dayOfMonthFormatter = DateTimeFormatter.ofPattern("d MMMM")
                val timeOfDayFormatter = DateTimeFormatter.ofPattern("HH:mm")
                val currentDateText = LocalDateTime.parse(dateText, standardFormatter)
                if (lastAuthDate.format(dayOfMonthFormatter) == currentDateText.format(dayOfMonthFormatter)) {
                    lastMsgTimestamp.text = lastAuthDate.format(timeOfDayFormatter)
                } else if (lastAuthDate.format(dayOfYearFormatter) == currentDateText.format(dayOfYearFormatter)) {
                    lastMsgTimestamp.text = lastAuthDate.format(dayOfMonthFormatter)
                } else {
                    lastMsgTimestamp.text = lastAuthDate.format(dayOfYearFormatter)
                }
            }
            groupTitle.text = title
            lastMsg.text = lastMessage
            lastMsgAuthor.text = "$lastMessageAuthor:"
            if (username == lastMessageAuthor && lastMsgRead == 0) {
                readIndicator.isVisible = false
                unreadMessagesCounter.isVisible = false
                unreadIndicator.isVisible = true
                Log.d("tag", "recyclerDetails: username == lastMessageAuthor && lastMsgRead == 0")
            } else if (username == lastMessageAuthor && lastMsgRead == 1) {
                unreadIndicator.isVisible = false
                unreadMessagesCounter.isVisible = false
                readIndicator.isVisible = true
                Log.d("tag", "recyclerDetails: username == lastMessageAuthor && lastMsgRead == 1")
            } else if (messagesCount != 0){
                unreadMessagesCounter.text = messagesCount.toString()
                Log.d("tag", "messagesCount != 0")
            } else {
                unreadMessagesCounter.isVisible = false
                Log.d("tag", "else")
            }
        }

        override fun provideGroupInfo() = GroupInfo(groupId, title, messagesCount, lastMessageAuthor)

        override fun sameId(id: String) = groupId == id

        override fun updateLastMessage(lstMsg: LastMessageForUpdateInGroup) {
            lastMessage = lstMsg.msgText
            lastMessageAuthor = lstMsg.authorName
            lastMessageTimestamp = lstMsg.timestamp
            lastMsgRead = lstMsg.read
            Log.d("tag", "updateLastMessage: read or not -> ${lstMsg.read}")
            if (lstMsg.read == 1) {
                messagesCount = 0
            } else {
                messagesCount += 1
            }
        }

        override fun isAuthoredMessage(authorName: String) = authorName == lastMessageAuthor

        override fun updateMessagesCounter(count: Int) {
            messagesCount = count
        }

        override fun provideId() = groupId

        override fun provideGroupMessagesCounter() = messagesCount

        override fun markAuthoredMessagesAsRead() {
            lastMsgRead = 1
        }
    }
}