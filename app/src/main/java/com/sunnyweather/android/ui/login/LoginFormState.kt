package com.sunnyweather.android.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val tokenError: Int? = null, // 具体错误信息通过resource id来获取
    val isDataValid: Boolean = false
)