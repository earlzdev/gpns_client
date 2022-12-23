package com.earl.gpns.ui

import com.earl.gpns.ui.models.ChatInfo
import com.earl.gpns.ui.models.GroupInfo
import com.earl.gpns.ui.models.NewFirstDriverForm

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

    fun newSearchForm()

    fun startFirstDriverForm()

    fun startSecondDriverForm(newFirstDriverForm: NewFirstDriverForm)

    fun startCompanionForm()

    fun driverFormDetails()

    fun companionFormDetails()

    fun groupMessaging(groupInfo: GroupInfo)

    fun exit()
}