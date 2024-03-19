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

    /**
     * “正则化的”天气状况，只包括表达天气的字符串和日期
     * 与用于展示的天气状况区分
     */
    data class CanonicalSkycon(val value: String, val date: Date)

    data class Temperature(val min: Float, val max: Float)

    /**
     * 未来几天的天气
     */
    data class Daily(
        val temperature: List<Temperature>,
        @SerializedName("skycon") val canonicalSkycon: List<CanonicalSkycon>,
        @SerializedName("life_index") val lifeIndex: LifeIndex
    )

    data class Result(val daily: Daily)
}