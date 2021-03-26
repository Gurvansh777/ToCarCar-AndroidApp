package com.example.tocarcar.ui.cars

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE)
        return inflater.inflate(R.layout.fragment_add_car, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAddCar.setOnClickListener{
            val companyName = etCompanyAddCar.text.toString()
            val modelName = etModelAddCar.text.toString()
            val licensePlate = etLicensePlateAddCar.text.toString()
            val year = etYearAddCar.text.toString().toInt()
            val kms = etKmsAddCar.text.toString().toInt()

            val email = sharedPreferences.getString(Constants.USER_EMAIL, "")

            var carObj = Car(companyName, modelName, licensePlate, year, kms, email!!)
            addCar(carObj)
        }
    }


    private fun addCar(car: Car) {
        progressBarAddCar.visibility = View.VISIBLE
        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiHelperService = retroFit.create(ApiHelper::class.java)

        val adapter = JsonHelper.getMoshiCarAdapter()
        val requestBody: String = adapter.toJson(car)

        val addCar: Call<JsonObject> = apiHelperService.addCar(requestBody)

        addCar.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                Log.i("RESPONSE", response?.body().toString())
                progressBarAddCar.visibility = View.INVISIBLE

                //val action = CarsFragmentDirections.actionNavigationCarsToAddCar()
                //findNavController().navigate(action)
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                Log.e("CAR_ADD_FAILED", t?.message.toString())
                progressBarAddCar.visibility = View.INVISIBLE
                //finish()
            }
        })
    }


}