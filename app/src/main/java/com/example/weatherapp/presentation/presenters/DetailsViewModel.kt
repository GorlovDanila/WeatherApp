package com.example.weatherapp.presentation.presenters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weatherapp.domain.weather.GetWeatherUseCase
import com.example.weatherapp.domain.weather.WeatherInfo

class DetailsViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
) : ViewModel() {

    private val _weatherInfo = MutableLiveData<WeatherInfo?>(null)
    val weatherInfo: LiveData<WeatherInfo?>
        get() = _weatherInfo

    suspend fun loadWeather(query: String) {
        _weatherInfo.value = getWeatherUseCase(query)
    }

    companion object {
        fun provideFactory(
            getWeatherUseCase: GetWeatherUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DetailsViewModel(getWeatherUseCase)
            }
        }
    }
}
