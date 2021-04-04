package com.example.tocarcar.ui.cars

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tocarcar.Constants
import com.example.tocarcar.R
import com.example.tocarcar.api.ApiHelper
import com.example.tocarcar.entity.Car
import com.example.tocarcar.utility.JsonHelper
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_add_car.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class addCar : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var carsViewModel: CarsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE)
        carsViewModel = ViewModelProvider(this).get(CarsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_add_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAddCar.setOnClickListener {
            val companyName = etCompanyAddCar.text.toString()
            val modelName = etModelAddCar.text.toString()
            val licensePlate = etLicensePlateAddCar.text.toString()
            val yearStr = etYearAddCar.text.toString()
            val kmsStr = etKmsAddCar.text.toString()
            val email = sharedPreferences.getString(Constants.USER_EMAIL, "")
            var randomNumber = (1..15).random()
            val photo = "car$randomNumber"

            if (validateInputs(companyName, modelName, licensePlate, yearStr, kmsStr)) {
                val carObj = Car(companyName, modelName, licensePlate, yearStr.toInt(), kmsStr.toInt(), email!!, photo)
                addCar(carObj)
                findNavController().navigateUp()
            }
        }
    }

    private fun validateInputs(
        companyName: String,
        modelName: String,
        licensePlate: String,
        yearStr: String,
        kmsStr: String
    ): Boolean {
        var result = true
        if(companyName.isEmpty()){
            etCompanyAddCar.error = "Cannot be blank"
            result = false
        }
        if(modelName.isEmpty()){
            etModelAddCar.error = "Cannot be blank"
            result = false
        }
        if(licensePlate.isEmpty()){
            etLicensePlateAddCar.error = "Cannot be blank"
            result = false
        }
        if(yearStr.isEmpty()){
            etYearAddCar.error = "Cannot be blank"
            result = false
        }
        if(kmsStr.isEmpty()){
            etKmsAddCar.error = "Cannot be blank"
            result = false
        }
        return result
    }


    private fun addCar(car: Car) {
        //local copy
        carsViewModel.addCar(car)

        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiHelperService = retroFit.create(ApiHelper::class.java)

        val adapter = JsonHelper.getMoshiCarAdapter()
        val requestBody: String = adapter.toJson(car)

        val addCar: Call<JsonObject> = apiHelperService.addCar(requestBody)

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