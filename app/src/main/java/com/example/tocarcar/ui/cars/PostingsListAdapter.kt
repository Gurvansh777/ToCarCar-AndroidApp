package com.example.tocarcar.ui.cars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tocarcar.R
import com.example.tocarcar.databinding.CardViewPostingBinding
import com.example.tocarcar.entity.Posting

class PostingsListAdapter (private val postingsList: List<Posting>) :
        RecyclerView.Adapter<PostingsListAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val binding = CardViewPostingBinding.bind(itemView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.card_view_posting, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = postingsList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val posting = postingsList[position]
            with(holder.binding) {
                var str = "License Plate - ${posting.car.licensePlate}\n"+
                        "From ${posting.dateFrom} to ${posting.dateTo}\n Rent Per Day - $${posting.rentPerDay}"
                if(posting.isBooked == 1){
                    str+= "Booked By - ${posting.bookedBy}"
                }else{
                    str += "Approved - ${posting.isApproved}"
                }
                tvPostingDetailsCardView.setText(str)
            }

        }
}