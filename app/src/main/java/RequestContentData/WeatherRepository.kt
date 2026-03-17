package RequestContentData

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class WeatherRepository {

    // 彩云天气API - 获取逐小时天气预报
    // TODO: 上传GitHub前请替换为你的API Token
    suspend fun getWeather(
        token: String = "YOUR_CAIYUN_TOKEN",
        longitude: Double = 101.6656,
        latitude: Double = 39.2072,
        hourlySteps: Int = 48
    ): Result<Weather> = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.caiyunapp.com/v2.6/$token/$longitude,$latitude/hourly?hourlysteps=$hourlySteps"

            val request = okhttp3.Request.Builder()
                .url(url)
                .get()
                .build()

            NetworkClient.client.newCall(request).execute().use { response ->

                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("请求失败: ${response.code}"))
                }

                val responseBody = response.body?.string()
                responseBody?.let { Log.i("request_fail", it) }
                if (responseBody == null) {
                    return@withContext Result.failure(Exception("响应体为空"))
                }

                val json = JSONObject(responseBody)

                // 检查API返回状态
                val status = json.getString("status")
                if (status != "ok") {
                    return@withContext Result.failure(Exception("API返回错误: $status"))
                }

                val result = json.getJSONObject("result")
                val hourly = result.getJSONObject("hourly")

                // 解析温度
                val temperatureArray = hourly.getJSONArray("temperature")
                val temperature = (0 until temperatureArray.length()).map {
                    temperatureArray.getJSONObject(it).getDouble("value")
                }

                // 解析天气现象
                val skyconArray = hourly.getJSONArray("skycon")
                val skycon = (0 until skyconArray.length()).map {
                    skyconArray.getJSONObject(it).getString("value")
                }

                // 解析湿度
                val humidityArray = hourly.getJSONArray("humidity")
                val humidity = (0 until humidityArray.length()).map {
                    try {
                        humidityArray.getJSONObject(it).getDouble("value")
                    } catch (e: Exception) {
                        0.0
                    }
                }

                // 解析风速和风向 - wind是数组格式
                val windArray = hourly.getJSONArray("wind")
                val windSpeed = (0 until windArray.length()).map {
                    try {
                        windArray.getJSONObject(it).getDouble("speed")
                    } catch (e: Exception) {
                        0.0
                    }
                }
                val windDirection = (0 until windArray.length()).map {
                    try {
                        windArray.getJSONObject(it).getDouble("direction").toInt()
                    } catch (e: Exception) {
                        0
                    }
                }

                // 解析降水 - precipitation是数组格式
                val precipitationArray = hourly.getJSONArray("precipitation")
                val precipitation = (0 until precipitationArray.length()).map {
                    try {
                        precipitationArray.getJSONObject(it).getDouble("value")
                    } catch (e: Exception) {
                        0.0
                    }
                }

                // 解析时间 - 从wind数组中获取datetime
                val time = (0 until windArray.length()).map {
                    try {
                        windArray.getJSONObject(it).getString("datetime")
                    } catch (e: Exception) {
                        ""
                    }
                }

                val weather = Weather(
                    status = status,
                    temperature = temperature,
                    skycon = skycon,
                    humidity = humidity,
                    windSpeed = windSpeed,
                    windDirection = windDirection,
                    precipitation = precipitation,
                    time = time,
                    longitude = longitude,
                    latitude = latitude
                )

                Result.success(weather)
            }
        } catch (e: Exception) {
            Log.i("fail_result",e.toString())
            Result.failure(e)
        }
    }
}
