package com.mumu.smileweather.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mumu.smileweather.R
import com.mumu.smileweather.logic.model.Place
import com.mumu.smileweather.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: PlaceFragment, private val places: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            val place = places[adapterPosition]
            val activity = fragment.activity
            if (activity is WeatherActivity) {
                activity.weatherBinding.drawer.closeDrawers()
                activity.weatherViewModel.placeName = place.name
                activity.weatherViewModel.lat = place.location.lat
                activity.weatherViewModel.lng = place.location.lng
                activity.queryWeather()
            } else {
                WeatherActivity.startWeatherActivity(
                    parent.context,
                    place.name,
                    place.location.lng,
                    place.location.lat
                )
                fragment.activity?.finish()
            }
            fragment.viewModel.savePlace(place)
        }
        return viewHolder
    }

    override fun getItemCount() = places.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }
}