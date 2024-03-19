package com.sunnyweather.android.logic.dao

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place

class PlaceDao {

    private val context = SunnyWeatherApplication.getContext()
    private val spSavedPlaceKey = context.getString(R.string.spSavedPlaceKey)

    fun savePlace(place: Place) {
        getSp().edit().apply {
            putString(spSavedPlaceKey, Gson().toJson(place))
            apply()
        }
    }

    /**
     * 获取存储的地点。调用者需要确保先前存储过地点。如果不曾存储过地点，将抛出NullPointerException。
     */
    fun getSavedPlace(): Place {
        val json = getSp().getString(spSavedPlaceKey, "")
        // Gson从空字符串反序列化对象时将返回null，由于返回值是非空类型Place，将抛出NullPointerException
        return Gson().fromJson(json, Place::class.java)
    }

    fun hasSavedPlace() =
        getSp().contains(spSavedPlaceKey)


    private fun getSp(): SharedPreferences {
        val spName = context.getString(R.string.savedPlaceSp)
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }
}