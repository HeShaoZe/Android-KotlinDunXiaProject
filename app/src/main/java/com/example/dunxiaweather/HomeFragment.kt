package com.example.dunxiaweather

import HomeDetail.HomeContentDetailActivity
import Layout.ContstrantLayoutActivity
import Layout.FrameLayoutActivity
import PlayMedia.PlayMediaActivity
import RequestContentData.WeatherRepository
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.dunxiaweather.RequestContentData.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 初始显示空列表
        adapter = WeatherAdapter(emptyList()) { item ->
            println("selcttheIndex---${item.index}---${item.precipitation}")

            Log.i("select indexx --", "selcttheIndex---${item.index}---${item.precipitation}")
            if (item.index == 0) {
                val jumpHomeDetailIntent = Intent(requireContext(), HomeContentDetailActivity::class.java)
                jumpHomeDetailIntent.putExtra("index", item.index)
                jumpHomeDetailIntent.putExtra("time", item.time)
                jumpHomeDetailIntent.putExtra("precipitation", item.precipitation)
                jumpHomeDetailIntent.putExtra("temperature", item.temperature)
                jumpHomeDetailIntent.putExtra("skycon", item.skycon)
                jumpHomeDetailIntent.putExtra("longitude", item.longitude)
                jumpHomeDetailIntent.putExtra("latitude", item.latitude)
                startActivity(jumpHomeDetailIntent)
            } else if (item.index == 1) {
                val jumpVideoPlayPage = Intent(requireContext(), PlayMediaActivity::class.java)
                startActivity(jumpVideoPlayPage)
            }

        }
        recyclerView.adapter = adapter

        // 开始请求数据
        startRequestPageContentData()
    }

    ///开始请求界面数据
    fun startRequestPageContentData() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                WeatherRepository().getWeather()
            }

            result.onSuccess { weather ->
                val items = weather.time.mapIndexed { index, time ->
                    WeatherItem(
                        index = index,
                        time = time,
                        precipitation = weather.precipitation.getOrElse(index) { 0.0 },
                        temperature = weather.temperature.getOrElse(index) { 0.0 },
                        skycon = weather.skycon.getOrElse(index) { "UNKNOWN" },
                        longitude = weather.longitude,
                        latitude = weather.latitude
                    )
                }

                adapter = WeatherAdapter(items) { item ->
                    if (item.index == 0) {
                        val jumpHomeDetailIntent = Intent(requireContext(), HomeContentDetailActivity::class.java)
                        jumpHomeDetailIntent.putExtra("index", item.index)
                        jumpHomeDetailIntent.putExtra("time", item.time)
                        jumpHomeDetailIntent.putExtra("precipitation", item.precipitation)
                        jumpHomeDetailIntent.putExtra("temperature", item.temperature)
                        jumpHomeDetailIntent.putExtra("skycon", item.skycon)
                        jumpHomeDetailIntent.putExtra("longitude", item.longitude)
                        jumpHomeDetailIntent.putExtra("latitude", item.latitude)
                        startActivity(jumpHomeDetailIntent)
                    } else if (item.index == 1) {
                        val jumpVideoPlayPage = Intent(requireContext(), PlayMediaActivity::class.java)
                        startActivity(jumpVideoPlayPage)
                    } else if (item.index == 2) {
                        val jumpContranLayoutPage = Intent(requireContext(), ContstrantLayoutActivity::class.java)
                        startActivity(jumpContranLayoutPage)
                    } else if (item.index == 3) {
                        val jumpFrameLayout = Intent(requireContext(), FrameLayoutActivity::class.java)
                        startActivity(jumpFrameLayout)
                    }
                }
                recyclerView.adapter = adapter
            }

            result.onFailure { error ->
                Toast.makeText(requireContext(), "获取天气失败: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
