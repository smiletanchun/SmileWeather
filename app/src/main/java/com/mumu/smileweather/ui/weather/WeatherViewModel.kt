package com.mumu.smileweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mumu.smileweather.logic.Repository
import com.mumu.smileweather.logic.model.Location

class WeatherViewModel : ViewModel() {

    private var location = MutableLiveData<Location>()

    var lng = ""

    var lat = ""

    var placeName = ""

    var weather = Transformations.switchMap(location) {
        Repository.refreshWeather(it.lng, it.lat)
    }

    fun queryWeather(lng: String, lat: String) {
        location.value = Location(lat, lng)
    }
}