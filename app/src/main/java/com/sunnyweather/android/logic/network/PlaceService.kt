package com.sunnyweather.android.logic.network

import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 处理与地点相关的网络请求
 */
interface PlaceService {
    /**
     * 根据输入查询相关的地名
     */
    @GET("v2.6/place?lang=zh_CN")
    fun searchPlaces(
        @Query("query") query: String,
        @Query("token") token: String
    ): Call<PlaceResponse>
}