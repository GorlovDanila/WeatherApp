package com.example.weatherapp.domain.weather

interface WeatherRepository {

    suspend fun getWeather(query: String): WeatherInfo

    suspend fun getNearestCities(
        latitude: Double?,
        longitude: Double?,
    ): CitiesInfo
}
