package com.example.tocarcar.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Api Helper interface to manage mongo db api calls
 * @author Harman, Gurvansh
 */
interface ApiHelper {
    /**
     *function to add cars in mongo db
     */
    @Headers("objectName: cars", "Content-Type: application/json")
    @POST("/api/addrecord")
    fun addCar(@Body body : String) : Call<JsonObject>

    /**
     *function to get user cars from mongo db
     */
    @Headers("objectName: cars", "Content-Type: application/json")
    @POST("/api/getspecificrecords")
    fun getUserCars(@Body body : String) : Call<JsonArray>

    /**
     *function to add postings in mongo db
     */
    @Headers("objectName: postings", "Content-Type: application/json")
    @POST("/api/addrecord")
    fun addPosting(@Body body : String) : Call<JsonObject>

    /**
     *function to get user postings from mongo db
     */
    @Headers("objectName: postings", "Content-Type: application/json")
    @POST("/api/getspecificrecords")
    fun getUserPostings(@Body body : String) : Call<JsonArray>

    /**
     *function to update user postings in mongo db
     */
    @Headers("objectName: postings", "Content-Type: application/json")
    @POST("/api/updaterecord")
    fun updatePosting(@Body body : String) : Call<JsonObject>

    /**
     *function to delete user postings in mongo db
     */
    @Headers("objectName: postings", "Content-Type: application/json")
    @POST("/api/delete")
    fun deletePosting(@Body body : String) : Call<JsonObject>
}