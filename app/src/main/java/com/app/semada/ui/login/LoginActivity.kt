package com.app.semada.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.semada.DataStore
import com.app.semada.databinding.ActivityLoginBinding
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.semada.MainActivity
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val serverUrl = "https://semada-learn.tifc.myhost.id/semada/api/api_login.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val preferenceDataStore = DataStore(this)

        binding.teksPassword.transformationMethod = AsteriskPasswordTransformationMethod()

        binding.btnLogin.setOnClickListener {
            val nis = binding.teksNis.text.toString()
            val password = binding.teksPassword.text.toString()
                val stringRequest = object : StringRequest(
                    Method.POST,
                    serverUrl,
                    Response.Listener { response ->
                        val jsonResponse = JSONObject(response)
                        when (jsonResponse.getString("status")) {
                            "success" -> {
                                // Login successful
                                Toast.makeText(
                                    this,
                                    jsonResponse.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                                val fetchedName = jsonResponse.getString("nama")
                                val fetchedClass = jsonResponse.getString("kelas")

                                preferenceDataStore.saveNIS(nis)
                                preferenceDataStore.saveLoggedData(fetchedName, fetchedClass)

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            "failure" -> {
                                Toast.makeText(
                                    this,
                                    jsonResponse.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    Response.ErrorListener {error->
                        Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params["username"] = nis
                        params["password"] = password
                        return params
                    }
                }
                // Add the request to the RequestQueue
                Volley.newRequestQueue(this).add(stringRequest)
        }
    }
}

class AsteriskPasswordTransformationMethod : PasswordTransformationMethod() {

    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }

    inner class PasswordCharSequence (private val source: CharSequence) : CharSequence {

        override val length: Int
            get() = source.length

        override fun get(index: Int): Char = '*'

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return source.subSequence(startIndex, endIndex)
        }
    }
}