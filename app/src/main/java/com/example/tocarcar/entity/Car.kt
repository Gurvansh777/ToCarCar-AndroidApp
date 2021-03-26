package com.example.tocarcar.entity

import android.os.Parcelable
import com.squareup.moshi.Json

data class Car(
    var companyName : String = "",
    var modelName : String = "",
    var licensePlate: String = "",
    var year : Int = 0,
    var kms : Int = 0,
    var ownerEmail: String = ""
)

