package com.example.weatherapp.domain.weather

class GetCitiesWeatherUseCase(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(
       latitude: Double?,
       longitude: Double?,) : CitiesInfo = weatherRepository.getNearestCities(latitude, longitude)
}
