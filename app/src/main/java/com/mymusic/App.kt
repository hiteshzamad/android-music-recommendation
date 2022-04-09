package com.mymusic

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContainer.applicationContext = applicationContext
    }
}