package com.example.tocarcar.ui.findcars

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
import com.example.tocarcar.databinding.FragmentFindCarsBinding
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

class FindCarsFragment : Fragment() , FindCarsListAdapter.AllPostingsListItemListener {

    private lateinit var findCarsViewModel: FindCarsViewModel
    private lateinit var binding : FragmentFindCarsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: FindCarsListAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        findCarsViewModel =
                ViewModelProvider(this).get(FindCarsViewModel::class.java)
        binding = FragmentFindCarsBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE)

        getAllPostedCars()
        findCarsViewModel.allPostingsList.observe(viewLifecycleOwner, {
            adapter = FindCarsListAdapter(it, this)
            binding.recyclerViewFindCarsList.adapter = adapter
            binding.recyclerViewFindCarsList.layoutManager = LinearLayoutManager(activity)
        })
        return binding.root
    }

    private fun getAllPostedCars() {
        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiHelperService = retroFit.create(ApiHelper::class.java)

        val emailOfLoggedInUser = sharedPreferences.getString(Constants.USER_EMAIL, "")
        //{"car.ownerEmail": { $ne: "test" }, "isBooked" : 0, "isApproved" : 1}
        val requestBody: String = "{ \"car.ownerEmail\": { \"\$ne\" :\"" + emailOfLoggedInUser + "\" }, \"isBooked\" : 0, \"isApproved\" : 1 }"

        val getUserPostings: Call<JsonArray> = apiHelperService.getUserPostings(requestBody)

        getUserPostings.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>?, response: Response<JsonArray>?) {
                val jsonPostings = response?.body().toString()
                Log.i("ALL_POSTINGS", jsonPostings)
                var postingsList = getPostingsFromJson(jsonPostings)
                findCarsViewModel.allPostingsList.value = postingsList
            }

            override fun onFailure(call: Call<JsonArray>?, t: Throwable?) {
                Log.e("ALL_POSTINGS_FAILED", t?.message.toString())
            }
        })
    }

    fun getPostingsFromJson(text: String): List<Posting>{
        var postings: List<Posting> = ArrayList();
        try{
            val myType = Types.newParameterizedType(List::class.java, Posting::class.java)
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter: JsonAdapter<List<Posting>> = moshi.adapter(myType)

            postings = (adapter.fromJson(text))!!.toList();
            println("ALL_POSTINGS: $postings")
        }
        catch (e: Exception){
            println("Error" + e.message)
        }
        return postings
    }

    override fun displayPosting(postingPosition: Int) {
        Log.i("SELECTED POSTING INDEX", postingPosition.toString())
    }
}