package com.example.tocarcar.ui.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.tocarcar.Constants
import com.example.tocarcar.FlashScreenActivity
import com.example.tocarcar.api.ApiHelper
import com.example.tocarcar.databinding.FragmentHomeBinding
import com.example.tocarcar.entity.Posting
import com.google.gson.JsonArray
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * class to manage Home display
 * @author Gurvansh
 */
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var anyChartView: AnyChartView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences(Constants.MY_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE)

        var firstName = sharedPreferences.getString(Constants.USER_FIRST_NAME, "")
        firstName = firstName?.replace("\"", "")
        var lastName = sharedPreferences.getString(Constants.USER_LAST_NAME, "")
        lastName = lastName?.replace("\"", "")
        homeBinding.textViewWelcomeHome.text = "Welcome $firstName $lastName"

        anyChartView = homeBinding.anyChartViewId

        homeBinding.btnLogout.setOnClickListener {
            logout()
        }

        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPieChart()
    }

    /**
     * function to handle logout
     */
    private fun logout() {
        sharedPreferences.edit().clear().apply()
        startActivity(Intent(activity, FlashScreenActivity::class.java))
        requireActivity().finish()
    }

    /**
     * function to setup Pie Chart
     * @author Harman
     */
    private fun setupPieChart(){

        val retroFit = Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiHelperService = retroFit.create(ApiHelper::class.java)

        val emailOfLoggedInUser = sharedPreferences.getString(Constants.USER_EMAIL, "")

        val requestBody: String = "{ \"car.ownerEmail\": \"" + emailOfLoggedInUser + "\" }"

        val getUserPostings: Call<JsonArray> = apiHelperService.getUserPostings(requestBody)

        getUserPostings.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>?, response: Response<JsonArray>?) {
                val jsonPostings = response?.body().toString()
                Log.i("USERPOSTINGS", jsonPostings)
                var postingsList = getPostingsFromJson(jsonPostings)

                if(postingsList.size > 0){
                    var unApprovedPostingsCount = 0
                    var approvedPostingsCount = 0
                    var bookedPostingsCount = 0
                    for(posting in postingsList){
                        if(posting.isApproved == 0){
                            unApprovedPostingsCount++
                        }
                        else{
                            if(posting.isBooked == 1){
                                bookedPostingsCount++
                            }
                            else{
                                approvedPostingsCount++
                            }
                        }
                    }

                    var months=  arrayOf("Unapproved Postings", "Approved Postings", "Booked Postings")
                    var earnings: IntArray = intArrayOf(unApprovedPostingsCount, approvedPostingsCount, bookedPostingsCount)

                    var pie: Pie = AnyChart.pie()

                    var dataEntries =  ArrayList<DataEntry>()

                    var i: Int = 0

                    while (i < months.size) {
                        dataEntries.add(ValueDataEntry(months[i], earnings[i]))
                        i++
                    }

                    pie.data(dataEntries)

                    anyChartView.setChart(pie)
                    noPostingsTextview.visibility = View.INVISIBLE
                    anyChartView.visibility = View.VISIBLE
                }

                else{
                    noPostingsTextview.setText("No posting added yet!")
                    anyChartView.visibility = View.INVISIBLE
                    noPostingsTextview.visibility = View.VISIBLE
                }

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
}