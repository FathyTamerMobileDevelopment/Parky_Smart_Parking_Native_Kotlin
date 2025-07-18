package com.example.parkyapplication.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.parkyapplication.R

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var logoImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Hide the action bar for splash screen
        supportActionBar?.hide()

        // Initialize UI elements
        logoImage = findViewById(R.id.ivLogo)
        logoImage.alpha = 0f
        // Start the animations
        startAnimations()
        endSplashScreen()
    }

    private fun endSplashScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, StartScreenActivity::class.java)
            startActivity(intent)
            finish() // Close SplashActivity
        }, 2000) // Delay in milliseconds (2 seconds)
    }

    private fun startAnimations() {
        // Logo animation - zoom in with bounce
        ViewCompat.animate(logoImage)
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(300)
            .setDuration(700)
            .setInterpolator(OvershootInterpolator(1.2f))
            .start()
    }


}


