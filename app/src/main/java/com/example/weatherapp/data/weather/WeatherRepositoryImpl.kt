package com.example.weatherapp.data.weather

import com.example.weatherapp.data.weather.datasource.remote.WeatherApi
import com.example.weatherapp.data.weather.mapper.toCitiesInfo
import com.example.weatherapp.data.weather.mapper.toWeatherInfo
import com.example.weatherapp.domain.weather.CitiesInfo
import com.example.weatherapp.domain.weather.WeatherInfo
import com.example.weatherapp.domain.weather.WeatherRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class WeatherRepositoryImpl(
    private val api: WeatherApi
) : WeatherRepository {
    override fun getWeather(
        query: String
    ): Single<WeatherInfo> = api.getWeatherByName(query)
        .map{
            it.toWeatherInfo()
        }
        .subscribeOn(Schedulers.io())

    override fun getNearestCities(
        latitude: Double?,
        longitude: Double?,
    ): Single<CitiesInfo> =  api.getNearestCities(latitude, longitude)
        .map {
           it.toCitiesInfo()
        }
        .subscribeOn(Schedulers.io())
}
