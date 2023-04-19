package com.example.weatherapp.domain.weather

import io.reactivex.rxjava3.core.Single

class GetCitiesWeatherUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(
       latitude: Double?,
       longitude: Double?,) : Single<CitiesInfo> = weatherRepository.getNearestCities(latitude, longitude)
}
