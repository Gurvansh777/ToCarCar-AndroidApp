package com.example.tocarcar.entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("firstName")
    @Expose
    private val firstName: String,

    @SerializedName("lastName")
    @Expose
    private val lastName: String,

    @SerializedName("email")
    @Expose
    private val email: String,

    @SerializedName("password")
    @Expose
    private val password : String
)