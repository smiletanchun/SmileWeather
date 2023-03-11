package com.mumu.smileweather.logic.network

import com.mumu.smileweather.WeatherApp
import com.mumu.smileweather.logic.model.DailyResponse
import com.mumu.smileweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    /**
     *  https://api.caiyunapp.com/v2.5/Llo8qRL7FExJIHau/116.322056,39.89491/realtime.json
     * 获取实时天气
     */
    @GET("/v2.5/${WeatherApp.token}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<RealtimeResponse>

    /**
     * https://api.caiyunapp.com/v2.5/Llo8qRL7FExJIHau/116.322056,39.89491/daily.json
     * 未来几天天气
     */
    @GET("/v2.5/${WeatherApp.token}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>
}
