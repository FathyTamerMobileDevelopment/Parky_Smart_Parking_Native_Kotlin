package com.example.parkyapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.parkyapplication.R
import com.example.parkyapplication.databinding.ActivityStartScreenBinding
import com.example.parkyapplication.ui.login.LoginScreenActivity
import com.example.parkyapplication.ui.onboarding.OnboardingActivity


class StartScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        onClickListener()

    
    }

    private fun onClickListener() {
        binding.btnStartScreen.setOnClickListener {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        }
        binding.llSignin.setOnClickListener {
        val intent = Intent(this, LoginScreenActivity::class.java)
        startActivity(intent)
        }
    }
}