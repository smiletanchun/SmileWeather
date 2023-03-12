package com.mumu.smileweather

import android.app.Application
import android.content.Context

class WeatherApp : Application() {

    companion object {
       const val token:String = "Llo8qRL7FExJIHau"
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}