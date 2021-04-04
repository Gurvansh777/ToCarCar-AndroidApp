package com.example.tocarcar

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
import com.example.tocarcar.api.ApiHelper
import com.example.tocarcar.databinding.FragmentEditPostingBinding
import com.example.tocarcar.entity.Posting
import com.example.tocarcar.ui.findcars.BookCarDirections
import com.example.tocarcar.utility.JsonHelper
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditPosting : Fragment()  {
    lateinit var binding: FragmentEditPostingBinding
    val args: EditPostingArgs by navArgs()
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentEditPostingBinding.inflate(inflater, container, false)
        binding.tvCarDetailsEditPosting.text = "${args.editPosting.car.companyName} ${args.editPosting.car.modelName} - ${args.editPosting.car.licensePlate}"

        val editPosting = args.editPosting
        binding.etDateFromEditPosting.setText(editPosting.dateFrom.toString())
        binding.etDateToEditPosting.setText(editPosting.dateTo.toString())
        binding.etRentPerDayEditPosting.setText(editPosting.rentPerDay.toString())
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE)

        binding.btnCancelEditPosting.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnEditPosting.setOnClickListener {
            val email = sharedPreferences.getString(Constants.USER_EMAIL, "")

            val adapter = JsonHelper.getMoshiPostingAdapter()
            val postingJSON = adapter.toJson(editPosting)
            val dateFrom = binding.etDateFromEditPosting.text.toString().trim()
            val dateTo = binding.etDateToEditPosting.text.toString().trim()
            val rentPerDay : Double = binding.etRentPerDayEditPosting.text.toString().toDouble()

            var updateString = "\"dateFrom\": \"$dateFrom\", \"dateTo\": \"$dateTo\", \"rentPerDay\": $rentPerDay "

            var requestBody = JsonHelper.getUpdateRequestBody(postingJSON, updateString)

            Log.i("EDIT_POSTING", requestBody)

            editPosting(requestBody)

            findNavController().navigateUp()
        }

        binding.btnDeletePosting.setOnClickListener {
            val adapter = JsonHelper.getMoshiPostingAdapter()
            val postingJSON = adapter.toJson(editPosting)

            deletePosting(postingJSON)
            findNavController().navigateUp()
        }
        return binding.root
    }

    private fun editPosting(requestBody: String) {
        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiHelperService = retroFit.create(ApiHelper::class.java)

        val updateCar: Call<JsonObject> = apiHelperService.updatePosting(requestBody)

        updateCar.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                Log.i("RESPONSE", response?.body().toString())
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                Log.e("POSTING_UPDATE_FAILED", t?.message.toString())
            }
        })
    }

    private fun deletePosting(requestBody: String) {
        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiHelperService = retroFit.create(ApiHelper::class.java)

        val deletePosting: Call<JsonObject> = apiHelperService.deletePosting(requestBody)

        deletePosting.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                Log.i("RESPONSE", response?.body().toString())
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                Log.e("POSTING_DELETE_FAILED", t?.message.toString())
            }
        })
    }

}