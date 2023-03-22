package com.example.weatherapp.domain.location

import com.example.weatherapp.data.location.LocationDataSource

class GetLocationUseCase(
    private val locationDataSource: LocationDataSource
) {
    suspend operator fun invoke(): LocationInfo = locationDataSource.getLocation()
}
