package com.example.weatherapp.presentation.presenters

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weatherapp.domain.weather.GetWeatherUseCase
import com.example.weatherapp.domain.weather.WeatherInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy

class DetailsViewModel @AssistedInject constructor(
    @Assisted private val getWeatherUseCase: GetWeatherUseCase,
    @Assisted private val cityName: String
) : ViewModel() {

    private val _weatherInfo = MutableLiveData<WeatherInfo?>(null)
    val weatherInfo: LiveData<WeatherInfo?>
        get() = _weatherInfo

    private var weatherDisposable: Disposable? = null

    fun loadWeather() {
        weatherDisposable = getWeatherUseCase(cityName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = { weatherInfo ->
                _weatherInfo.value = weatherInfo
            })
        }

    @AssistedFactory
    interface DetailsViewModelFactory {
        fun create(getWeatherUseCase: GetWeatherUseCase, cityName: String?): DetailsViewModel
    }

    override fun onCleared() {
        super.onCleared()
        weatherDisposable?.dispose()
    }

    companion object {
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
