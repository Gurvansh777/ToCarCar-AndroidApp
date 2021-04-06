package com.example.tocarcar.entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * User Entity
 */
class User(
    @SerializedName("firstName")
    @Expose
    var firstName: String,

    @SerializedName("lastName")
    @Expose
    var lastName: String,

    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("password")
    @Expose
    var password : String


) {
    override fun toString(): String {
        return "User(firstName='$firstName', lastName='$lastName', email='$email', password='$password')"
    }
}