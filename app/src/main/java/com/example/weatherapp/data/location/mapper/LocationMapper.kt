package com.example.weatherapp.data.location.mapper

import android.location.Location
import com.example.weatherapp.domain.location.LocationInfo

fun Location.toLocationInfo(): LocationInfo = LocationInfo(
    latitude = latitude,
    longitude = longitude,
)
