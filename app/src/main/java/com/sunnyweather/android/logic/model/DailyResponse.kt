package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DailyResponse(val status: String, val result: Result) {
    data class LifeDesc(val desc: String)

    /**
     * 生活指数
     *
     * @property coldRisk 感冒指数
     * @property carWashing 洗车指数
     * @property ultraviolet 紫外线
     * @property dressing 穿衣指数
     */
    data class LifeIndex(
        val coldRisk: List<LifeDesc>,
        val carWashing: List<LifeDesc>,
        val ultraviolet: List<LifeDesc>,
        val dressing: List<LifeDesc>
    )

    data class Skycon(val value: String, val date: Date)

    data class Temperature(val min: Float, val max: Float)

    /**
     * 未来几天的天气
     */
    data class Daily(
        val temperature: List<Temperature>,
        val skycon: List<Skycon>,
        @SerializedName("life_index") val lifeIndex: LifeIndex
    )

    data class Result(val daily: Daily)
}