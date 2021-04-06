package com.example.tocarcar.ui.bookings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tocarcar.db.AppDatabase
import com.example.tocarcar.entity.Car
import com.example.tocarcar.entity.Posting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyBookingsViewModel(app: Application)  : AndroidViewModel(app) {
    var bookingsList = MutableLiveData<List<Posting>>()
    private val database = AppDatabase.getInstance(app)


    fun addBookings(car: Car) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.carDao()?.insertCar(car)
            }
        }
    }
}