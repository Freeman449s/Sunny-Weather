package com.sunnyweather.android.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.sunnyweather.android.R

class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    var tokenEditHadFocus = false // EditText是否曾取得过焦点
    var nameEditHadFocus = false

    fun loginDataChanged(token: String) {
        if (!isTokenValid(token)) {
            _loginForm.value = LoginFormState(tokenError = R.string.invalidToken)
        }
        else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isTokenValid(token: String) = token.isNotEmpty()
}
