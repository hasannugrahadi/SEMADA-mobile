package com.app.semada.ui.presensi

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.semada.DataStore
import com.app.semada.MainActivity
import com.app.semada.R
import com.app.semada.databinding.ActivitySendpresensiBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class SendingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendpresensiBinding
    private val locationPermissionCode = 1

    private val url = "https://semada-learn.tifc.myhost.id/semada/api/api_sendAttend.php"

    private lateinit var storageReference: StorageReference
    private lateinit var requestQueue: RequestQueue

    private val status = "hadir"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendpresensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnKirimPresensi.isEnabled = false
        binding.btnKirimPresensi.setBackgroundResource(R.drawable.bg_btn_kirim_na)
        binding.btnKirimPresensi.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                locationPermissionCode
            )
        } else {
            startLocationUpdates()
        }

        binding.btnBackSendPresensi.setOnClickListener {
            val intent = Intent(this@SendingActivity, PresensiActivity::class.java)
            startActivity(intent)
            finish()
        }

        val imageUri = intent.getParcelableExtra<Uri>("PHOTO_URI")
        binding.sendPreviewImage.setImageURI(imageUri)

        storageReference = Firebase.storage.reference

        binding.btnKirimPresensi.setOnClickListener {
            uploadData(imageUri!!)
        }
    }

    private fun startLocationUpdates() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val targetLocation = Location("TargetLocation")
                targetLocation.latitude = -8.157605070092234
                targetLocation.longitude = 113.72290734261978

                val distance = location.distanceTo(targetLocation)

                if (distance < 60) {
                    binding.locationStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.presensi_location_circle_ok, 0, 0, 0)
                    binding.locationStatus.text = "Cocok"
                    binding.btnKirimPresensi.isEnabled = true
                    binding.btnKirimPresensi.setBackgroundResource(R.drawable.bg_btn_kirim)
                    binding.btnKirimPresensi.setTextColor(ContextCompat.getColor(this@SendingActivity, android.R.color.white))

                } else {
                    binding.locationStatus.text = "Tidak Cocok"
                    binding.locationStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.presensi_location_circle_wrong, 0, 0, 0)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Request location updates
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode &&
            grantResults.isNotEmpty() &&
            grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        ) {
            startLocationUpdates()
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
        val keterangan = binding.presensiKeterangan.text.toString()
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

        // Make a POST request
        makePostRequest(url, params)

        imageRef.putFile(selectedImageUri)
            .addOnSuccessListener {
                Toast.makeText(this, "Presensi Berhasil Terkirim", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SendingActivity, MainActivity::class.java)
                preferenceDataStore.saveAttendDay(todayDay())
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                // Handle unsuccessful uploads
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