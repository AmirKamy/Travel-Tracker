package com.example.traveltracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LinkerApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        // OSMDroid requires a user agent
        org.osmdroid.config.Configuration.getInstance().userAgentValue = packageName
    }
}