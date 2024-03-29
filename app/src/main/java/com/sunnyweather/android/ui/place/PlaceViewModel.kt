package com.sunnyweather.android.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.dao.PlaceDao
import com.sunnyweather.android.logic.model.Place

class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    private var token: String = ""

    val placeList = ArrayList<Place>()

    val placeLiveData: LiveData<Result<List<Place>>> =
        Transformations.switchMap(searchLiveData) { query: String ->
            Repository.searchPlaces(query, token)
        }

    fun searchPlaces(query: String, token: String) {
        searchLiveData.value = query
        this.token = token // 由UI层确保token有效
    }

    // ==================== 地点存取功能 ====================
    fun savePlace(place: Place) {
        Repository.savePlace(place)
    }

    fun getSavedPlace(): Place {
        return Repository.getSavedPlace()
    }

    fun hasSavedPlace(): Boolean {
        return Repository.hasSavedPlace()
    }
    // ==================== 结束 ====================
}