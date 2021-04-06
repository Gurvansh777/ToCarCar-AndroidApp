package com.example.tocarcar.ui.bookings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tocarcar.R
import com.example.tocarcar.databinding.CardViewMyBookingsBinding
import com.example.tocarcar.entity.Posting

/**
 * List Adapter to set bookings
 * @author Gurvansh
 */
class MyBookingsListAdapter(private val bookingsList: List<Posting>, private val listener: MyBookingsListAdapter.PostingsListItemListener, val context: Context) :
        RecyclerView.Adapter<MyBookingsListAdapter.ViewHolder>() {


        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val binding = CardViewMyBookingsBinding.bind(itemView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.card_view_my_bookings, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = bookingsList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val booking = bookingsList[position]
            with(holder.binding) {
                val carStr = "Company - ${booking.car.companyName}\n"+
                        "Model - ${booking.car.modelName}\n"+
                        "Year - ${booking.car.year}\n"+
                        "Booked - ${booking.dateFrom} to ${booking.dateTo}\n"+
                        "Rent - $${booking.rentPerDay} per day\n"+
                        "Booking Status - Confirmed"

                tvBookingDetailsCardView.text = carStr

                val resID: Int = context.getResources().getIdentifier(booking.car.photo, "drawable", context.packageName)
                //Log.i("RES_ID", resID.toString())
                //Log.i("RES_ID", R.drawable.car11.toString())
                imageViewCarBookings.setImageResource(resID)
                root.setOnClickListener{
                    listener.displayBooking(position)
                }

            }

        }

        interface PostingsListItemListener {
            fun displayBooking(bookingPosition: Int)
        }
}