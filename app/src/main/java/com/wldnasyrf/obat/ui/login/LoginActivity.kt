package com.wldnasyrf.obat.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wldnasyrf.obat.R
import com.wldnasyrf.obat.data.request.LoginRequest
import com.wldnasyrf.obat.data.response.LoginResponse
import com.wldnasyrf.obat.data.retrofit.ApiConfig
import com.wldnasyrf.obat.databinding.ActivityMainBinding
import com.wldnasyrf.obat.ui.home.HomeActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            buttonLogin.setOnClickListener {
                val username = edLoginEmail.text.toString().trim()
                val password = edLoginPassword.text.toString().trim()

                if (username.isEmpty()) {
                    edLoginEmail.error = getString(R.string.validation_username)
                    edLoginEmail.requestFocus()
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    edLoginPassword.error = getString(R.string.validation_password)
                    edLoginPassword.requestFocus()
                    return@setOnClickListener
                }

                if (password.length < 6) {
                    edLoginPassword.error = getString(R.string.validation_password_char)
                    edLoginPassword.requestFocus()
                    return@setOnClickListener
                }
                login(username, password)
            }
        }

    }

    private fun login(username: String, password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().login(LoginRequest(username, password))

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d(TAG, "Login sukses: ${loginResponse?.message}")
                    Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                } else {
                    val errorJson = response.errorBody()?.string()
                    try {
                        val jsonObj = JSONObject(errorJson!!)
                        val errorMessage = jsonObj.getString("message")

                        Log.e(TAG, "Login gagal: $errorMessage")
                        Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.e(TAG, "Login gagal: Tidak bisa parse error message", e)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}