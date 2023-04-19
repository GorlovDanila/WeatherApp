package com.example.weatherapp.domain.weather

import io.reactivex.rxjava3.core.Single

class GetWeatherUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(query: String): Single<WeatherInfo> = weatherRepository.getWeather(query)
}
