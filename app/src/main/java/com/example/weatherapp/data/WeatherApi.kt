package com.example.weatherapp.data

import com.example.weatherapp.data.response.CitiesResponse
import com.example.weatherapp.data.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val COUNT_CITIES = 10

interface WeatherApi {

    @GET("weather")
    suspend fun getWeatherByName(
        @Query("q") city: String,
    ): WeatherResponse

    @GET("find")
    suspend fun getNearestCities(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
        @Query("cnt") count: Int = COUNT_CITIES,
    ): CitiesResponse

}
