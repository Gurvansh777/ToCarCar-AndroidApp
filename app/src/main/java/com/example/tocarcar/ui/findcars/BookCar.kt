package com.example.tocarcar.ui.findcars

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tocarcar.Constants
import com.example.tocarcar.api.ApiHelper
import com.example.tocarcar.databinding.FragmentBookCarBinding
import com.example.tocarcar.entity.Car
import com.example.tocarcar.entity.Posting
import com.example.tocarcar.utility.JsonHelper
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookCar : Fragment() {

    private lateinit var binding : FragmentBookCarBinding
    private lateinit var sharedPreferences: SharedPreferences

    val args: BookCarArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE)
        binding = FragmentBookCarBinding.inflate(inflater, container, false)
        val posting : Posting = args.posting
        val car : Car = posting.car
        var postingDetails = "${car.year} ${car.companyName} ${car.modelName}"
        postingDetails += "\nMileage: ${car.kms} kms"
        postingDetails += "\nAvailable: ${posting.dateFrom} - ${posting.dateTo}"
        postingDetails += "\nRent: $${posting.rentPerDay}/per day"
        binding.postingDetailsInBooking.text = postingDetails

        val resID: Int = requireActivity().getApplicationContext().getResources().getIdentifier(car.photo, "drawable", requireActivity().getApplicationContext().packageName)
        binding.imageViewCarInBookCar.setImageResource(resID)


        binding.bookCarButtonInBooking.setOnClickListener{
            val email = sharedPreferences.getString(Constants.USER_EMAIL, "")

            val adapter = JsonHelper.getMoshiPostingAdapter()
            val postingJSON = adapter.toJson(posting)

            var updateString = "\"isBooked\": 1, \"bookedBy\": \"$email\" "

            var requestBody = JsonHelper.getUpdateRequestBody(postingJSON, updateString)

            Log.i("BOOK_CAR", requestBody)

            updatePosting(requestBody)

            val action = BookCarDirections.actionBookCarToNavigationHome()
            findNavController().navigate(action)

        }

        binding.cancelButtonInBooking.setOnClickListener {
            findNavController().navigateUp()
        }


        return binding.root
    }

    private fun updatePosting(requestBody: String) {
        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiHelperService = retroFit.create(ApiHelper::class.java)

        val updateCar: Call<JsonObject> = apiHelperService.updatePosting(requestBody)

        updateCar.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                Log.i("RESPONSE", response?.body().toString())
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                Log.e("CAR_UPDATE_FAILED", t?.message.toString())
            }
        })
    }

}