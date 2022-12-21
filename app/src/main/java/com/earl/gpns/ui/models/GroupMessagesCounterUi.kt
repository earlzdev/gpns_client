package com.earl.gpns.ui.models

import androidx.lifecycle.MutableLiveData

interface GroupMessagesCounterUi {

    fun setCounterValue(liveData: MutableLiveData<Int>)

    fun provideCounter() : Int

    class Base(
        private val groupId: String,
        private val counter: Int
    ) : GroupMessagesCounterUi {
        override fun setCounterValue(liveData: MutableLiveData<Int>) {
            liveData.value = counter
        }

        override fun provideCounter() = counter
    }
}