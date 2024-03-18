package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

/**
 * 实时天气状况响应类
 */
data class RealtimeResponse(val status: String, val result: Result) {
    data class UltraViolet(val index: Int, val desc: String) // 紫外线
    data class Comfort(val index: Int, val desc: String) // 舒适度
    data class LifeIndex(val ultraviolet: UltraViolet, val comfort: Comfort)

    data class AirQualityDesc(@SerializedName("chn") val desc: String)
    data class AQI(@SerializedName("chn") val value: Int)
    data class AirQuality(val aqi: AQI, @SerializedName("description") val desc: AirQualityDesc)

    /**
     * 实时天气状况
     *
     * @property temperature 地表2米气温
     * @property apparentTemperature 体感温度
     * @property humidity 湿度
     * @property skycon 天气现象
     * @property visibility 能见度
     * @property airQuality 空气质量
     * @property lifeIndex 生活指数
     */
    data class Realtime(
        val temperature: Float,
        val apparentTemperature: Float,
        val humidity: Float,
        val skycon: String,
        val visibility: Float,
        @SerializedName("air_quality") val airQuality: AirQuality,
        @SerializedName("life_index") val lifeIndex: LifeIndex
    )

    data class Result(val realtime: Realtime)
}