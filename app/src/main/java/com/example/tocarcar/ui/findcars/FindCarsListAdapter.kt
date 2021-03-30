package com.example.tocarcar.ui.findcars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tocarcar.R
import com.example.tocarcar.databinding.CardViewFindCarBinding
import com.example.tocarcar.entity.Posting

class FindCarsListAdapter (private val allPostingsList: List<Posting>, private val listener: AllPostingsListItemListener) :
        RecyclerView.Adapter<FindCarsListAdapter.ViewHolder>() {


        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val binding = CardViewFindCarBinding.bind(itemView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.card_view_find_car, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = allPostingsList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val posting = allPostingsList[position]
            with(holder.binding) {
                tvFindCarDetailsCardView.text = "${posting.car.companyName} - "+
                        "${posting.car.modelName} -"+
                        "${posting.car.year}\n"+
                        "Available - ${posting.dateFrom} to ${posting.dateTo}\n"+
                        "Rent per day - $${posting.rentPerDay}"

                root.setOnClickListener{
                    listener.displayPosting(position)
                }

            }

        }

        interface AllPostingsListItemListener {
            fun displayPosting(postingPosition: Int)
        }
}