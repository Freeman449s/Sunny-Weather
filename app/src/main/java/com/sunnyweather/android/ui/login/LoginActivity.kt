package com.sunnyweather.android.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.sunnyweather.android.MainActivity
import com.sunnyweather.android.databinding.ActivityLoginBinding

import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherActivity
import com.sunnyweather.android.SunnyWeatherApplication
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class LoginActivity : SunnyWeatherActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tokenEditText = binding.tokenEdit
        val nameEditText = binding.nameEdit
        val loading = binding.loading
        val confirmBtn = binding.confirm

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        if (loginViewModel.isUserInfoInitialized()) { // 因为屏幕旋转等原因重新创建Activity时，填充先前输入的内容
            tokenEditText.setText(loginViewModel.userInfo.token)
            nameEditText.setText(loginViewModel.userInfo.name)
        }

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable confirm button unless token has been input
            confirmBtn.isEnabled = loginState.isDataValid

            if (loginState.tokenError != null) {
                if (loginViewModel.tokenEditHadFocus) // 只有在曾经获取过焦点后才展示错误信息
                    tokenEditText.error = getString(loginState.tokenError)
            }
        })

        tokenEditText.setOnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus) loginViewModel.tokenEditHadFocus = true
        }

        tokenEditText.afterTextChanged {
            loginViewModel.loginDataChanged(
                tokenEditText.text.toString(),
                nameEditText.text.toString()
            )
        }

        nameEditText.afterTextChanged {
            loginViewModel.loginDataChanged(
                tokenEditText.text.toString(),
                nameEditText.text.toString()
            )
        }

        // 只有通过ViewModel的数据合法性校验，才能按下此按钮
        confirmBtn.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.confirmUserInfo()
            thread {
                sleep(2000)
                runOnUiThread {
                    loading.visibility = View.GONE
                    updateUiWithUser(loginViewModel.userInfo)
                    // 如果LoginActivity是唯一运行的Activity，则在完成登录时启动MainActivity
                    if (SunnyWeatherApplication.getNumRunningActivities() == 1) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                }
            }
        }
    }

    private fun updateUiWithUser(userInfo: UserInfo) {
        val welcome = getString(R.string.welcome)
        val displayName = userInfo.name
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher { // 为EditText添加一个TextWatcher来监听输入内容的变化
        override fun afterTextChanged(editable: Editable?) {
            // 此处afterTextChanged为传入EditText.afterTextChanged()的lambda
            // 将editable转为String后，交给lambda处理
            afterTextChanged.invoke(editable.toString()) // editable是SpannableBuilder类型的数据，该类型与EditText.text相同
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}