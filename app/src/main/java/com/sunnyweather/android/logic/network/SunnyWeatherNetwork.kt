package com.sunnyweather.android.logic.network

import com.sunnyweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 封装项目中所有网络请求的单例类
 */
object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create(PlaceService::class.java)
    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    suspend fun searchPlaces(query: String, token: String) =
        placeService.searchPlaces(query, token).await()


    suspend fun queryRealtimeWeather(
        placeCoordinate: PlaceResponse.PlaceCoordinate, token: String
    ) =
        weatherService.getRealtimeWeather(
            token,
            placeCoordinate.longitude,
            placeCoordinate.latitude
        ).await()

    suspend fun queryDailyWeather(
        placeCoordinate: PlaceResponse.PlaceCoordinate, token: String
    ) =
        weatherService.getDailyWeather(token, placeCoordinate.longitude, placeCoordinate.latitude)
            .await()

    /**
     * 简化网络请求回调的扩展函数，封装了通用的请求处理过程
     *
     * @param T 响应体的类型
     * @return 响应体，或抛出异常
     */
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine<T> { continuation: Continuation<T> ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body) // body: Response
                    else continuation.resumeWithException(java.lang.RuntimeException("Response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }
}