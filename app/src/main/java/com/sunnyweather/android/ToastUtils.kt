package com.sunnyweather.android

import android.widget.Toast

class ToastUtils {
    companion object {
        fun makeToast(text: String, duration: Int) {
            Toast.makeText(SunnyWeatherApplication.getContext(), text, duration).show()
        }

        fun makeToast(text: String) {
            Toast.makeText(SunnyWeatherApplication.getContext(), text, Toast.LENGTH_SHORT).show()
        }
    }
}