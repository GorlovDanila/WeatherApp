package com.example.weatherapp.presentation.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.App
import com.example.weatherapp.R
import com.example.weatherapp.data.weather.datasource.remote.response.WeatherResponse
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.domain.location.GetLocationUseCase
import com.example.weatherapp.domain.weather.GetCitiesWeatherUseCase
import com.example.weatherapp.domain.weather.GetWeatherUseCase
import com.example.weatherapp.presentation.presenters.MainViewModel
import com.example.weatherapp.presentation.ui.adapters.WeatherListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainMvvmFragment : Fragment(R.layout.fragment_main) {

    private var binding: FragmentMainBinding? = null
    private var listAdapter: WeatherListAdapter? = null
    private var citiesRepository: List<WeatherResponse?>? = null

    @Inject
    lateinit var getWeatherUseCase: GetWeatherUseCase

    @Inject
    lateinit var getCitiesWeatherUseCase: GetCitiesWeatherUseCase

    @Inject
    lateinit var getLocationUseCase: GetLocationUseCase

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.provideFactory(getWeatherUseCase, getCitiesWeatherUseCase, getLocationUseCase)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                lifecycleScope.launch{
                    viewModel.locationPerm(true)
                }
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                lifecycleScope.launch{
                    viewModel.locationPerm(true)
                }
            }
            else -> {
                lifecycleScope.launch{
                    viewModel.locationPerm(false)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)
        listAdapter = WeatherListAdapter{viewModel.onWeatherClick(it)}
        binding?.rvCities?.adapter = listAdapter
        binding?.rvCities?.layoutManager = LinearLayoutManager(requireContext())

        observeViewModel()

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
            lifecycleScope.launch{
                viewModel.locationPerm(true)
            }
        }

        binding?.run {
            swCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isNotEmpty()) {
                        viewModel.loadWeather(query)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun observeViewModel() {
        with(viewModel) {
            loading.observe(viewLifecycleOwner) {
                binding?.pbLoad?.isVisible = it
            }

            error.observe(viewLifecycleOwner) {
                if (it == null) return@observe
                setError(error.value?.toInt() ?: throw java.lang.NullPointerException("error is null"))
            }

            citiesList.observe(viewLifecycleOwner) {
                if (it == null) return@observe
                citiesRepository = it
                setListAdapterConfig()
            }

            location.observe(viewLifecycleOwner) {
                lifecycleScope.launch {
                    viewModel.getNearestCities(it?.latitude, it?.longitude)
                    setListAdapterConfig()
                }
            }

            transaction.observe(viewLifecycleOwner) {
                if (it != null) {
                    doTransactionToDetail(it)
                }
            }
        }
    }

    private fun setError(error: Int) =
        Snackbar.make(
            requireView(),
            this.getString(error),
            Snackbar.LENGTH_LONG,
        )
            .show()


    private fun doTransactionToDetail(cityName: String) {
        parentFragmentManager.beginTransaction().setCustomAnimations(
            com.google.android.material.R.anim.abc_fade_in,
            com.google.android.material.R.anim.abc_fade_out,
            com.google.android.material.R.anim.abc_fade_in,
            com.google.android.material.R.anim.abc_fade_out
        )
            .replace(
                R.id.fragment_container,
                DetailsMvvmFragment.newInstance(cityName)
            )
            .addToBackStack("ToMainFragment").commit()
    }

    private fun setListAdapterConfig() {
        binding?.rvCities?.adapter = listAdapter
        listAdapter?.submitList(citiesRepository)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
