package com.example.tocarcar.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Car Entity
 */
@Parcelize
@Entity
data class Car(
    var companyName : String = "",
    var modelName : String = "",
    @PrimaryKey
    var licensePlate: String = "",
    var year : Int = 0,
    var kms : Int = 0,
    var ownerEmail: String = "",
    var photo: String = ""
) : Parcelable

