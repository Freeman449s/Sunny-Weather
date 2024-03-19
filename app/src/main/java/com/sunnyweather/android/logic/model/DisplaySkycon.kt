package com.sunnyweather.android.logic.model


import com.sunnyweather.android.R

/**
 * “天气状况”封装类
 *
 * @property info 文字描述
 * @property icon 图标
 * @property bg 背景图片
 */
class DisplaySkycon(val info: String, val icon: Int, val bg: Int)

private val skyconMap = mapOf(
    "CLEAR_DAY" to DisplaySkycon("晴", R.drawable.ic_clear_day, R.drawable.bg_clear_day),
    "CLEAR_NIGHT" to DisplaySkycon("晴", R.drawable.ic_clear_night, R.drawable.bg_clear_night),
    "PARTLY_CLOUDY_DAY" to DisplaySkycon(
        "多云",
        R.drawable.ic_partly_cloud_day,
        R.drawable.bg_partly_cloudy_day
    ),
    "PARTLY_CLOUDY_NIGHT" to DisplaySkycon(
        "多云",
        R.drawable.ic_partly_cloud_night,
        R.drawable.bg_partly_cloudy_night
    ),
    "CLOUDY" to DisplaySkycon("阴", R.drawable.ic_cloudy, R.drawable.bg_cloudy),
    "WIND" to DisplaySkycon("大风", R.drawable.ic_cloudy, R.drawable.bg_wind),
    "LIGHT_RAIN" to DisplaySkycon("小雨", R.drawable.ic_light_rain, R.drawable.bg_rain),
    "MODERATE_RAIN" to DisplaySkycon("中雨", R.drawable.ic_moderate_rain, R.drawable.bg_rain),
    "HEAVY_RAIN" to DisplaySkycon("大雨", R.drawable.ic_heavy_rain, R.drawable.bg_rain),
    "STORM_RAIN" to DisplaySkycon("暴雨", R.drawable.ic_storm_rain, R.drawable.bg_rain),
    "THUNDER_SHOWER" to DisplaySkycon("雷阵雨", R.drawable.ic_thunder_shower, R.drawable.bg_rain),
    "SLEET" to DisplaySkycon("雨夹雪", R.drawable.ic_sleet, R.drawable.bg_rain),
    "LIGHT_SNOW" to DisplaySkycon("小雪", R.drawable.ic_light_snow, R.drawable.bg_snow),
    "MODERATE_SNOW" to DisplaySkycon("中雪", R.drawable.ic_moderate_snow, R.drawable.bg_snow),
    "HEAVY_SNOW" to DisplaySkycon("大雪", R.drawable.ic_heavy_snow, R.drawable.bg_snow),
    "STORM_SNOW" to DisplaySkycon("暴雪", R.drawable.ic_heavy_snow, R.drawable.bg_snow),
    "HAIL" to DisplaySkycon("冰雹", R.drawable.ic_hail, R.drawable.bg_snow),
    "LIGHT_HAZE" to DisplaySkycon("轻度雾霾", R.drawable.ic_light_haze, R.drawable.bg_fog),
    "MODERATE_HAZE" to DisplaySkycon("中度雾霾", R.drawable.ic_moderate_haze, R.drawable.bg_fog),
    "HEAVY_HAZE" to DisplaySkycon("重度雾霾", R.drawable.ic_heavy_haze, R.drawable.bg_fog),
    "FOG" to DisplaySkycon("雾", R.drawable.ic_fog, R.drawable.bg_fog),
    "DUST" to DisplaySkycon("浮尘", R.drawable.ic_fog, R.drawable.bg_fog)
)

fun convertSkycon(canonical: DailyResponse.CanonicalSkycon): DisplaySkycon {
    return convertSkycon(canonical.value)
}

fun convertSkycon(skyconString: String): DisplaySkycon {
    // !!表示程序员确定对象不为null，用于通过编译（如果为null会抛出异常）
    return skyconMap[skyconString] ?: skyconMap["CLEAR_DAY"]!!
}