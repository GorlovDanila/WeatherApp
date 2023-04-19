package com.example.weatherapp.domain.location

import com.example.weatherapp.data.location.LocationDataSource
import io.reactivex.rxjava3.core.Flowable

class GetLocationUseCase(
    private val locationDataSource: LocationDataSource
) {
    operator fun invoke(): Flowable<LocationInfo> = locationDataSource.getLocation()
}
