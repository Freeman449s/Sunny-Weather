package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place

class PlaceViewModel {
    private val searchLiveData = MutableLiveData<String>()
    private var token: String = ""

    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData) { query: String ->
        Repository.searchPlaces(query, token)
    }

    fun searchPlaces(query: String, token: String) {
        searchLiveData.value = query
        this.token = token // 由UI层确保token有效
    }
}