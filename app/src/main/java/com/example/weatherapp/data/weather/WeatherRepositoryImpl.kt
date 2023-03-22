package com.example.weatherapp.data.weather

import com.example.weatherapp.data.weather.datasource.remote.WeatherApi
import com.example.weatherapp.data.weather.mapper.toCitiesInfo
import com.example.weatherapp.data.weather.mapper.toWeatherInfo
import com.example.weatherapp.domain.weather.CitiesInfo
import com.example.weatherapp.domain.weather.WeatherInfo
import com.example.weatherapp.domain.weather.WeatherRepository

class WeatherRepositoryImpl(
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun getWeather(
        query: String
    ): WeatherInfo = api.getWeatherByName(query).toWeatherInfo()

    override suspend fun getNearestCities(
        latitude: Double?,
        longitude: Double?,
    ): CitiesInfo =  api.getNearestCities(latitude, longitude).toCitiesInfo()
}
