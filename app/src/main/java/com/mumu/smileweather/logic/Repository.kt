package com.mumu.smileweather.logic

import androidx.lifecycle.liveData
import com.mumu.smileweather.logic.model.Place
import com.mumu.smileweather.logic.model.Weather
import com.mumu.smileweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object Repository {

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val searchPlaces = SunnyWeatherNetwork.searchPlaces(query)
        if (searchPlaces.status == "ok") {
            val places = searchPlaces.places
            Result.success(places)
        } else {
            Result.failure(java.lang.RuntimeException("response status is ${searchPlaces.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val realtimeResponseDeferred = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val dailyResponseDeferred = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = realtimeResponseDeferred.await()
            val dailyResponse = dailyResponseDeferred.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val realTime = realtimeResponse.result.realtime
                val daily = dailyResponse.result.daily
                val weather = Weather(realTime, daily)
                Result.success(weather)
            } else {
                Result.failure<Weather>(
                    java.lang.RuntimeException(
                        "realtimeResponse status is ${realtimeResponse.status}"
                                + "dailyResponseDeferred status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }



    private fun <T> fire(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend () -> Result<T>
    ) = liveData<Result<T>> {
        val result = try {
            block()
        } catch (e: java.lang.Exception) {
            Result.failure<T>(e)
        }
        emit(result)
    }

}