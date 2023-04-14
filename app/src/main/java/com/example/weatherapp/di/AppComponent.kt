package com.example.weatherapp.di

import android.app.Application
import android.content.Context
import com.example.weatherapp.presentation.ui.fragments.DetailsMvvmFragment
import com.example.weatherapp.presentation.ui.fragments.MainMvvmFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

//@Component(modules = [NetworkModule::class, WeatherModule::class, LocationModule::class])
//@Singleton
@Module
@InstallIn(SingletonComponent::class)
class AppComponent {

    @Provides
    fun provideContext(app: Application): Context = app.applicationContext
    @Provides
    fun provideMainMvvmFragment(): MainMvvmFragment = MainMvvmFragment()

    @Provides
    fun provideDetailsMvvmFragment(): DetailsMvvmFragment = DetailsMvvmFragment()

//    fun inject(mainMvvmFragment: MainMvvmFragment)
//
//    fun inject(detailsMvvmFragment: DetailsMvvmFragment)

//    @Component.Builder
//    interface Builder {
//
//        @BindsInstance
//        fun context(applicationContext: Context): Builder
//
//        fun build(): AppComponent
//    }
}
