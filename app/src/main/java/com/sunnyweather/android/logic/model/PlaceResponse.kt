package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName


data class PlaceResponse(val status: String, val places: List<Place>) {
    data class PlaceCoordinate(
        @SerializedName("lng") val longitude: String,
        @SerializedName("lat") val latitude: String
    )

    /**
     * @property name 地点名称
     * @property location 经纬度
     * @property address 地点的详细地址
     */
    data class Place(
        val name: String,
        val location: PlaceCoordinate,
        @SerializedName("formatted_address") val address: String
    )
}