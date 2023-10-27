package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getContext(): Context {
            return context
        }

        fun getToken(): String? {
            val sp = context.getSharedPreferences(
                context.getString(R.string.userInfoSp),
                Context.MODE_PRIVATE
            )
            return sp.getString(context.getString(R.string.spTokenKey), null)
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}