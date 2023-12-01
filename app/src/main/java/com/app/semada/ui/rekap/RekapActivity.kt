package com.app.semada.ui.rekap

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.semada.MainActivity
import com.app.semada.databinding.ActivityRekapBinding
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.app.semada.DataStore
import org.json.JSONException

class RekapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRekapBinding

    private val apiUrl = "https://semada-learn.tifc.myhost.id/semada/api/api_rekap.php"

    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRekapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        requestQueue = Volley.newRequestQueue(this)

        val preferenceDataStore = DataStore(this)
        val nis = preferenceDataStore.getNIS()
        val data = preferenceDataStore.getLoggedData()
        binding.rekapKelas.text = data.second

        binding.btnBackRekap.setOnClickListener {
            val intent = Intent(this@RekapActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        fetchData(nis!!)
    }

    private fun fetchData(username: String) {
        val url = "$apiUrl?username=$username"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {

                    val sakitCount = response.getInt("sakit")
                    val izinCount = response.getInt("izin")
                    val alphaCount = response.getInt("alpha")

                    binding.rekapSumSakit.text = sakitCount.toString()
                    binding.rekapSumIzin.text = izinCount.toString()
                    binding.rekapSumAlpha.text = alphaCount.toString()

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Error parsing JSON", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                val errorMessage = "Error: ${error.message}"
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
            }
        )
        requestQueue.add(jsonObjectRequest)
    }
}