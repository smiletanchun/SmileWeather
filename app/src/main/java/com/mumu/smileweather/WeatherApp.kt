package com.mumu.smileweather

import android.app.Application
import android.content.Context

class WeatherApp : Application() {

    companion object {
       lateinit var context: Context

       const val token:String = "Llo8qRL7FExJIHau"
    }
}