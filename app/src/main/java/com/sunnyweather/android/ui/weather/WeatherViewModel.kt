package com.sunnyweather.android.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.PlaceResponse

class WeatherViewModel : ViewModel() {
    private val coordinateLiveData = MutableLiveData<PlaceResponse.PlaceCoordinate>()
    private var token = ""

    var longitude = ""
    var latitude = ""
    var placeName = ""

    val weatherLiveData = Transformations.switchMap(coordinateLiveData) {
        Repository.refreshWeather(it, token)
    }

    fun refreshWeather(longitude: String, latitude: String, token: String) {
        coordinateLiveData.value = PlaceResponse.PlaceCoordinate(longitude, latitude)
        this.token = token
    }
}