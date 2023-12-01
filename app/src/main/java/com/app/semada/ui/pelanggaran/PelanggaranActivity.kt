package com.app.semada.ui.pelanggaran

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.app.semada.DataStore
import com.app.semada.MainActivity
import com.app.semada.databinding.ActivityPelanggaranBinding
import com.app.semada.ui.riwayat.RiwayatAdapter
import com.app.semada.ui.riwayat.RiwayatItem
import org.json.JSONArray

class PelanggaranActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPelanggaranBinding

    private lateinit var adapter: PelanggaranAdapter
    private val pelanggaranList = ArrayList<PelanggaranItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPelanggaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewPelanggaran.layoutManager = LinearLayoutManager(this)
        adapter = PelanggaranAdapter(pelanggaranList)
        binding.recyclerViewPelanggaran.adapter = adapter

        supportActionBar?.hide()
        val preferenceDataStore = DataStore(this)
        val username = preferenceDataStore.getNIS()
        supportActionBar?.hide()

        binding.btnBackPelanggaran.setOnClickListener {
            val intent = Intent(this@PelanggaranActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val apiUrl = "https://semada-learn.tifc.myhost.id/semada/api/api_pelanggaran.php?username=$username"

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
        pelanggaranList.clear()

        for (i in 0 until response.length()) {
            val jsonArray = response.getJSONArray(i)

            if (jsonArray.length() >= 2) {
                val tanggal = jsonArray.getString(0)
                val nama = jsonArray.getString(1)

                pelanggaranList.add(PelanggaranItem(tanggal, nama))
            }
        }

        adapter.notifyDataSetChanged()
    }
}

data class PelanggaranItem(val tanggal: String, val nama: String)

