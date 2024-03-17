package com.sunnyweather.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 为方便Activity的管理提供的类，用于替代AppCompatActivity
 */
open class SunnyWeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SunnyWeatherApplication.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SunnyWeatherApplication.removeActivity(this)
    }
}