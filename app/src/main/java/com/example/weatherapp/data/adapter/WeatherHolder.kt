package com.example.weatherapp.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemCityBinding
import com.example.weatherapp.data.response.WeatherResponse

class WeatherHolder(
    private val binding: ItemCityBinding,
    private val actionNext: (WeatherResponse) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private var weatherResponse: WeatherResponse? = null

    init {
        itemView.setOnClickListener {
            weatherResponse?.also(actionNext)
        }
    }

    @SuppressLint("SetTextI18n")
    fun onBind(weatherResponse: WeatherResponse) {
        this.weatherResponse = weatherResponse
        with(binding) {
            weatherResponse.weather?.firstOrNull()?.also {
                ivCover.load("https://openweathermap.org/img/w/${it.icon}.png") {
                    crossfade(true)
                }
            }
            tvTitle.text = weatherResponse.name
            val temp = weatherResponse.main?.temp
            val color = setTempColor(temp)
            tvTemp.text = temp.toString() + "°C"
            tvTemp.setTextColor(color)
        }
    }

    private fun setTempColor(temp: Double?): Int {
        var color = 0
        if (temp != null) {
            when (temp) {
                in -100.0..-20.1 -> color = R.color.purple_700
                in -20.0..-0.1 -> color = R.color.teal_200
                0.0 -> color = R.color.green
                in 0.1..20.0 -> color = R.color.orange_light
                in 20.1..100.0 -> color = R.color.red
            }
        }
        return color
    }

    companion object {
        fun create(
            parent: ViewGroup,
            actionNext: (WeatherResponse) -> Unit,
        ): WeatherHolder = WeatherHolder(
            binding = ItemCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            actionNext = actionNext,
        )
    }
}
