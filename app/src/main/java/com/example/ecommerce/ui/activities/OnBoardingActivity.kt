package com.example.ecommerce.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ecommerce.databinding.ActivityOnboardingBinding
import com.example.ecommerce.ui.fragments.HomeFragment

class OnBoardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        binding.btnGetStarted.setOnClickListener(){
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}