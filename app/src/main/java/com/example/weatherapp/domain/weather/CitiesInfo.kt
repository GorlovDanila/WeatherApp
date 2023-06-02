package com.example.weatherapp.domain.weather

import com.example.weatherapp.data.weather.datasource.remote.response.WeatherResponse

data class CitiesInfo(
    val cod: String?,
    val count: Int?,
    val list: List<WeatherResponse?>?,
    val message: String?
)
