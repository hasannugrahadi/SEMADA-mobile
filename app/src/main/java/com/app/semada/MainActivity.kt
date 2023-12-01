package com.app.semada

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.RequestQueue
import com.app.semada.databinding.ActivityMainBinding
import com.app.semada.ui.presensi.PresensiActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        val preferenceDataStore = DataStore(this)
        val attendDay = preferenceDataStore.getAttendDay()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_presensi, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            if (isCurrentTimeInRange() && isWeekday() && attendDay != todayDay()) {
                if (isWeekday()) {
                    val intent = Intent(this@MainActivity, PresensiActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                    if (attendDay == todayDay()) {
                        Toast.makeText(
                            this@MainActivity,
                            "Kamu sudah mengisi presensi untuk hari ini",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!isWeekday()) {
                        Toast.makeText(
                            this@MainActivity,
                            "Presensi hanya untuk hari masuk saja",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Presensi hanya bisa diisi\npada jam 06:00 - 07:30",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        if (shouldSendApiRequest()) {
            val intent = Intent(this, ApiService::class.java)
            startService(intent)
        }
    }


    private fun isCurrentTimeInRange(): Boolean {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        return currentHour == 6 && currentMinute in 0..29 || currentHour == 7 && currentMinute in 0..29
    }

    private fun todayDay(): String {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val dayNames = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        return dayNames[dayOfWeek - 1]
    }

    private fun isWeekday(): Boolean {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        return dayOfWeek in Calendar.MONDAY..Calendar.FRIDAY
    }

    private fun shouldSendApiRequest(): Boolean {
        if (!isNetworkAvailable()) {
            return false
        }

        val startTime = getStartTimeInMillis()
        val endTime = getEndTimeInMillis()
        val currentTime = System.currentTimeMillis()

        if (currentTime !in startTime..endTime) {
            return false
        }

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            return false
        }

        val preferenceDataStore = DataStore(this)
        val attendDay = preferenceDataStore.getAttendDay()
        if (attendDay == todayDay()){
            return false
        }

        return true
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun getStartTimeInMillis(): Long {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 6)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    private fun getEndTimeInMillis(): Long {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 30)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }


}