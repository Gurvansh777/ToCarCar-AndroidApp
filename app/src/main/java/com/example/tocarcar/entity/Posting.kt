package com.example.tocarcar.entity

data class Posting(
    var car: Car = Car(),
    var email: String = "",
    var dateFrom: String = "",
    var dateTo: String = "",
    var rentPerDay: Float = 0.0F,
    var isApproved: Int = 0,
    var isBooked: Int = 0,
    var bookedBy: String = ""
)

