package com.example.tocarcar.ui.bookings

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tocarcar.Constants
import com.example.tocarcar.api.ApiHelper
import com.example.tocarcar.databinding.MyBookingsFragmentBinding
import com.example.tocarcar.entity.Posting
import com.google.gson.JsonArray
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Class to manage bookings
 * @author Gurvansh
 */
class MyBookingsFragment : Fragment(), MyBookingsListAdapter.PostingsListItemListener {

    private lateinit var viewModel: MyBookingsViewModel
    private lateinit var binding: MyBookingsFragmentBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: MyBookingsListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = MyBookingsFragmentBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE)
        viewModel = ViewModelProvider(this).get(MyBookingsViewModel::class.java)

        getUserBookings()

        viewModel.bookingsList.observe(viewLifecycleOwner, {
            adapter = MyBookingsListAdapter(it, this, requireActivity().getApplicationContext())
            binding.recyclerViewMyBookingsList.adapter = adapter
            binding.recyclerViewMyBookingsList.layoutManager = LinearLayoutManager(activity)
            if(it.isEmpty()){
                binding.tvNoBookingsFound.setText("No bookings found")
                binding.recyclerViewMyBookingsList.visibility = View.INVISIBLE
                binding.tvNoBookingsFound.visibility = View.VISIBLE
            }else{
                binding.recyclerViewMyBookingsList.visibility = View.VISIBLE
                binding.tvNoBookingsFound.visibility = View.INVISIBLE
            }
        })
        return binding.root
    }

    /**
     * api call to get user bookings
     */
    private fun getUserBookings() {
        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiHelperService = retroFit.create(ApiHelper::class.java)

        val emailOfLoggedInUser = sharedPreferences.getString(Constants.USER_EMAIL, "")

        val requestBody: String = "{ \"bookedBy\": \"" + emailOfLoggedInUser + "\" }"

        val getUserPostings: Call<JsonArray> = apiHelperService.getUserPostings(requestBody)

        getUserPostings.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>?, response: Response<JsonArray>?) {
                val jsonPostings = response?.body().toString()
                Log.i("USERPOSTINGS", jsonPostings)
                var postingsList = getPostingsFromJson(jsonPostings)
                viewModel.bookingsList.value = postingsList
            }

            override fun onFailure(call: Call<JsonArray>?, t: Throwable?) {
                Log.e("USER_POSTINGS_FAILED", t?.message.toString())
            }
        })
    }

    /**
     * Helper function to parse JSON
     */
    fun getPostingsFromJson(text: String): List<Posting>{
        var postings: List<Posting> = ArrayList();
        try{
            val myType = Types.newParameterizedType(List::class.java, Posting::class.java)
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter: JsonAdapter<List<Posting>> = moshi.adapter(myType)

            postings = (adapter.fromJson(text))!!.toList();
            println("POSTINGS: $postings")
        }
        catch (e: Exception){
            println("Error" + e.message)
        }
        return postings
    }

    override fun displayBooking(bookingPosition: Int) {

    }
}