package com.example.weatherapp.presentation.presenters

import androidx.lifecycle.*
import com.example.weatherapp.R
import com.example.weatherapp.data.weather.datasource.remote.response.WeatherResponse
import com.example.weatherapp.domain.location.GetLocationUseCase
import com.example.weatherapp.domain.location.LocationInfo
import com.example.weatherapp.domain.weather.GetCitiesWeatherUseCase
import com.example.weatherapp.domain.weather.GetWeatherUseCase
import com.example.weatherapp.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val getCitiesWeatherUseCase: GetCitiesWeatherUseCase
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = SingleLiveEvent<Int>()
    val error: SingleLiveEvent<Int>
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

    private var weatherDisposable: Disposable? = null
    private var citiesDisposable: Disposable? = null
    private var locationDisposable: Disposable? = null

    private var disposable: CompositeDisposable = CompositeDisposable()

    fun onWeatherClick(weatherResponse: WeatherResponse) {
        val cityName = weatherResponse.name
        if (cityName != null) {
            _transaction.value = cityName
        }
    }

    fun loadWeather(cityName: String) {
        weatherDisposable = getWeatherUseCase(cityName)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _loading.value = true }
            .doAfterTerminate { _loading.value = false }
            .subscribeBy(onSuccess = { weatherInfo ->
                if (weatherInfo.cityName.isNullOrEmpty())
                    Timber.e(weatherInfo.cityName.toString())
                _transaction.value = weatherInfo.cityName.toString()
            }, onError = {
                _error.value = R.string.error
            })
    }

    fun locationPerm(res: Boolean) {
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

    private fun getLocation() {
        locationDisposable = getLocationUseCase.invoke()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { locationInfo ->
                Timber.e(locationInfo.toString())
                _location.value = locationInfo
            }
//        _location.value = getLocationUseCase.invoke()
    }

    fun getNearestCities(latitude: Double?, longitude: Double?) {
        citiesDisposable = getCitiesWeatherUseCase.invoke(latitude, longitude)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { citiesInfo ->
                _citiesList.value = citiesInfo.list
            }
    }

    override fun onCleared() {
        super.onCleared()
        weatherDisposable?.dispose()
        citiesDisposable?.dispose()
        locationDisposable?.dispose()
        disposable.clear()
    }
}
