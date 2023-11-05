package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

/**
 * 仓库层根据需要，从本地缓存或网络获取数据
 */
object Repository {
    fun searchPlaces(query: String, token: String) = liveData<Result<List<Place>>>(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query, token)
            if (placeResponse.status ==
                SunnyWeatherApplication.getContext().getString(R.string.responseStatusOK)
            ) {
                // 标准库的Result<T>只有一个value:Any?成员，而不像一些实现中有表示结果的success成员和表示失败的failure成员
                // success()方法会直接将传入的对象作为value，而failure()方法会调用createFailure()将传入的对象转为Failure类型，再作为value
                // T并不一定与value成员的类型相同。例如，T是某个数据类，而Result中存储的是Failure时，Result的getOrNull()方法将返回null
                Result.success(placeResponse.places)
            }
            else {
                // Result.value是Failure类型，但是T参数仍为List<Place>
                // 显式声明类型参数是不必要的，可以通过上面的success调用来推断
                Result.failure<List<Place>>(RuntimeException("response status: ${placeResponse.status}"))
            }
        }
        catch (ex: Exception) {
            Result.failure<List<Place>>(ex)
        }
        emit(result) // 类似于setValue()；该调用会使Kotlin将LiveData的类型推断为Result<List<Place>>
    }
}