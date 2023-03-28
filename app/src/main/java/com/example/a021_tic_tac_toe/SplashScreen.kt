package com.example.a021_tic_tac_toe

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView

class SplashScreen : AppCompatActivity() {

    lateinit var splash_screen_logo:ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar!!.hide()

        splash_screen_logo = findViewById(R.id.splash_screen_logo)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            val options: ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this,android.util.Pair(splash_screen_logo,"main_logo"))
            startActivity(intent,options.toBundle())
            finish()
        }, 2000)

    }
}