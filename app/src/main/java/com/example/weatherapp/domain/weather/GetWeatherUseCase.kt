package com.example.weatherapp.domain.weather

class GetWeatherUseCase(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(query: String): WeatherInfo = weatherRepository.getWeather(query)
}
