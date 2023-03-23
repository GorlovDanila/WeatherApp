package com.example.weatherapp.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.App
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentDetailBinding
import com.example.weatherapp.domain.weather.GetWeatherUseCase
import com.example.weatherapp.domain.weather.WeatherInfo
import com.example.weatherapp.presentation.presenters.DetailsViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DetailsMvvmFragment : Fragment(R.layout.fragment_detail) {

    private var binding: FragmentDetailBinding? = null

    @Inject
    lateinit var useCase: GetWeatherUseCase

    private val viewModel: DetailsViewModel by viewModels {
        DetailsViewModel.provideFactory(useCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cityName = arguments?.getString(ARG_TITLE_VALUE)
        binding = FragmentDetailBinding.bind(view)

        lifecycleScope.launch {
            if (cityName != null) {
                viewModel.loadWeather(cityName)
            }
        }

        viewModel.weatherInfo.observe(this.viewLifecycleOwner) {
            if (it == null) return@observe
            setWeather(binding, it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setWeather(binding: FragmentDetailBinding?, weatherResponse: WeatherInfo) {
        binding?.run {
            tvCityName.text = weatherResponse.cityName?.replace("'", "")
            tvForecast.text = weatherResponse.description
            tvTemp.text = weatherResponse.temp.toString() + "°C"
            tvCity.text = weatherResponse.cityName?.replace("'", "")
            tvCountry.text = weatherResponse.country
            tvHumidity.text = weatherResponse.humidity.toString()
            tvMinTemp.text = weatherResponse.tempMin.toString() + "°C"
            tvMaxTemp.text = weatherResponse.tempMax.toString() + "°C"
            tvWindSp.text = weatherResponse.windSpeed.toString()
            tvSunrises.text = convertLongToTime(weatherResponse.sunrise?.toLong()) + " AM"
            tvSunsets.text = convertLongToTime(weatherResponse.sunset?.toLong()) + " PM"
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertLongToTime(time: Long?): String? {
        if (time != null) {
            val date = Date(time)
            val format = SimpleDateFormat("hh:mm:ss")
            return format.format(date)
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val ARG_TITLE_VALUE = "arg_title_value"

        fun newInstance(title: String) = DetailsMvvmFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE_VALUE, title)
            }
        }
    }
}
