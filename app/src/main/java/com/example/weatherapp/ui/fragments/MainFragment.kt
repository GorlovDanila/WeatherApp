package com.example.weatherapp.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.Container
import com.example.weatherapp.data.adapter.WeatherListAdapter
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.data.response.WeatherResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainFragment : Fragment(R.layout.fragment_main) {

    private var binding: FragmentMainBinding? = null
    private var listAdapter: WeatherListAdapter? = null
    private val api = Container.weatherApi
    private var latitude: Double? = null
    private var longitude: Double? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var citiesRepository: List<WeatherResponse?>? = null


    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                getLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                getLocation()
            }
            else -> {
                latitude = 54.5299
                longitude = 52.8039
                lifecycleScope.launch {
                    citiesRepository = api.getNearestCities(latitude, longitude).list
                    setListAdapterConfig()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        listAdapter = WeatherListAdapter(actionNext = ::onWeatherClick)
        binding?.rvCities?.adapter = listAdapter
        binding?.rvCities?.layoutManager = LinearLayoutManager(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        } else {
            getLocation()
        }

        binding?.run {
            swCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isNotEmpty()) {
                        loadWeather(query)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun loadWeather(cityName: String) {
        lifecycleScope.launch {
            try {
                showLoading(true)
                if (!api.getWeatherByName(cityName).name.isNullOrEmpty())
                    doTransactionToDetail(api.getWeatherByName(cityName).name.toString())
            } catch (e: HttpException) {
                Snackbar.make(requireView(), R.string.error, Snackbar.LENGTH_LONG)
                    .show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun onWeatherClick(weatherResponse: WeatherResponse) {
        val cityName = weatherResponse.name
        if (cityName != null) {
            doTransactionToDetail(cityName)
        }
    }

    private fun doTransactionToDetail(cityName: String) {
        parentFragmentManager.beginTransaction().setCustomAnimations(
            com.google.android.material.R.anim.abc_fade_in,
            com.google.android.material.R.anim.abc_fade_out,
            com.google.android.material.R.anim.abc_fade_in,
            com.google.android.material.R.anim.abc_fade_out
        )
            .replace(
                R.id.fragment_container,
                DetailFragment.newInstance(cityName)
            )
            .addToBackStack("ToMainFragment").commit()
    }

    private fun showLoading(isShow: Boolean) {
        binding?.pbLoad?.isVisible = isShow
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                latitude = location?.latitude
                longitude = location?.longitude
                lifecycleScope.launch {
                    citiesRepository = api.getNearestCities(latitude, longitude).list
                    setListAdapterConfig()
                }
            }
    }

    private fun setListAdapterConfig() {
        listAdapter?.submitList(citiesRepository)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
