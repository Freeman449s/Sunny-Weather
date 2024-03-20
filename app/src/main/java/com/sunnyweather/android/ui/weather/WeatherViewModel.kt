package com.sunnyweather.android.ui.weather

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.ToastUtils
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.PlaceResponse

class WeatherViewModel : ViewModel() {
    private val coordinateLiveData = MutableLiveData<PlaceResponse.PlaceCoordinate>()
    private var token = ""

    var longitude = ""
    var latitude = ""
    var placeName = ""

    private val debugTag = javaClass.canonicalName

    val weatherLiveData = Transformations.switchMap(coordinateLiveData) {
        Repository.refreshWeather(it, token)
    }

    private fun refreshWeather(longitude: String, latitude: String, token: String) {
        coordinateLiveData.value = PlaceResponse.PlaceCoordinate(longitude, latitude)
        this.token = token
    }

    fun refreshWeather(token: String) {
        refreshFire {
            refreshWeather(
                longitude,
                latitude,
                token
            )
        }
    }

    fun refreshWeather() {
        refreshFire {
            refreshWeather(
                longitude,
                latitude,
                token
            )
        }
    }

    /**
     * 封装刷新天气时的try-catch过程
     *
     * @param block 执行刷新天气任务的函数
     */
    private fun refreshFire(block: () -> Unit) {
        try {
            block()
        } catch (ex: NullPointerException) {
            ToastUtils.makeToast("刷新天气时发生异常")
            Log.d(debugTag, ex.stackTraceToString())
        }
    }
}