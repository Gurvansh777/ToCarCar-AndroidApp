package com.example.tocarcar.ui.cars

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tocarcar.R
import com.example.tocarcar.databinding.CardViewCarBinding
import com.example.tocarcar.entity.Car

class CarsListAdapter (private val carList: List<Car>, private val listener: CarsListItemListener, val context: Context) :
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
                val carStr = "Company - ${car.companyName}\n"+
                        "Model - ${car.modelName}\n"+
                        "Year - ${car.year}\n"+
                        "License Plate - ${car.licensePlate}\n"+
                        "Mileage - ${car.kms} Kms"
                tvCarDetailsCardView.text = carStr

                val resID: Int = context.getResources().getIdentifier(car.photo, "drawable", context.packageName)
                //Log.i("RES_ID", resID.toString())
                //Log.i("RES_ID", R.drawable.car11.toString())
                imageViewCar.setImageResource(resID)
                root.setOnClickListener{
                    listener.displayCar(position)
                }

            }

        }

        interface CarsListItemListener {
            fun displayCar(carPosition: Int)
        }
}