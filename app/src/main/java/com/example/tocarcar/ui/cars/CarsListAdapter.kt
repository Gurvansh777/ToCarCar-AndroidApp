package com.example.tocarcar.ui.cars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tocarcar.R
import com.example.tocarcar.databinding.CardViewCarBinding
import com.example.tocarcar.entity.Car

class CarsListAdapter (private val carList: List<Car>, private val listener: CarsListItemListener) :
        RecyclerView.Adapter<CarsListAdapter.ViewHolder>() {


        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val binding = CardViewCarBinding.bind(itemView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.card_view_car, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = carList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val car = carList[position]
            with(holder.binding) {
                println("CarModel ${car.modelName}")
                tvCarDetailsCardView.text = car.modelName
                root.setOnClickListener{
                    listener.displayCar(car.licensePlate)
                }

            }

        }

        interface CarsListItemListener {
            fun displayCar(carLicensePlate: String)
        }
}