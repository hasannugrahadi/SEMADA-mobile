package com.app.semada.ui.riwayat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.app.semada.DataStore
import org.json.JSONArray

import com.app.semada.MainActivity
import com.app.semada.databinding.ActivityRiwayatBinding

class RiwayatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRiwayatBinding

    private lateinit var adapter: RiwayatAdapter
    private val riwayatList = ArrayList<RiwayatItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewRiwayat.layoutManager = LinearLayoutManager(this)
        adapter = RiwayatAdapter(riwayatList)
        binding.recyclerViewRiwayat.adapter = adapter

        supportActionBar?.hide()
        val preferenceDataStore = DataStore(this)
        val username = preferenceDataStore.getNIS()

        binding.btnBackRiwayat.setOnClickListener {
            val intent = Intent(this@RiwayatActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val apiUrl = "https://semada-learn.tifc.myhost.id/semada/api/api_riwayat.php?username=$username"

        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, apiUrl, null,
            { response ->
                handleApiResponse(response)
            },
            { error ->
                Log.e("Volley Error", "Error: $error")
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    private fun handleApiResponse(response: JSONArray) {
        riwayatList.clear()

        for (i in 0 until response.length()) {
            val jsonArray = response.getJSONArray(i)

            if (jsonArray.length() >= 2) {
                val status = jsonArray.getString(0)
                val tanggal = jsonArray.getString(1)

                riwayatList.add(RiwayatItem(status, tanggal))
            }
        }

        adapter.notifyDataSetChanged()
    }
}

data class RiwayatItem(val status: String, val tanggal: String)



