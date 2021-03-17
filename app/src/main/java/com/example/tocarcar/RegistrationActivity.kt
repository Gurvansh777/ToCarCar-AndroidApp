package com.example.tocarcar

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tocarcar.api.LoginRegistrationAPI
import com.example.tocarcar.entity.User
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val firstName = etFirstNameReg.text.toString().trim()
        val lastName = etLastNameReg.text.toString().trim()
        val email = etEmailReg.text.toString().trim()
        val password = etPasswordReg.text.toString().trim()
        val confirmPassword = etConfirmPasswordReg.text.toString().trim()

        val user: User = User(firstName, lastName, email, password)

        btnSaveReg.setOnClickListener {
            addUser(user)
        }
    }

    private fun addUser(user: User) {
        progressBarReg.visibility = View.VISIBLE
        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val loginRegistrationAPIService = retroFit.create(LoginRegistrationAPI::class.java)

        val addUser: Call<User> = loginRegistrationAPIService.addUser(user)

        addUser.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Toast.makeText(applicationContext, "User Added! Please login", Toast.LENGTH_LONG)
                    .show()
                progressBarReg.visibility = View.INVISIBLE
                finish()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("USER_ADD_FAILED", t.message.toString())
                Toast.makeText(applicationContext, "Please try again!", Toast.LENGTH_LONG).show()
                progressBarReg.visibility = View.INVISIBLE
                finish()
            }
        })
    }
}