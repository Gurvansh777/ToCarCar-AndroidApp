package com.example.tocarcar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvNewUser.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val email: String = etEmailLogin.text.toString().trim()
            val password: String = etPasswordLogin.text.toString().trim()
            verifyUser(email, password)
        }
    }

    private fun verifyUser(email: String, password: String) {

    }
}