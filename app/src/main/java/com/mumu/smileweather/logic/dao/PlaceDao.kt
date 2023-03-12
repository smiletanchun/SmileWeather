package com.mumu.smileweather.logic.dao

import android.content.Context
import com.google.gson.Gson
import com.mumu.smileweather.WeatherApp
import com.mumu.smileweather.logic.model.Place

object PlaceDao {

    fun savePlace(place: Place) {
        val sharedPreferences = sharedPreferences()
        sharedPreferences.edit().apply {
            putString("place", Gson().toJson(place))
            apply()
        }

    }

    fun getSavePlace(): Place {
        val string = sharedPreferences().getString("place", "")
        return Gson().fromJson<Place>(string, Place::class.java)
    }

    fun isContainPlace(): Boolean {
        return sharedPreferences().contains("place")
    }

    private fun sharedPreferences() =
        WeatherApp.context.getSharedPreferences("weather", Context.MODE_PRIVATE)
}