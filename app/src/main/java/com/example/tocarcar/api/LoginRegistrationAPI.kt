package com.example.tocarcar.api

import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginRegistrationAPI {
    @FormUrlEncoded
    @POST("api/checkuser")
    fun verifyUser(@Field("email") email : String, @Field("password") password : String): Call<JsonObject>

    @POST("api/adduser")
    fun addUser(@Body userBody : RequestBody) : Call<JsonObject>
}