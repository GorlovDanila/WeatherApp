package com.example.weatherapp.domain.weather

data class WeatherInfo(
    val cityName: String?,
    val description: String?,
    val temp: Double?,
    val country: String?,
    val humidity: Int?,
    val tempMin: Double?,
    val tempMax: Double?,
    val windSpeed: Double?,
    val sunrise: Int?,
    val sunset : Int?,
)
