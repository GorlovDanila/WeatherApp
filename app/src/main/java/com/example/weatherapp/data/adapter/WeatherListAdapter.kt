package com.example.weatherapp.data.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.response.WeatherResponse

class WeatherListAdapter(
    private val actionNext: (WeatherResponse) -> Unit,
) : ListAdapter<WeatherResponse, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<WeatherResponse>() {
        override fun areItemsTheSame(
            oldItem: WeatherResponse,
            newItem: WeatherResponse
        ): Boolean = (oldItem as? WeatherResponse)?.name == (newItem as? WeatherResponse)?.name

        override fun areContentsTheSame(
            oldItem: WeatherResponse,
            newItem: WeatherResponse
        ): Boolean = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_city -> WeatherHolder.create(parent, actionNext)
            else -> throw IllegalArgumentException("Error!")
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val currentItem = currentList[position]
        when (holder) {
            is WeatherHolder -> holder.onBind(currentItem as WeatherResponse)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (currentList[position]) {
            is WeatherResponse -> R.layout.item_city
            else -> throw IllegalArgumentException("Error!")
        }
}
