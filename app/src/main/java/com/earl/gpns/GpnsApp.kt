package com.earl.gpns

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GpnsApp : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}