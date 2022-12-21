package com.earl.gpns.ui.models

interface GroupLastMessageUi {

    fun provideId(): String

    fun provideLastMessageForUpdateInGroup() : LastMessageForUpdateInGroup

    fun isLastMessageReadOrNot() : Boolean

    class Base(
        private val groupsId: String,
        private val authorName: String,
        private val authorImage: String,
        private val msgText: String,
        private val timestamp: String,
        private val read: Int
    ) : GroupLastMessageUi {
        override fun provideId() = groupsId

        override fun provideLastMessageForUpdateInGroup() =
            LastMessageForUpdateInGroup(authorName, authorImage, msgText, timestamp, read)

        override fun isLastMessageReadOrNot() = read == 1
    }
}