package com.example.tocarcar.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginRegistrationAPI {

    @FormUrlEncoded
    @POST("checkuser")
    fun verifyUser(@Field("email") email : String, @Field("password") password : String): Call<JsonObject>
}