package com.example.tocarcar

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tocarcar.api.LoginRegistrationAPI
import com.example.tocarcar.utility.Validator
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Login class
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        sharedPreferences = applicationContext.getSharedPreferences(Constants.MY_PREFERENCES,
            MODE_PRIVATE)

        tvNewUser.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val email: String = etEmailLogin.text.toString().trim()
            val password: String = etPasswordLogin.text.toString().trim()
            if(validateInputs(email, password)){
                progressBarLogin.visibility = View.VISIBLE
                verifyUser(email, password)
            }
        }
    }

    /**
     * function to validations
     */
    private fun validateInputs(email: String, password: String) : Boolean {
        var result = true
        if(!Validator.isValidEmailAddress(email)){
            etEmailLogin.error = "Invalid Email"
            result = false
        }
        if(!Validator.isValidPassword(password)){
            etPasswordLogin.error = "Password must be more than 6 characters"
            result = false
        }
        return result
    }
    /**
     * function to verify user api
     */
    private fun verifyUser(email: String, password: String) {
        val retroFit = Retrofit.Builder().baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val loginRegistrationAPIService = retroFit.create(LoginRegistrationAPI::class.java)

        val checkUser: Call<JsonObject> = loginRegistrationAPIService.verifyUser(email, password)
        checkUser.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.i("LOGIN_RESPONSE", response.body().toString())
                if (response.body()?.get("uservalid").toString().toInt() == 1) {
                    if(response.body()?.get("isApproved").toString().toInt() == 1) {
                        val spEditor: SharedPreferences.Editor = sharedPreferences.edit()
                        spEditor.putString(Constants.USER_EMAIL, email)
                        spEditor.putString(Constants.USER_FIRST_NAME,
                            response.body()?.get("firstName").toString())
                        spEditor.putString(Constants.USER_LAST_NAME,
                            response.body()?.get("lastName").toString())
                        spEditor.apply()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(applicationContext, "Please wait while we approve your account!", Toast.LENGTH_SHORT).show()
                    }
                    progressBarLogin.visibility = View.INVISIBLE
                }else{
                    Toast.makeText(applicationContext, "Incorrect email or password!", Toast.LENGTH_SHORT).show()
                    progressBarLogin.visibility = View.INVISIBLE
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Login failed!", Toast.LENGTH_SHORT).show()
                Log.e("LOGIN_FAILED", t.toString())
                progressBarLogin.visibility = View.INVISIBLE
            }
        })
    }
}