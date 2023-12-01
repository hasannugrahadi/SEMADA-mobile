package com.app.semada.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.semada.DataStore
import com.app.semada.databinding.FragmentDashboardBinding
import com.app.semada.ui.ijin.IjinActivity
import com.app.semada.ui.pelanggaran.PelanggaranActivity
import com.app.semada.ui.rekap.RekapActivity
import com.app.semada.ui.riwayat.RiwayatActivity
import com.app.semada.ui.sakit.SakitActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DashboardFragment : Fragment() {

    private val workingHoursMap = mapOf(
        "Senin" to Pair("06:45", "16:00"),
        "Selasa" to Pair("06:45", "16:00"),
        "Rabu" to Pair("06:45", "16:00"),
        "Kamis" to Pair("06:45", "16:00"),
        "Jumat" to Pair("06:45", "11:00")
    )

    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val preferenceDataStore = DataStore(requireContext())
        val attendDay = preferenceDataStore.getAttendDay()
        val data = preferenceDataStore.getLoggedData()
        val nama = data.first?.substringBefore(" ")
        val namaLow = nama?.toLowerCase()?.capitalize()
        binding.firstName.text = namaLow

        val currentDay = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
        val workingHours = workingHoursMap[currentDay]
        if (workingHours != null) {
            binding.workingStart.text = workingHours.first
            binding.workingEnd.text = workingHours.second
        } else {
            binding.workingStart.text = "Libur"
            binding.workingEnd.text = "Libur"
        }

        binding.menuRiwayat.setOnClickListener {
            val intent = Intent(context, RiwayatActivity::class.java)
            context?.startActivity(intent)
        }
        binding.menuIjin.setOnClickListener {
            if (isCurrentTimeInRange() && isWeekday() && attendDay != todayDay()) {
                val intent = Intent(context, IjinActivity::class.java)
                context?.startActivity(intent)
            } else {
                if (attendDay == todayDay()) {
                    Toast.makeText(
                        activity,
                        "Kamu sudah mengisi status kehadiran untuk hari ini",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!isWeekday()) {
                    Toast.makeText(
                        activity,
                        "Surat hanya untuk hari masuk saja",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        "Surat hanya bisa diisi\npada jam 06:00 - 09:30",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.menuSakit.setOnClickListener {
            if (isCurrentTimeInRange() && isWeekday() && attendDay != todayDay()) {
                val intent = Intent(context, SakitActivity::class.java)
                context?.startActivity(intent)
            } else {
                // Show a separate message for each condition
                if (attendDay == todayDay()) {
                    Toast.makeText(
                        activity,
                        "Kamu sudah mengisi status kehadiran untuk hari ini",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!isWeekday()) {
                    Toast.makeText(
                        activity,
                        "Surat hanya untuk hari masuk saja",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        "Surat hanya bisa diisi\npada jam 06:00 - 09:30",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.menuRekap.setOnClickListener {
            val intent = Intent(context, RekapActivity::class.java)
            context?.startActivity(intent)
        }
        binding.menuPelanggaran.setOnClickListener {
            val intent = Intent(context, PelanggaranActivity::class.java)
            context?.startActivity(intent)
        }

        return root

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

    private fun isCurrentTimeInRange(): Boolean {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        return currentHour == 6 && currentMinute in 0..29 || currentHour == 9 && currentMinute in 0..29
    }

}