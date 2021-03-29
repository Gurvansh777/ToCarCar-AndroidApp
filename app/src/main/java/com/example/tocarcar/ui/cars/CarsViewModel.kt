package com.example.tocarcar.ui.cars

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tocarcar.entity.Car
import com.example.tocarcar.entity.Posting

class CarsViewModel : ViewModel() {

    var carsList = MutableLiveData<List<Car>>()
    var postingsList = MutableLiveData<List<Posting>>()

}