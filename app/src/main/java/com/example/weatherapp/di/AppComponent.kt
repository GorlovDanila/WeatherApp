package com.example.weatherapp.di

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.weatherapp.App
import com.example.weatherapp.presentation.ui.MainActivity
import com.example.weatherapp.presentation.ui.fragments.DetailsMvvmFragment
import com.example.weatherapp.presentation.ui.fragments.MainMvvmFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, WeatherModule::class, LocationModule::class])
@Singleton
interface AppComponent {

    fun inject(mainMvvmFragment: MainMvvmFragment)

    fun inject(detailsMvvmFragment: DetailsMvvmFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(applicationContext: Context): Builder

        fun build(): AppComponent
    }
}