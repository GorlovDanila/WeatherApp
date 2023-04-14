package com.example.weatherapp.presentation.presenters

import android.os.Bundle
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weatherapp.domain.weather.GetWeatherUseCase
import com.example.weatherapp.domain.weather.WeatherInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//@HiltViewModel
class DetailsViewModel @AssistedInject constructor(
    @Assisted private val getWeatherUseCase: GetWeatherUseCase,
    @Assisted private val cityName: String
) : ViewModel() {

    private val _weatherInfo = MutableLiveData<WeatherInfo?>(null)
    val weatherInfo: LiveData<WeatherInfo?>
        get() = _weatherInfo

    suspend fun loadWeather() {
        _weatherInfo.value = getWeatherUseCase(cityName)
    }

    @AssistedFactory
    interface DetailsViewModelFactory {
        fun create(getWeatherUseCase: GetWeatherUseCase, cityName: String?): DetailsViewModel
    }

//    companion object {
//        fun provideFactory(
//            getWeatherUseCase: GetWeatherUseCase,
//            defaultArgs: Bundle? = null,
//            cityName: String,
//        ): AbstractSavedStateViewModelFactory = object : AbstractSavedStateViewModelFactory(getWeatherUseCase, d)
//    }

    companion object {
//        fun provideFactory(
//            getWeatherUseCase: GetWeatherUseCase
//        ): ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                DetailsViewModel(getWeatherUseCase)
//            }
//        }

        fun provideFactory(
            assistedFactory: DetailsViewModelFactory,
            getWeatherUseCase: GetWeatherUseCase,
            cityName: String?
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                assistedFactory.create(getWeatherUseCase, cityName)
            }
        }
    }
}
