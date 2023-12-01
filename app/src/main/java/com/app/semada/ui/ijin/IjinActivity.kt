package com.app.semada.ui.ijin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.semada.DataStore
import com.app.semada.MainActivity
import com.app.semada.R
import com.app.semada.databinding.ActivityIjinBinding
import com.app.semada.databinding.ActivitySakitBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class IjinActivity : AppCompatActivity() {

    private val url = "https://semada-learn.tifc.myhost.id/semada/api/api_sendAttend.php"

    private lateinit var binding: ActivityIjinBinding
    private lateinit var storageReference: StorageReference
    private lateinit var requestQueue: RequestQueue

    private val PICK_IMAGE_REQUEST = 1

    private val status = "izin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIjinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val myButton: Button = findViewById(R.id.btn_kirim_suratIzin)

        myButton.isEnabled = false
        myButton.setBackgroundResource(R.drawable.bg_btn_kirim_na)
        myButton.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))

        binding.btnBackIjin.setOnClickListener {
            val intent = Intent(this@IjinActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSuratIzin.setOnClickListener {
            browseImage()
        }

        storageReference = Firebase.storage.reference
    }

    private fun browseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri = data.data!!
            val myButton: Button = findViewById(R.id.btn_kirim_suratIzin)
            myButton.isEnabled = true
            myButton.setBackgroundResource(R.drawable.bg_btn_kirim)
            myButton.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            binding.ijinFileIndicator.setImageResource(R.drawable.riwayat_hadir)
            binding.btnKirimSuratIzin.setOnClickListener {
                uploadData(selectedImageUri)
            }
        }
    }
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun uploadData(selectedImageUri: Uri) {

        val preferenceDataStore = DataStore(this)
        val nis = preferenceDataStore.getNIS()
        val keterangan = binding.ijinKeterangan.text.toString()
        val tanggal = getCurrentDate()

        val timestamp = System.currentTimeMillis()
        val imageName = "image_$timestamp.jpg"
        val imageRef = storageReference.child("images/$imageName")

        requestQueue = Volley.newRequestQueue(this)

        val params = HashMap<String, String>()
        params["username"] = nis!!
        params["status"] = status
        params["picture_name"] = imageName
        params["text"] = keterangan
        params["date"] = tanggal

        makePostRequest(url, params)

        imageRef.putFile(selectedImageUri)
            .addOnSuccessListener {
                Toast.makeText(this, "Data Berhasil Terkirim", Toast.LENGTH_SHORT).show()
                preferenceDataStore.saveAttendDay(todayDay())
                val intent = Intent(this@IjinActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Data gagal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
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

    private fun todayDay(): String {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val dayNames = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        return dayNames[dayOfWeek - 1]
    }
}