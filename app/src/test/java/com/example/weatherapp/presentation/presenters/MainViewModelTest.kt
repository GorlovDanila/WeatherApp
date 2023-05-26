package com.example.weatherapp.presentation.presenters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.R
import com.example.weatherapp.data.weather.datasource.remote.response.WeatherResponse
import com.example.weatherapp.domain.location.GetLocationUseCase
import com.example.weatherapp.domain.location.LocationInfo
import com.example.weatherapp.domain.weather.CitiesInfo
import com.example.weatherapp.domain.weather.GetCitiesWeatherUseCase
import com.example.weatherapp.domain.weather.GetWeatherUseCase
import com.example.weatherapp.domain.weather.WeatherInfo
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @MockK
    lateinit var getWeatherUseCase: GetWeatherUseCase

    @MockK
    lateinit var getLocationUseCase: GetLocationUseCase

    @MockK
    lateinit var getCitiesWeatherUseCase: GetCitiesWeatherUseCase

    private lateinit var viewModel: MainViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = MainViewModel(
            getWeatherUseCase,
            getLocationUseCase,
            getCitiesWeatherUseCase,
        )
    }

    @Test
    fun whenCallOnWeatherClick() {
        // arrange
        val weatherResponse: WeatherResponse = mockk()
        val cityName = "Test City"
        every { weatherResponse.name } returns cityName

        //act
        viewModel.onWeatherClick(weatherResponse)

        // assert
        assertEquals(cityName, viewModel.transaction.value)
    }

    @Test
    fun whenCallLoadWeather() {
        // arrange
        val cityName = "Test City"
        val loading = false
        val expectedData: WeatherInfo = mockk()
        every { expectedData.cityName } returns cityName
        coEvery { getWeatherUseCase.invoke(cityName) } returns expectedData

        // act
        viewModel.loadWeather(cityName)

        // assert
        assertEquals(cityName, viewModel.transaction.value)
        assertEquals(loading, viewModel.loading.value)
    }

    @Test
    fun whenCallLocationPermissionsWithTrueAnswerFromUser() = runTest {
        // arrange
        val res = true
        val expectedData: LocationInfo = mockk {
            every { latitude } returns 15.0
            every { longitude } returns 25.0
        }
        val error = null
        coEvery { getLocationUseCase.invoke() } returns expectedData
        // act
        viewModel.locationPerm(res)

        // assert
        assertEquals(error, viewModel.error.value)
        assertEquals(expectedData, viewModel.location.value)

    }

    @Test
    fun whenCallLocationPermissionsWithFalseAnswerFromUser() = runTest {
        // arrange
        val res = false
        val expectedData = LocationInfo(
            latitude = 54.5299,
            longitude = 52.8039
        )
        val error = R.string.perm_error

        // act
        viewModel.locationPerm(res)

        // assert
        assertEquals(error, viewModel.error.value)
        assertEquals(expectedData, viewModel.location.value)
    }

    @Test
    fun whenCallGetNearestCities() = runTest {
        // arrange
        val latitude = 10.0
        val longitude = 25.0
        val expectedData = CitiesInfo(
            cod = "200",
            count = 10,
            list = null,
            message = null
        )
        coEvery { getCitiesWeatherUseCase.invoke(latitude, longitude) } returns expectedData
        // act
        viewModel.getNearestCities(latitude, longitude)

        // assert
        assertEquals(null, viewModel.citiesList.value)
    }
}
