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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tocarcar.Constants
import com.example.tocarcar.api.ApiHelper
import com.example.tocarcar.databinding.FragmentPostingsBinding
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

class PostingsFragment : Fragment() {

    lateinit var binding: FragmentPostingsBinding
    val args: PostingsFragmentArgs by navArgs()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var carsViewModel: CarsViewModel
    private lateinit var adapter: PostingsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPostingsBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE)
        carsViewModel = ViewModelProvider(this).get(CarsViewModel::class.java)
        getUserPostings()

        carsViewModel.postingsList.observe(viewLifecycleOwner, {
            adapter = PostingsListAdapter(it)
            binding.recyclerViewPostingsList.adapter = adapter
            binding.recyclerViewPostingsList.layoutManager = LinearLayoutManager(activity)
            if(it.isEmpty()){
                binding.tvNoPostingsFound.setText("No postings found \n   ${args.car.companyName} - ${args.car.modelName}")
                binding.recyclerViewPostingsList.visibility = View.INVISIBLE
                binding.tvNoPostingsFound.visibility = View.VISIBLE
            }else{
                binding.recyclerViewPostingsList.visibility = View.VISIBLE
                binding.tvNoPostingsFound.visibility = View.INVISIBLE
            }
        })

        binding.floatingBtnAddPosting.setOnClickListener {
            val action = PostingsFragmentDirections.actionPostingsFragmentToAddPosting(args.car)
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onResume() {
        getUserPostings()
        super.onResume()
    }

    private fun getUserPostings() {
        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiHelperService = retroFit.create(ApiHelper::class.java)

        val emailOfLoggedInUser = sharedPreferences.getString(Constants.USER_EMAIL, "")

        val requestBody: String = "{ \"car.ownerEmail\": \"" + emailOfLoggedInUser + "\", \"car.licensePlate\": \""+args.car.licensePlate+"\" }"

        val getUserPostings: Call<JsonArray> = apiHelperService.getUserPostings(requestBody)

        getUserPostings.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>?, response: Response<JsonArray>?) {
                val jsonPostings = response?.body().toString()
                Log.i("USERPOSTINGS", jsonPostings)
                var postingsList = getPostingsFromJson(jsonPostings)
                carsViewModel.postingsList.value = postingsList
            }

            override fun onFailure(call: Call<JsonArray>?, t: Throwable?) {
                Log.e("USER_POSTINGS_FAILED", t?.message.toString())
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
            println("POSTINGS: $postings")
        }
        catch (e: Exception){
            println("Error" + e.message)
        }
        return postings
    }

}