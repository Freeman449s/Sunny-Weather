package com.sunnyweather.android.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // isAssignableFrom()检查调用Class是否是传入Class，或其超类
        // 或：是否可将传入类的对象赋给调用类的引用
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}