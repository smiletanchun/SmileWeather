package com.mumu.smileweather.ui.weather

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.ViewModelProvider
import com.mumu.smileweather.R
import com.mumu.smileweather.databinding.ActivityWeatherBinding
import com.mumu.smileweather.logic.model.Weather
import com.mumu.smileweather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    lateinit var weatherBinding: ActivityWeatherBinding

    val weatherViewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        weatherBinding = ActivityWeatherBinding.inflate(layoutInflater)
        window.statusBarColor = Color.TRANSPARENT
        setContentView(weatherBinding.root)
        initData()
    }

    companion object {
        const val consLng = "lng"
        const val consLat = "lat"
        const val placeName = "placeName"
        fun startWeatherActivity(context: Context, place: String, lng: String, lat: String) {
            val intent = Intent(context, WeatherActivity::class.java)
            intent.apply {
                putExtra(consLng, lng)
                putExtra(consLat, lat)
                putExtra(placeName, place)
            }
            context.startActivity(intent)
        }
    }

    private fun initData() {
        if (weatherViewModel.lng.isEmpty()) {
            weatherViewModel.lng = intent.getStringExtra(consLng) ?: ""
        }

        if (weatherViewModel.lat.isEmpty()) {
            weatherViewModel.lat = intent.getStringExtra(consLat) ?: ""
        }

        if (weatherViewModel.placeName.isEmpty()) {
            weatherViewModel.placeName = intent.getStringExtra(placeName) ?: ""
        }

        weatherViewModel.weather.observe(this) { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            weatherBinding.swipeRefresh.isRefreshing = false
        }
        queryWeather()
        weatherBinding.swipeRefresh.setColorSchemeColors(getColor(R.color.purple_200))
        weatherBinding.swipeRefresh.setOnRefreshListener {
            queryWeather()
        }

        weatherBinding.llNow.navBtn.setOnClickListener {
            weatherBinding.drawer.openDrawer(GravityCompat.START)
        }

        weatherBinding.drawer.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }

            override fun onDrawerStateChanged(newState: Int) {
            }

        })

    }

    fun queryWeather() {
        weatherViewModel.queryWeather(weatherViewModel.lng, weatherViewModel.lat)
        weatherBinding.swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {
        weatherBinding.llNow.tvPlaceName.text = weatherViewModel.placeName
        val realTime = weather.realTime
        // 展示now.xml布局数据
        val temperatureText = "${realTime.temperature.toInt()} ℃"
        weatherBinding.llNow.tvCurrentTemp.text = temperatureText
        weatherBinding.llNow.tvCurrentSky.text = getSky(realTime.skycon).info

        val currentPM25Text = "空气指数 ${realTime.airQuality.aqi.chn.toInt()}"
        weatherBinding.llNow.tvCurrentAQI.text = currentPM25Text
        weatherBinding.llNow.root.setBackgroundResource(getSky(realTime.skycon).bg)

        // 填充forecast.xml布局
        weatherBinding.flForecast.llForecast.removeAllViews()

        val size = weather.daily.skycon.size
        for (i in 0 until size) {
            val skycon = weather.daily.skycon[i]
            val temperature = weather.daily.temperature[i]
            val view = LayoutInflater.from(this)
                .inflate(R.layout.item_forecast, weatherBinding.flForecast.llForecast, false)
            val tvDateInfo = view.findViewById<TextView>(R.id.tvDateInfo)
            val ivSkyIcon = view.findViewById<ImageView>(R.id.ivSkyIcon)
            val tvSkyInfo = view.findViewById<TextView>(R.id.tvSkyInfo)
            val tvTemperatureInfo = view.findViewById<TextView>(R.id.tvTemperatureInfo)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = simpleDateFormat.format(skycon.date)
            tvDateInfo.text = date
            val sky = getSky(skycon.value)
            ivSkyIcon.setImageResource(sky.icon)
            tvSkyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            tvTemperatureInfo.text = tempText
            weatherBinding.flForecast.llForecast.addView(view)
        }
        // 填充life_index.xml布局中的数据
        val lifeIndex = weather.daily.lifeIndex
        weatherBinding.flLifeIndex.carWashingText.text = lifeIndex.carWashing[0].desc
        weatherBinding.flLifeIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
        weatherBinding.flLifeIndex.dressingText.text = lifeIndex.dressing[0].desc
        weatherBinding.flLifeIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        weatherBinding.flLifeIndex.root.visibility = View.VISIBLE
    }
}