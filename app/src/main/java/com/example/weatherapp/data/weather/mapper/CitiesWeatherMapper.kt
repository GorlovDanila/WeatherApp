package com.example.weatherapp.data.weather.mapper

import com.example.weatherapp.data.weather.datasource.remote.response.CitiesResponse
import com.example.weatherapp.domain.weather.CitiesInfo

fun CitiesResponse.toCitiesInfo(): CitiesInfo = CitiesInfo(
    cod = cod,
    count = count,
    list = list,
    message = message,
)
