package com.earl.gpns.ui

interface NavigationContract {

    fun start()

    fun login()

    fun register()

    fun back()

    fun mainFragment()

    fun showProgressBar()

    fun hideProgressBar()

    fun usersFragment()

    fun log(text: String)
}