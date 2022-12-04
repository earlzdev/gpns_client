package com.earl.gpns.ui

import com.earl.gpns.ui.models.ChatInfo
import com.earl.gpns.ui.models.RoomUi

interface NavigationContract {

    fun start()

    fun login()

    fun register()

    fun back()

    fun mainFragment()

    fun chat(chatInfo: ChatInfo)

    fun showProgressBar()

    fun hideProgressBar()

    fun usersFragment()

    fun log(text: String)

    fun exit()
}