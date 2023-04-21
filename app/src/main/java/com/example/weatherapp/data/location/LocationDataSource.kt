package com.example.weatherapp.data.location

import android.annotation.SuppressLint
import com.example.weatherapp.data.location.mapper.toLocationInfo
import com.example.weatherapp.domain.location.LocationInfo
import com.google.android.gms.location.FusedLocationProviderClient
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class LocationDataSource(
    private val fusedLocationClient: FusedLocationProviderClient
) {
    private val locationSubject = PublishSubject.create<LocationInfo>()

    @SuppressLint("MissingPermission")
    fun getLocation(): Flowable<LocationInfo> = locationSubject.toFlowable(
        BackpressureStrategy.LATEST
    )
        .doOnSubscribe {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                locationSubject.onNext(
                    it.toLocationInfo()
                )
            }.addOnFailureListener {
                LocationInfo(
                    latitude = 54.5299,
                    longitude = 52.8039,
                )
            }
        }
        .subscribeOn(Schedulers.io())
}
