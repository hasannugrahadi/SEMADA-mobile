package com.app.semada

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.text.SimpleDateFormat
import java.util.Date

class ApiService : IntentService("ApiService") {

    private lateinit var requestQueue: RequestQueue
    private val url = "https://semada-learn.tifc.myhost.id/semada/api/api_sendAttend.php"

    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {
        val preferenceDataStore = DataStore(this)
        val nis = preferenceDataStore.getNIS()
        val status = "alpha"

        val imageName = ""
        val keterangan = ""
        val tanggal = getCurrentDate()

        requestQueue = Volley.newRequestQueue(this)

        val params = HashMap<String, String>()
        params["username"] = nis!!
        params["status"] = status
        params["picture_name"] = imageName
        params["text"] = keterangan
        params["date"] = tanggal

        makePostRequest(url, params)

        Log.d("ApiService", "API request completed")
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun makePostRequest(url: String, params: Map<String, String>) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
            },
            Response.ErrorListener { error ->
            }) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }

        requestQueue.add(stringRequest)
    }
}