package com.app.semada

import android.content.Context
import android.content.SharedPreferences

class DataStore(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    fun saveValue(value: Boolean) {
        sharedPreferences.edit().putBoolean("STATUS", value).apply()
    }
    fun getValue(): Boolean? {
        return sharedPreferences.getBoolean("STATUS", false)
    }
    fun eraseValues() {
        sharedPreferences.edit().remove("STATUS").apply()
    }
}