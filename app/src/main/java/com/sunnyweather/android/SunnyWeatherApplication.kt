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

        fun hasToken(): Boolean {
            return token != null
        }

        fun getToken(): String {
            if (token != null) return token as String
            else throw NullPointerException("Token is null.")
        }

        /**
         * 更新保存在SharedPreferences中的用户信息
         */
        fun updateNameAndToken(userName: String, token: String) {
            val sp =
                context.getSharedPreferences(context.getString(R.string.userInfoSp), MODE_PRIVATE)
            sp.edit().apply {
                putString(context.getString(R.string.spTokenKey), token)
                putString(context.getString(R.string.spUserNameKey), userName)
                apply()
            }

            refreshToken()
        }

        /**
         * 从SharedPreferences更新token
         */
        private fun refreshToken() {
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
        refreshToken()
    }
}