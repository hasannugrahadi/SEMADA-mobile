package com.app.semada.splash_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.app.semada.MainActivity
import com.app.semada.R
import com.app.semada.databinding.ActivitySplashScreenBinding
import com.app.semada.login.login

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //menyembunyikan
        supportActionBar?.hide()

        // Ini sementara, bisa diganti berdasarkan kondisi
        Handler(Looper.getMainLooper()).postDelayed({
            // Ini pindah ke MainActivity
            startActivity(Intent(this, login::class.java))
            finish()
        }, 3000) // Delay selama 3 detik
    }
}