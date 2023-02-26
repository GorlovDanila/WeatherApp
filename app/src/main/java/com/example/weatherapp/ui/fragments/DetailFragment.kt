package com.example.weatherapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.data.Container
import com.example.weatherapp.databinding.FragmentDetailBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var binding: FragmentDetailBinding? = null

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cityName = arguments?.getString(ARG_TITLE_VALUE)
        binding = FragmentDetailBinding.bind(view)
        binding?.run {
            if (cityName != null) {
                lifecycleScope.launch {
                    val weatherResponse = Container.weatherApi.getWeatherByName(cityName)
                    tvCityName.text = weatherResponse.name?.replace("'", "").toString()
                    tvForecast.text = weatherResponse.weather?.get(0)?.description
                    tvTemp.text = weatherResponse.main?.temp.toString()+"°C"
                    tvCity.text = weatherResponse.name
                    tvCountry.text = weatherResponse.sys?.country
                    tvHumidity.text = weatherResponse.main?.humidity.toString()
                    tvMinTemp.text = weatherResponse.main?.tempMin.toString()
                    tvMaxTemp.text = weatherResponse.main?.tempMax.toString()
                    tvWindSp.text = weatherResponse.wind?.speed.toString()
                    tvSunrises.text = convertLongToTime(weatherResponse.sys?.sunrise?.toLong()) + " AM"
                    tvSunsets.text = convertLongToTime(weatherResponse.sys?.sunset?.toLong()) + " PM"
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertLongToTime (time: Long?): String? {
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

        fun newInstance(title: String) = DetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE_VALUE, title)
            }
        }
    }
}
