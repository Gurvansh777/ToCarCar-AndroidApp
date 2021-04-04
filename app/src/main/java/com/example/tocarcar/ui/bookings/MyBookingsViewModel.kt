package com.example.tocarcar.ui.bookings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tocarcar.entity.Posting

class MyBookingsViewModel : ViewModel() {
    var bookingsList = MutableLiveData<List<Posting>>()
}