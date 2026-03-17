package com.example.dunxiaweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

data class WeatherItem(
    val index: Int,
    val time: String,
    val precipitation: Double,
    val temperature: Double,
    val skycon: String,
    val longitude: Double,
    val latitude: Double
)

class WeatherAdapter(
    private val items: List<WeatherItem>,
    private val onItemClick: (WeatherItem) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivWeatherIcon: ImageView = view.findViewById(R.id.iv_weather_icon)
        val tvTime: TextView = view.findViewById(R.id.tv_time)
        val tvPrecipitation: TextView = view.findViewById(R.id.tv_precipitation)
        val tvTemperature: TextView = view.findViewById(R.id.tv_temperature)
        val tvSkycon: TextView = view.findViewById(R.id.tv_skycon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvTime.text = formatTime(item.time)
        holder.tvPrecipitation.text = "降水: ${item.precipitation} mm/h"
        holder.tvTemperature.text = "温度: ${item.temperature}℃"
        holder.tvSkycon.text = "天气: ${getSkyconText(item.skycon)}"

        // 加载天气图标
        val iconUrl = getWeatherIconUrl(item.skycon)
        Glide.with(holder.itemView.context)
            .load(iconUrl)
            .into(holder.ivWeatherIcon)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = items.size

    // 格式化时间
    private fun formatTime(datetime: String): String {
        return try {
            // 格式: 2026-03-16T15:00+08:00 -> 15:00
            if (datetime.contains("T")) {
                val time = datetime.split("T")[1].split("+")[0]
                time
            } else {
                datetime
            }
        } catch (e: Exception) {
            datetime
        }
    }

    // 天气现象代码转中文
    private fun getSkyconText(skycon: String): String {
        return when (skycon) {
            "CLEAR_DAY" -> "晴天"
            "CLEAR_NIGHT" -> "晴夜"
            "PARTLY_CLOUDY_DAY" -> "多云"
            "PARTLY_CLOUDY_NIGHT" -> "多云"
            "CLOUDY" -> "阴"
            "RAIN" -> "雨"
            "SNOW" -> "雪"
            "RAIN_SNOW" -> "雨夹雪"
            "SHOWER" -> "阵雨"
            "THUNDERSTORM" -> "雷暴"
            "FOG" -> "雾"
            else -> skycon
        }
    }

    // 获取天气图标URL - 使用OpenWeatherMap图标
    private fun getWeatherIconUrl(skycon: String): String {
        val iconCode = when (skycon) {
            "CLEAR_DAY" -> "01d"
            "CLEAR_NIGHT" -> "01n"
            "PARTLY_CLOUDY_DAY" -> "02d"
            "PARTLY_CLOUDY_NIGHT" -> "02n"
            "CLOUDY" -> "03d"
            "RAIN" -> "09d"
            "SNOW" -> "13d"
            "RAIN_SNOW" -> "13d"
            "SHOWER" -> "09d"
            "THUNDERSTORM" -> "11d"
            "FOG" -> "50d"
            else -> "01d"
        }
        return "https://ww3.sinaimg.cn/mw690/008twgG8gy1i8zu1ouhi3j30kg10cae1.jpg"//"https://openweathermap.org/img/wn/$iconCode@2x.png"
    }
}
