package com.app.semada.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.semada.R
import com.app.semada.databinding.ActivityLoginBinding

class login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}