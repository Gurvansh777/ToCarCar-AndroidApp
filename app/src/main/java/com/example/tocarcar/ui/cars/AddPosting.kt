package com.example.tocarcar.ui.cars

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
import com.example.tocarcar.Constants
import com.example.tocarcar.api.ApiHelper
import com.example.tocarcar.databinding.FragmentAddPostingBinding
import com.example.tocarcar.entity.Posting
import com.example.tocarcar.utility.JsonHelper
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_add_posting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class AddPosting : Fragment() {
    lateinit var binding: FragmentAddPostingBinding
    val args: AddPostingArgs by navArgs()
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddPostingBinding.inflate(inflater, container, false)
        binding.tvCarLicensePlate.text = "${args.car.companyName} ${args.car.modelName} - ${args.car.licensePlate}"
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.etDateFrom.setText("$year/$month/$day")
        binding.etDateTo.setText("$year/$month/$day")

        binding.etDateFrom.setOnClickListener {
            setDate(binding.etDateFrom)
        }

        binding.etDateTo.setOnClickListener {
            setDate(etDateTo)
        }

        binding.btnPost.setOnClickListener {
            val dateFrom = binding.etDateFrom.text.toString().trim()
            val dateTo = binding.etDateTo.text.toString().trim()
            val rentPerDayStr = binding.etRentPerDay.text.toString()
            val email = sharedPreferences.getString(Constants.USER_EMAIL, "")
            val isApproved = 0
            val isBooked = 0
            val bookedBy = ""
            if(validateInputs(dateFrom, dateTo, rentPerDayStr)){
                val postingObj = Posting(args.car, email!!, dateFrom, dateTo, rentPerDayStr.toDouble(), isApproved, isBooked, bookedBy)
                addPosting(postingObj)
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

    private fun validateInputs(dateFrom: String, dateTo: String, rentPerDay: String): Boolean {
        var result = true
        if(rentPerDay.isBlank()){
            binding.etRentPerDay.error = "Cannot be blank"
            result = false
        }

        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
        val dtFrom : Date = sdf.parse(dateFrom)
        val dtTo : Date = sdf.parse(dateTo)
        val dtCmp = dtTo.compareTo(dtFrom)

        if(dtCmp <= 0){
            Toast.makeText(requireActivity(), "Date from cannot be less than or equal to Date to", Toast.LENGTH_LONG).show()
            result = false
        }

        return result
    }

    private fun setDate(etDate: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            etDate.setText("$year/$monthOfYear/$dayOfMonth")
        }, year, month, day)
        dpd.show()
    }

    private fun addPosting(postingObj: Posting) {
        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiHelperService = retroFit.create(ApiHelper::class.java)

        val adapter = JsonHelper.getMoshiPostingAdapter()
        val requestBody: String = adapter.toJson(postingObj)

        val addCar: Call<JsonObject> = apiHelperService.addPosting(requestBody)

        addCar.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                Log.i("RESPONSE", response?.body().toString())
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                Log.e("CAR_ADD_FAILED", t?.message.toString())
            }
        })
    }

}