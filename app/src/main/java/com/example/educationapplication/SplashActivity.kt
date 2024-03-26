package com.example.educationapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val background : ImageView = findViewById(R.id.sa_logo)
        val Animation = AnimationUtils.loadAnimation(this,R.anim.animated)
        background.startAnimation(Animation)

        Handler().postDelayed({
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }, 3000)

    }
}