package com.example.tocarcar

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_flash_screen.*

class FlashScreenActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_screen)
        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE)
        progressBarFlashScreen.visibility = View.VISIBLE

        Handler().postDelayed({
            val userEmail = sharedPreferences.getString(Constants.USER_EMAIL, "")
            val invalidSession = userEmail == ""
            val i: Intent = if (invalidSession) {
                Intent(this@FlashScreenActivity, LoginActivity::class.java)
            } else {
                Intent(this@FlashScreenActivity, MainActivity::class.java)
            }
            progressBarFlashScreen.visibility = View.INVISIBLE
            startActivity(i)
            finish()
        }, 3000)
    }
}