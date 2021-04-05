package com.example.tocarcar

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tocarcar.api.ApiHelper
import com.example.tocarcar.databinding.FragmentEditPostingBinding
import com.example.tocarcar.utility.JsonHelper
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class EditPosting : Fragment() {
    lateinit var binding: FragmentEditPostingBinding
    val args: EditPostingArgs by navArgs()
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentEditPostingBinding.inflate(inflater, container, false)
        binding.tvCarDetailsEditPosting.text =
            "${args.editPosting.car.companyName} ${args.editPosting.car.modelName} - ${args.editPosting.car.licensePlate}"

        val editPosting = args.editPosting
        binding.etDateFromEditPosting.setText(editPosting.dateFrom.toString())
        binding.etDateToEditPosting.setText(editPosting.dateTo.toString())
        binding.etRentPerDayEditPosting.setText(editPosting.rentPerDay.toString())
        sharedPreferences =
            requireActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFERENCES,
                AppCompatActivity.MODE_PRIVATE)

        binding.btnCancelEditPosting.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.etDateFromEditPosting.setOnClickListener {
            setDate(binding.etDateFromEditPosting)
        }

        binding.etDateToEditPosting.setOnClickListener {
            setDate(binding.etDateToEditPosting)
        }

        binding.btnEditPosting.setOnClickListener {
            val email = sharedPreferences.getString(Constants.USER_EMAIL, "")

            val adapter = JsonHelper.getMoshiPostingAdapter()
            val postingJSON = adapter.toJson(editPosting)
            val dateFrom = binding.etDateFromEditPosting.text.toString().trim()
            val dateTo = binding.etDateToEditPosting.text.toString().trim()
            val rentPerDayStr = binding.etRentPerDayEditPosting.text.toString()

            if (validateInputs(dateFrom, dateTo, rentPerDayStr)) {
                var updateString =
                    "\"dateFrom\": \"$dateFrom\", \"dateTo\": \"$dateTo\", \"rentPerDay\": ${rentPerDayStr.toDouble()} "
                var requestBody = JsonHelper.getUpdateRequestBody(postingJSON, updateString)
                Log.i("EDIT_POSTING", requestBody)
                editPosting(requestBody)
                findNavController().navigateUp()
            }
        }

        binding.btnDeletePosting.setOnClickListener {
            val adapter = JsonHelper.getMoshiPostingAdapter()
            val postingJSON = adapter.toJson(editPosting)

            deletePosting(postingJSON)
            findNavController().navigateUp()
        }
        return binding.root
    }

    private fun validateInputs(dateFrom: String, dateTo: String, rentPerDayStr: String): Boolean {
        var result = true
        if (rentPerDayStr.isBlank()) {
            binding.etRentPerDayEditPosting.error = "Cannot be blank"
            result = false
        }

        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
        val dtFrom: Date = sdf.parse(dateFrom)
        val dtTo: Date = sdf.parse(dateTo)
        val dtCmp = dtTo.compareTo(dtFrom)
        Log.i("DATECMP", dtCmp.toString())
        if (dtCmp <= 0) {
            Toast.makeText(requireActivity(),
                "Date from cannot be less than or equal to Date to",
                Toast.LENGTH_LONG).show()
            result = false
        }

        return result
    }

    private fun setDate(etDate: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                etDate.setText("$year/$monthOfYear/$dayOfMonth")
            },
            year,
            month,
            day)
        dpd.show()
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