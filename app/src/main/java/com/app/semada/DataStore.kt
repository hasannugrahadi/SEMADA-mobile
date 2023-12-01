package com.app.semada

import android.content.Context
import android.content.SharedPreferences

class DataStore(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)


    fun saveNIS(nis: String){
        sharedPreferences.edit().putString("nis", nis).apply()
    }
    fun getNIS(): String? {
        return sharedPreferences.getString("nis", null)
    }


    fun saveLoggedData(nama: String, kelas: String ) {
        sharedPreferences.edit().putString("name", nama).apply()
        sharedPreferences.edit().putString("grade", kelas).apply()
    }
    fun getLoggedData(): Pair<String?, String?>{
        val nama = sharedPreferences.getString("name", null)
        val kelas = sharedPreferences.getString("grade", null)
        return Pair(nama, kelas)
    }

    fun saveAttendDay(day: String) {
        sharedPreferences.edit().putString("attend", day).apply()
    }
    fun getAttendDay(): String? {
        return sharedPreferences.getString("attend", null)
    }
    fun eraseAttendDay() {
        sharedPreferences.edit().remove("attend").apply()
    }
}