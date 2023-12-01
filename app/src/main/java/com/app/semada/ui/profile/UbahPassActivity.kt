package com.app.semada.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.semada.DataStore
import com.app.semada.MainActivity
import com.app.semada.databinding.ActivityUbahpassBinding
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class UbahPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUbahpassBinding

    private val url = "https://semada-learn.tifc.myhost.id/semada/api/api_ubahpass.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbahpassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnBackUbahpass.setOnClickListener {
            val intent = Intent(this@UbahPassActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnKirimUbahpass.setOnClickListener {
            val preferenceDataStore = DataStore(this)
            val nis = preferenceDataStore.getNIS()
            val oldpass = binding.ubahpassPasslama.text.toString()
            val newpass = binding.ubahpassPassbaru.text.toString()

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    Log.d("Response", response)
                    Toast.makeText(
                        this,
                        "Password berhasil diubah",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@UbahPassActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                Response.ErrorListener { error ->
                    Log.e("Error", "Error occurred: $error")
                }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["userID"] = nis!!
                    params["oldPassword"] = oldpass
                    params["newPassword"] = newpass
                    return params
                }
            }

            Volley.newRequestQueue(this).add(stringRequest)
        }
    }
}