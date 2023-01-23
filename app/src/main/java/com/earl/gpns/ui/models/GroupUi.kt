package com.earl.gpns.ui.models

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.earl.gpns.core.Same
import com.earl.gpns.ui.CurrentDateAndTimeGiver
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
                val dateGiver = CurrentDateAndTimeGiver()
                val currentDateText = dateGiver.fetchCurrentDateAndTime()
                val lastAuthDate = LocalDateTime.parse(lastMessageTimestamp, dateGiver.standardFormatter)
                val dayOfMonthFormatter = dateGiver.fetchDayOfMonthFormat()
                val timeOfDayFormatter = dateGiver.fetchTimeOfDayFormat()
                val dayOfYearFormatter = dateGiver.fetchDayOfYearFormat()
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
            lastMsgAuthor.text = lastMessageAuthor
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
            } else if (username != lastMessageAuthor && messagesCount != 0 && lastMsgRead == 0){
                unreadMessagesCounter.text = messagesCount.toString()
                unreadIndicator.isVisible = false
                unreadMessagesCounter.isVisible = true
                readIndicator.isVisible = false
                lastMsgAuthorImg.isVisible = true
                lastMsgAuthor.isVisible = true
                Log.d("tag", "messagesCount != 0")
            } else if (username != lastMessageAuthor && lastMsgRead == 1) {
                lastMsgAuthor.isVisible = true
                lastMsgAuthorImg.isVisible = true
                readIndicator.isVisible = false
                unreadIndicator.isVisible = false
                unreadMessagesCounter.isVisible = false
                Log.d("tag", "recyclerDetails: username != lastMessageAuthor && lastMsgRead == 1")
            } else {
                unreadMessagesCounter.isVisible = false
                Log.d("tag", "else")
            }
        }

        override fun provideGroupInfo() = GroupInfo(groupId, title, messagesCount, lastMessageAuthor, companionGroup == 1)

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
            messagesCount = 0
            lastMsgRead = 1
        }
    }
}