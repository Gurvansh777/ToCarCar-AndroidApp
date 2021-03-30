package com.example.tocarcar.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Car(
    var companyName : String = "",
    var modelName : String = "",
    var licensePlate: String = "",
    var year : Int = 0,
    var kms : Int = 0,
    var ownerEmail: String = ""
) : Parcelable

