package com.sunnyweather.android.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.PlaceResponse
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * 仓库层根据需要，从本地缓存或网络获取数据
 */
object Repository {
    fun searchPlaces(query: String, token: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query, token)
        if (placeResponse.status ==
            SunnyWeatherApplication.getContext().getString(R.string.responseStatusOK)
        ) {
            // 标准库的Result<T>只有一个value:Any?成员，而不像一些实现中有表示结果的success成员和表示失败的failure成员
            // success()方法会直接将传入的对象作为value，而failure()方法会调用createFailure()将传入的对象转为Failure类型，再作为value
            // T并不一定与value成员的类型相同。例如，T是某个数据类，而Result中存储的是Failure时，Result的getOrNull()方法将返回null
            Result.success(placeResponse.places)
        } else {
            // Result.value是Failure类型，但是T参数仍为List<Place>
            // 显式声明类型参数是不必要的，可以通过上面的success调用来推断
            Result.failure<List<PlaceResponse.Place>>(RuntimeException("response status: ${placeResponse.status}"))
        }
    }

    fun refreshWeather(coordinate: PlaceResponse.PlaceCoordinate, token: String) =
        fire(Dispatchers.IO) {
            // 挂起函数上下文不同于协程上下文，仍然需要coroutineScope()来提供一个协程上下文
            coroutineScope {
                val realtimeDefer = async {
                    SunnyWeatherNetwork.queryRealtimeWeather(coordinate, token)
                }
                val dailyDefer = async {
                    SunnyWeatherNetwork.queryDailyWeather(coordinate, token)
                }
                val realtimeResponse = realtimeDefer.await()
                val dailyResponse = dailyDefer.await()
                val context = SunnyWeatherApplication.getContext()
                if (realtimeResponse.status == context.getString(R.string.responseStatusOK)
                    && dailyResponse.status == context.getString(R.string.responseStatusOK)
                ) {
                    val weather =
                        Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                    Result.success(weather)
                } else {
                    Result.failure(
                        RuntimeException(
                            "realtime response status: ${realtimeResponse.status}, " +
                                    "daily response status: ${dailyResponse.status}"
                        )
                    )
                }
            }
        }

    /**
     * 用于在发起网络请求时屏蔽try-catch过程
     */
    private fun <T> fire(
        context: CoroutineContext,
        block: suspend () -> Result<T>
    ): LiveData<Result<T>> =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (ex: Exception) {
                Result.failure(ex)
            }
            emit(result) // 类似于setValue()；该调用能帮助Kotlin推断LiveData的泛型类型
        }
}