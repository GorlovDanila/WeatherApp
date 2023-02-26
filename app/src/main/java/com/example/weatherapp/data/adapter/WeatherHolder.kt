package com.example.weatherapp.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
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
            tvTemp.text = weatherResponse.main?.temp.toString() + "°C"
        }
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
