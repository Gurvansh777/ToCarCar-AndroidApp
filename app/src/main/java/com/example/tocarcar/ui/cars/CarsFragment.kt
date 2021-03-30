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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tocarcar.Constants
import com.example.tocarcar.api.ApiHelper
import com.example.tocarcar.databinding.FragmentCarsBinding
import com.example.tocarcar.entity.Car
import com.google.gson.JsonArray
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.fragment_cars.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarsFragment : Fragment(), CarsListAdapter.CarsListItemListener {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var carsViewModel: CarsViewModel
    private lateinit var binding : FragmentCarsBinding
    private lateinit var adapter: CarsListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE)

        binding = FragmentCarsBinding.inflate(inflater,container,false)

        carsViewModel = ViewModelProvider(this).get(CarsViewModel::class.java)

        getUserCars()

        carsViewModel.carsList.observe(viewLifecycleOwner, {
            println("I m in observe")
            adapter = CarsListAdapter(it, this)
            binding.recyclerViewCarsList.adapter = adapter
            binding.recyclerViewCarsList.layoutManager = LinearLayoutManager(activity)
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        floatingBtnAddCar.setOnClickListener{
            val action = CarsFragmentDirections.actionNavigationCarsToAddCar()
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        getUserCars()
        super.onResume()
    }

    private fun getUserCars() {
        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiHelperService = retroFit.create(ApiHelper::class.java)

        val emailOfLoggedInUser = sharedPreferences.getString(Constants.USER_EMAIL, "")

        val requestBody: String = "{ \"ownerEmail\": \"" + emailOfLoggedInUser + "\" }"

        val getUserCars: Call<JsonArray> = apiHelperService.getUserCars(requestBody)

        getUserCars.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>?, response: Response<JsonArray>?) {
                val jsonCars = response?.body().toString()
                Log.i("USERCARS", jsonCars)
                var carsList = getCarsFromJson(jsonCars)
                carsViewModel.carsList.value = carsList
            }

            override fun onFailure(call: Call<JsonArray>?, t: Throwable?) {
                Log.e("CAR_ADD_FAILED", t?.message.toString())
            }
        })
    }

    fun getCarsFromJson(text: String): List<Car>{
        var cars: List<Car> = ArrayList();
        try{
            val myType = Types.newParameterizedType(List::class.java, Car::class.java)
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter: JsonAdapter<List<Car>> = moshi.adapter(myType)

            cars = (adapter.fromJson(text))!!.toList();
            println("Cars: $cars")
        }
        catch (e: Exception){
            println("Error" + e.message)
        }

        return cars

    }


    override fun displayCar(carPosition: Int) {
        Log.e("CAR_Position", carPosition.toString())
        val car = carsViewModel.carsList.value?.get(carPosition)
        val action = CarsFragmentDirections.actionNavigationCarsToPostingsFragment(car!!)
        findNavController().navigate(action)
    }
}