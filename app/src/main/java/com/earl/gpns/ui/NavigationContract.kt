package com.earl.gpns.ui

import com.earl.gpns.ui.models.ChatInfo
import com.earl.gpns.ui.models.GroupInfo
import com.earl.gpns.ui.models.FirstPartOfNewDriverForm

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

    fun startSecondDriverForm(firstPartOfNewDriverForm: FirstPartOfNewDriverForm)

    fun startCompanionForm()

    fun driverFormDetails(details: SearchFormsDetails, viewRegime: String, notificationId: String)

    fun companionFormDetails(details: SearchFormsDetails, viewRegime: String, notificationId: String)

    fun groupMessaging(groupInfo: GroupInfo)

    fun tripNotifications()

    fun popBackStackToFragment(fragment: String)

    fun companionGroupSettingsFragment(groupId: String)

    fun privacyPolicyFragment()

    fun exit()
}