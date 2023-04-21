package com.example.weatherapp.di

import com.example.weatherapp.data.weather.WeatherRepositoryImpl
import com.example.weatherapp.data.weather.datasource.remote.WeatherApi
import com.example.weatherapp.domain.weather.GetCitiesWeatherUseCase
import com.example.weatherapp.domain.weather.GetWeatherUseCase
import com.example.weatherapp.domain.weather.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class WeatherModule {

    @Provides
    fun provideWeatherRepository(
        weatherApi: WeatherApi
    ): WeatherRepository = WeatherRepositoryImpl(weatherApi)

    @Provides
    fun provideWeatherUseCase(
        weatherRepository: WeatherRepository
    ): GetWeatherUseCase = GetWeatherUseCase(weatherRepository)

    @Provides
    fun provideCitiesWeatherUseCase(
        weatherRepository: WeatherRepository
    ): GetCitiesWeatherUseCase = GetCitiesWeatherUseCase(weatherRepository)
}
