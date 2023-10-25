package com.sunnyweather.android.ui.login

import android.app.Activity
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
import com.sunnyweather.android.databinding.ActivityLoginBinding

import com.sunnyweather.android.R

class LoginActivity : AppCompatActivity() {

    // TODO 改造LoginActivity，使其可用于输入token和称呼
    // TODO 如果不输入称呼，则设置一个默认的称呼

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

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable confirm button unless token has been input
            confirmBtn.isEnabled = loginState.isDataValid

            if (loginState.tokenError != null) {
                if (loginViewModel.tokenEditHadFocus) // 只有在曾经获取过焦点后才展示错误信息
                    tokenEditText.error = getString(loginState.tokenError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE // 视图不显示，且不占用屏幕空间
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        Log.d("EditText.text", username.text::class.toString())

        tokenEditText.setOnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus) loginViewModel.tokenEditHadFocus = true
        }

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            // 与输入法有关的监听器，当在输入法上进行各种操作时回调
            setOnEditorActionListener { view: TextView, actionId: Int, event: KeyEvent ->
                // actionId是输入法发出的操作的标识符，可以在layout文件中编辑键盘右下角按钮执行的操作
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> // “完成”操作
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false // 隐藏键盘
            }

            confirmBtn.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
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