package com.example.weatherapp.data.weather.datasource.remote

import com.example.weatherapp.data.weather.datasource.remote.response.CitiesResponse
import com.example.weatherapp.data.weather.datasource.remote.response.WeatherResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

private const val COUNT_CITIES = 10

interface WeatherApi {

    @GET("weather")
    fun getWeatherByName(
        @Query("q") city: String,
    ): Single<WeatherResponse>

    @GET("find")
    fun getNearestCities(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
        @Query("cnt") count: Int = COUNT_CITIES,
    ): Single<CitiesResponse>
}
