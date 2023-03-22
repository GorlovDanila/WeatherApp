package com.example.weatherapp.data.weather.mapper

import com.example.weatherapp.data.weather.datasource.remote.response.WeatherResponse
import com.example.weatherapp.domain.weather.WeatherInfo

fun WeatherResponse.toWeatherInfo(): WeatherInfo = WeatherInfo(
    cityName = name,
    description = weather?.get(0)?.description,
    temp = main?.temp,
    country = sys?.country,
    humidity = main?.humidity,
    tempMin = main?.tempMin,
    tempMax = main?.tempMax,
    windSpeed = wind?.speed,
    sunrise = sys?.sunrise,
    sunset = sys?.sunset,
)
