package com.example.weatherapp.domain.weather

import io.reactivex.rxjava3.core.Single

interface WeatherRepository {

    fun getWeather(query: String): Single<WeatherInfo>

    fun getNearestCities(
        latitude: Double?,
        longitude: Double?,
    ): Single<CitiesInfo>
}
