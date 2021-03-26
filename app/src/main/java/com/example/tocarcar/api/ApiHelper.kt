package com.example.tocarcar.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiHelper {
    @Headers("objectName: cars", "Content-Type: application/json")
    @POST("/api/addrecord")
    fun addCar(@Body body : String) : Call<JsonObject>

    @Headers("objectName: cars", "Content-Type: application/json")
    @POST("/api/getspecificrecords")
    fun getUserCars(@Body body : String) : Call<JsonArray>
}