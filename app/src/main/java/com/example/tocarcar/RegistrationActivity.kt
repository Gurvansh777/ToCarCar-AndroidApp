package com.example.tocarcar

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tocarcar.api.LoginRegistrationAPI
import com.example.tocarcar.entity.User
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_registration.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btnSaveReg.setOnClickListener {
            val firstName = etFirstNameReg.text.toString().trim()
            val lastName = etLastNameReg.text.toString().trim()
            val email = etEmailReg.text.toString().trim()
            val password = etPasswordReg.text.toString().trim()
            val confirmPassword = etConfirmPasswordReg.text.toString().trim()
            if (password == confirmPassword) {
                val user = User(firstName, lastName, email, password)
                addUser(user)
            } else {
                Toast.makeText(applicationContext, "Passwords does not match!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addUser(user: User) {
        progressBarReg.visibility = View.VISIBLE
        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val loginRegistrationAPIService = retroFit.create(LoginRegistrationAPI::class.java)

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("firstName", user.firstName)
            .addFormDataPart("lastName", user.lastName)
            .addFormDataPart("email", user.email)
            .addFormDataPart("password", user.password)
            .build()

        val addUser: Call<JsonObject> = loginRegistrationAPIService.addUser(requestBody)

        addUser.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.body()?.get("useradded").toString().toInt() != 1) {
                    Toast.makeText(applicationContext,
                        "This Email is already registered!",
                        Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(applicationContext,
                        "User Added! Please login",
                        Toast.LENGTH_LONG)
                        .show()
                    finish()
                }
                progressBarReg.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                Log.e("USER_ADD_FAILED", t?.message.toString())
                Toast.makeText(applicationContext, "Please try again!", Toast.LENGTH_LONG).show()
                progressBarReg.visibility = View.INVISIBLE
                finish()
            }
        })
    }
}