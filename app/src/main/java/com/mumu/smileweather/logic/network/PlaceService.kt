package com.mumu.smileweather.logic.network

import com.mumu.smileweather.WeatherApp
import com.mumu.smileweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    /**
     * https://api.caiyunapp.com/v2/place?query=%E5%8C%97%E4%BA%AC&token=Llo8qRL7FExJIHau&lang=zh_CN
     * 查询地址api
     * @param query 城市名称
     */
    @GET("v2/place?token=${WeatherApp.token}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>

}