package com.example.tocarcar.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiHelper {
    @Headers("objectName: cars", "Content-Type: application/json")
    @POST("/api/addrecord")
    fun addCar(@Body body : String) : Call<JsonObject>

    @Headers("objectName: cars", "Content-Type: application/json")
    @POST("/api/getspecificrecords")
    fun getUserCars(@Body body : String) : Call<JsonArray>

    @Headers("objectName: postings", "Content-Type: application/json")
    @POST("/api/addrecord")
    fun addPosting(@Body body : String) : Call<JsonObject>

    @Headers("objectName: postings", "Content-Type: application/json")
    @POST("/api/getspecificrecords")
    fun getUserPostings(@Body body : String) : Call<JsonArray>

    @Headers("objectName: postings", "Content-Type: application/json")
    @POST("/api/updaterecord")
    fun updatePosting(@Body body : String) : Call<JsonObject>
}