package com.example.tocarcar.entity

data class Posting(
    var licensePlate: String = "",
    var ownerEmail: String = "",
    var dateFrom: String = "",
    var dateTo: String = "",
    var rentPerDay: Float = 0.0F,
    var isApproved: Int = 0,
    var isBooked: Int = 0,
    var bookedBy: String = ""
)

