package com.sunnyweather.android.ui.login

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherApplication

class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    var tokenEditHadFocus = false // EditText是否曾取得过焦点
    lateinit var userInfo: UserInfo

    fun loginDataChanged(token: String, name: String) {
        userInfo = UserInfo(token, name)
        if (!isTokenValid(token)) {
            _loginForm.value = LoginFormState(tokenError = R.string.invalidToken)
        }
        else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    /**
     * 使用SharedPreferences存储令牌和显示名称
     */
    fun confirmUserInfo() {
        val appContext = SunnyWeatherApplication.getContext()
        val saveName = if (userInfo.name.isNotEmpty()) userInfo.name
        else appContext.getString(R.string.defaultUserName)

        val sp =
            appContext.getSharedPreferences(appContext.getString(R.string.userInfoSp), MODE_PRIVATE)
        sp.edit().apply {
            putString(appContext.getString(R.string.spTokenKey), userInfo.token)
            putString(appContext.getString(R.string.spUserNameKey), saveName)
            apply()
        }
        SunnyWeatherApplication.refreshToken()
    }

    fun isUserInfoInitialized(): Boolean {
        return this::userInfo.isInitialized
    }

    private fun isTokenValid(token: String) = token.isNotEmpty()
}
