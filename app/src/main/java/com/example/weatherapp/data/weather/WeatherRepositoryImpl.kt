package com.example.weatherapp.data.weather

import com.example.weatherapp.data.weather.datasource.remote.WeatherApi
import com.example.weatherapp.data.weather.mapper.toCitiesInfo
import com.example.weatherapp.data.weather.mapper.toWeatherInfo
import com.example.weatherapp.domain.weather.CitiesInfo
import com.example.weatherapp.domain.weather.WeatherInfo
import com.example.weatherapp.domain.weather.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val api: WeatherApi,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO,
) : WeatherRepository {
    override suspend fun getWeather(
        query: String
    ): WeatherInfo = withContext(dispatcherIO) {
        api.getWeatherByName(query).toWeatherInfo()
    }

    override suspend fun getNearestCities(
        latitude: Double?,
        longitude: Double?,
    ): CitiesInfo = withContext(dispatcherIO) {
        api.getNearestCities(latitude, longitude).toCitiesInfo()
    }
}
