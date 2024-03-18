package com.sunnyweather.android.logic.network

import com.sunnyweather.android.logic.model.DailyResponse
import com.sunnyweather.android.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("v2.6/{token}/{lng},{lat}/realtime")
    fun getRealtimeWeather(
        @Path("token") token: String,
        @Path("lng") longitude: String,
        @Path("lat") latitude: String
    ): Call<RealtimeResponse>

    @GET("v2.6/{token}/{lng},{lat}/daily?dailysteps=15")
    fun getDailyWeather(
        @Path("token") token: String,
        @Path("lng") longitude: String,
        @Path("lat") latitude: String
    ): Call<DailyResponse>

}