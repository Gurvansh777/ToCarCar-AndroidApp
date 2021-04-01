package com.example.tocarcar.ui.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tocarcar.Constants
import com.example.tocarcar.FlashScreenActivity
import com.example.tocarcar.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences(Constants.MY_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE)

        homeBinding.btnLogout.setOnClickListener {
            logout()
        }

        return homeBinding.root
    }

    private fun logout() {
        sharedPreferences.edit().clear().apply()
        startActivity(Intent(activity, FlashScreenActivity::class.java))
        requireActivity().finish()
    }
}