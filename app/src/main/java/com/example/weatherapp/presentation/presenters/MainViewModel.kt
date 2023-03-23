package com.example.weatherapp.presentation.presenters

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weatherapp.R
import com.example.weatherapp.data.weather.datasource.remote.response.WeatherResponse
import com.example.weatherapp.domain.location.GetLocationUseCase
import com.example.weatherapp.domain.location.LocationInfo
import com.example.weatherapp.domain.weather.GetCitiesWeatherUseCase
import com.example.weatherapp.domain.weather.GetWeatherUseCase
import com.example.weatherapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val getCitiesWeatherUseCase: GetCitiesWeatherUseCase
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int>
        get() = _error

    private val _transaction = SingleLiveEvent<String?>()
    val transaction: SingleLiveEvent<String?>
        get() = _transaction

    private val _location = MutableLiveData<LocationInfo?>()
    val location: LiveData<LocationInfo?>
        get() = _location

    private val _citiesList = MutableLiveData<List<WeatherResponse?>?>()
    val citiesList: LiveData<List<WeatherResponse?>?>
        get() = _citiesList

    fun onWeatherClick(weatherResponse: WeatherResponse) {
        val cityName = weatherResponse.name
        if (cityName != null) {
            _transaction.value = cityName
        }
    }

    fun loadWeather(cityName: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                if (!getWeatherUseCase(cityName).cityName.isNullOrEmpty())
                    _transaction.value = getWeatherUseCase(cityName).cityName.toString()
            } catch (e: HttpException) {
                _error.value = R.string.error
            } finally {
                _loading.value = false
            }
        }
    }

    suspend fun locationPerm(res: Boolean) {
        if (res) {
            getLocation()
        } else {
            _error.value = R.string.perm_error
            _location.value = LocationInfo(
                latitude = 54.5299,
                longitude = 52.8039,
            )
        }
    }

    private suspend fun getLocation() {
        _location.value = getLocationUseCase.invoke()
    }

    suspend fun getNearestCities(latitude: Double?, longitude: Double?) {
        _citiesList.value = getCitiesWeatherUseCase.invoke(latitude, longitude).list
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object {
        fun provideFactory(
            getWeatherUseCase: GetWeatherUseCase,
            getCitiesWeatherUseCase: GetCitiesWeatherUseCase,
            getLocationUseCase: GetLocationUseCase,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(
                    getWeatherUseCase,
                    getLocationUseCase,
                    getCitiesWeatherUseCase
                )
            }
        }
    }
}
