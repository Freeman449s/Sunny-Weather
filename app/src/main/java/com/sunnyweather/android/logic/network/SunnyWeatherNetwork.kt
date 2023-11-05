package com.sunnyweather.android.logic.network

import com.sunnyweather.android.SunnyWeatherApplication
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

    suspend fun searchPlaces(query: String, token: String) =
        placeService.searchPlaces(query, token).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine<T> { continuation: Continuation<T> ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(java.lang.RuntimeException("Response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }
}