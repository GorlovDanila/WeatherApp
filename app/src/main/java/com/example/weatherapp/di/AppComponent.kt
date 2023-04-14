package com.example.weatherapp.di

import android.app.Application
import android.content.Context
import com.example.weatherapp.presentation.ui.fragments.DetailsMvvmFragment
import com.example.weatherapp.presentation.ui.fragments.MainMvvmFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppComponent {

    @Provides
    fun provideContext(app: Application): Context = app.applicationContext
    @Provides
    fun provideMainMvvmFragment(): MainMvvmFragment = MainMvvmFragment()

    @Provides
    fun provideDetailsMvvmFragment(): DetailsMvvmFragment = DetailsMvvmFragment()

}
