package com.sunnyweather.android.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.ui.weather.WeatherActivity

class PlaceAdapter(
    private val fragment: PlaceFragment,
    private val placeList: List<Place>
) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.cardPlaceNameText)
        val placeAddress: TextView = view.findViewById(R.id.cardPlaceAddressText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        val holder = ViewHolder(view)

        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val place = placeList[position]
            val coordinate = place.location
            val placeName = place.name

            fragment.viewModel.savePlace(place)

            val context = parent.context
            val intent = Intent(context, WeatherActivity::class.java)
            intent.putExtra(context.getString(R.string.intentExtraLng), coordinate.longitude)
            intent.putExtra(context.getString(R.string.intentExtraLat), coordinate.latitude)
            intent.putExtra(context.getString(R.string.intentExtraPlaceName), placeName)
            fragment.startActivity(intent)
            fragment.activity?.finish()
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

}