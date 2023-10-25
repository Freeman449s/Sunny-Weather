package com.sunnyweather.android.ui.login

/**
 * User details post authentication that is exposed to the UI
 * 通过鉴权后，在UI上展示的用户信息
 */
data class LoggedInUserView(
    val displayName: String
)