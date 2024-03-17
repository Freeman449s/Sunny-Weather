package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        private var token: String? = null

        private val runningActivities = ArrayList<Activity>()

        fun getContext(): Context {
            return context
        }

        fun getToken(): String? {
            if (token != null) return token

            refreshToken()
            return token
        }

        /**
         * 从SharedPreferences更新token
         */
        fun refreshToken() {
            val sp = context.getSharedPreferences(
                context.getString(R.string.userInfoSp),
                Context.MODE_PRIVATE
            )
            token = sp.getString(context.getString(R.string.spTokenKey), null)
        }

        fun addActivity(activity: SunnyWeatherActivity) {
            runningActivities.add(activity)
        }

        fun removeActivity(activity: SunnyWeatherActivity) {
            runningActivities.remove(activity)
        }

        fun getNumRunningActivities(): Int {
            return runningActivities.count()
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}