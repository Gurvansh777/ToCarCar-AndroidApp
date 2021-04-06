package com.example.tocarcar

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tocarcar.api.LoginRegistrationAPI
import com.example.tocarcar.entity.User
import com.example.tocarcar.utility.Validator
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_registration.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Registration
 */
class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        supportActionBar?.hide()

        btnSaveReg.setOnClickListener {
            val firstName = etFirstNameReg.text.toString().trim()
            val lastName = etLastNameReg.text.toString().trim()
            val email = etEmailReg.text.toString().trim()
            val password = etPasswordReg.text.toString().trim()
            val confirmPassword = etConfirmPasswordReg.text.toString().trim()
            if(validateInputs(firstName, lastName, email, password, confirmPassword)){
                val user = User(firstName, lastName, email, password)
                addUser(user)
            }
        }
    }

    /**
     * Validations function
     */
    private fun validateInputs(firstName: String, lastName: String, email: String, password: String, confirmPassword: String): Boolean {
        var result = true
        if(firstName.isEmpty()){
            etFirstNameReg.error = "Cannot be blank"
            result = false
        }
        if(lastName.isEmpty()){
            etLastNameReg.error = "Cannot be blank"
            result = false
        }
        if(!Validator.isValidEmailAddress(email)){
            etEmailReg.error = "Invalid Email"
            result = false
        }
        if(!Validator.isValidPassword(password)){
            etPasswordReg.error = "Must be more than 6 characters"
            result = false
        }
        if(password != confirmPassword){
            etPasswordReg.error = "Passwords does not match"
            etConfirmPasswordReg.error = "Passwords does not match"
            result = false
        }
        return result
    }

    /**
     * function to add user
     */
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
                    etEmailReg.error = "This email is already registered"
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