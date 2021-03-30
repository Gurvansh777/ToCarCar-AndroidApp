package com.example.tocarcar.ui.findcars

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tocarcar.entity.Posting

class FindCarsViewModel : ViewModel() {

    var allPostingsList = MutableLiveData<List<Posting>>()
}