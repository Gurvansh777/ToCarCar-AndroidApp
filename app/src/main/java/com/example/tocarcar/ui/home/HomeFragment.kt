package com.example.tocarcar.ui.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.tocarcar.Constants
import com.example.tocarcar.FlashScreenActivity
import com.example.tocarcar.R
import com.example.tocarcar.databinding.FragmentHomeBinding
import com.example.tocarcar.ui.cars.CarsFragmentDirections
import kotlinx.android.synthetic.main.fragment_cars.*
import java.util.ArrayList
import kotlin.reflect.jvm.internal.impl.protobuf.LazyStringArrayList

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

        val firstName = sharedPreferences.getString(Constants.USER_FIRST_NAME, "")
        val lastName = sharedPreferences.getString(Constants.USER_LAST_NAME, "")
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

    private fun logout() {
        sharedPreferences.edit().clear().apply()
        startActivity(Intent(activity, FlashScreenActivity::class.java))
        requireActivity().finish()
    }

    private fun setupPieChart(){
        var months=  arrayOf("Jan", "Feb", "Mar")
        var earnings: IntArray = intArrayOf(10, 20, 30)

        var pie: Pie = AnyChart.pie()

        var dataEntries =  ArrayList<DataEntry>()

        var i: Int = 0

        while (i < months.size) {
            dataEntries.add(ValueDataEntry(months[i], earnings[i]))
            i++
        }

        pie.data(dataEntries)

        anyChartView.setChart(pie)

    }
}