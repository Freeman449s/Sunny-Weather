package com.sunnyweather.android.ui.weather

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.ViewModelProvider
import com.sunnyweather.android.*
import com.sunnyweather.android.databinding.ActivityWeatherBinding
import com.sunnyweather.android.databinding.ForecastItemBinding
import com.sunnyweather.android.logic.model.DailyResponse
import com.sunnyweather.android.logic.model.DisplaySkycon
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.convertSkycon
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class WeatherActivity : SunnyWeatherActivity() {
    lateinit var binding: ActivityWeatherBinding
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (viewModel.longitude.isEmpty()) { // 如果viewModel的经度为空，则尝试从intent获取
            viewModel.longitude = intent.getStringExtra(getString(R.string.intentExtraLng)) ?: ""
        }
        if (viewModel.latitude.isEmpty()) {
            viewModel.latitude = intent.getStringExtra(getString(R.string.intentExtraLat)) ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName =
                intent.getStringExtra(getString(R.string.intentExtraPlaceName)) ?: ""
        }

        viewModel.weatherLiveData.observe(this) { result: Result<Weather> ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                ToastUtils.makeToast("未能获取到天气信息")
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.swipeRefresh.isRefreshing = false
        }

        // 下拉刷新相关功能
        binding.swipeRefresh.setOnRefreshListener {
            refresh()
        }
        var color: Int? = null
        color = getColorCompat(R.attr.color_primary)
        binding.swipeRefresh.setColorSchemeColors(color)

        // 抽屉相关功能
        val drawerBtn = findViewById<Button>(R.id.navBtn)
        drawerBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerClosed(drawerView: View) { // 关闭抽屉时，隐藏输入法
                // TODO CHECKPOINT
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        })

        refresh()
    }

    /**
     * 将天气信息填充到前端
     */
    private fun showWeatherInfo(weather: Weather) {
        // 填充实时天气信息
        val placeNameText = findViewById<TextView>(R.id.placeName)
        val currentTemperatureText = findViewById<TextView>(R.id.currentTemperature)
        val currentSkyconText = findViewById<TextView>(R.id.currentSkycon)
        val currentAqiText = findViewById<TextView>(R.id.currentAqi)

        placeNameText.text = viewModel.placeName
        currentTemperatureText.text = "${weather.realtime.temperature.roundToInt()} ℃"
        val realtimeSkyCon: DisplaySkycon = convertSkycon(weather.realtime.skycon)
        currentSkyconText.text = realtimeSkyCon.info
        currentAqiText.text = "AQI ${weather.realtime.airQuality.aqi.value}"

        val realtimeLayout = findViewById<RelativeLayout>(R.id.realtimeLayout)
        realtimeLayout.setBackgroundResource(realtimeSkyCon.bg)

        // 填充未来7天信息
        // TODO 允许用户更改显示天数
        val forecastLayout = findViewById<LinearLayout>(R.id.forecastLayout)
        forecastLayout.removeAllViews()
        val nDays = 7
        for (i in 0 until nDays) {
            val canonicalSkyCon: DailyResponse.CanonicalSkycon = weather.daily.canonicalSkycon[i]
            val temperature: DailyResponse.Temperature = weather.daily.temperature[i]
            val forecastSkycon: DisplaySkycon = convertSkycon(canonicalSkyCon)
            // 不传入parent参数可能导致子项的margin显示异常
            val forecastItemBinding =
                ForecastItemBinding.inflate(layoutInflater, forecastLayout, false)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val convertedDate = simpleDateFormat.format(canonicalSkyCon.date)

            forecastItemBinding.forecastDate.text = convertedDate
            forecastItemBinding.forecastSkycon.text = forecastSkycon.info
            forecastItemBinding.forecastTemperature.text =
                "${temperature.min.roundToInt()}~${temperature.max.roundToInt()} ℃"
            forecastItemBinding.forecastSkyconIcon.setImageResource(forecastSkycon.icon)

            forecastLayout.addView(forecastItemBinding.root)
        }

        // 填充生活指数信息
        val coldRiskText = findViewById<TextView>(R.id.coldRiskText)
        val dressingText = findViewById<TextView>(R.id.dressingText)
        val uvText = findViewById<TextView>(R.id.ultravioletText)
        val carWashingText = findViewById<TextView>(R.id.carWashingText)
        val lifeInfoCollection: DailyResponse.LifeIndex = weather.daily.lifeIndex

        coldRiskText.text = lifeInfoCollection.coldRisk[0].desc
        dressingText.text = lifeInfoCollection.dressing[0].desc
        uvText.text = lifeInfoCollection.ultraviolet[0].desc
        carWashingText.text = lifeInfoCollection.carWashing[0].desc

        binding.weatherLayout.visibility = View.VISIBLE
    }

    fun refresh() {
        viewModel.refreshWeather(SunnyWeatherApplication.getToken())
        binding.swipeRefresh.isRefreshing = true
    }
}