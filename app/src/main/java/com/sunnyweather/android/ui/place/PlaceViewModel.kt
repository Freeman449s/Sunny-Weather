package com.sunnyweather.android.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.PlaceResponse

class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    private var token: String = ""

    val placeList = ArrayList<PlaceResponse.Place>()

    val placeLiveData: LiveData<Result<List<PlaceResponse.Place>>> =
        Transformations.switchMap(searchLiveData) { query: String ->
            Repository.searchPlaces(query, token)
        }

    fun searchPlaces(query: String, token: String) {
        searchLiveData.value = query
        this.token = token // 由UI层确保token有效
    }
}