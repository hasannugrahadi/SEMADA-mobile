package com.app.semada.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.semada.MainActivity
import com.app.semada.databinding.ActivityTentangappBinding

class TentangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTentangappBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTentangappBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnBackTentangapp.setOnClickListener {
            val intent = Intent(this@TentangActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}