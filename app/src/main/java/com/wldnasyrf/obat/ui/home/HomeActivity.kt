package com.wldnasyrf.obat.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wldnasyrf.obat.data.response.Obat
import com.wldnasyrf.obat.data.response.ObatResponse
import com.wldnasyrf.obat.data.retrofit.ApiConfig
import com.wldnasyrf.obat.databinding.ActivityHomeBinding
import com.wldnasyrf.obat.ui.create.CreateActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: HomeAdapter  // Inisialisasi adapter sekali

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        getObat()

        binding.fabAddObat.setOnClickListener {
            val intent = Intent(this@HomeActivity, CreateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter()  // Inisialisasi adapter sekali
        binding.rvHome.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = this@HomeActivity.adapter  // Set adapter ke RecyclerView
        }
    }

    private fun getObat() {
        val service = ApiConfig.getApiService()
        service.getAllObat().enqueue(object : Callback<ObatResponse> {
            override fun onResponse(call: Call<ObatResponse>, response: Response<ObatResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setItemData(responseBody.data)
                    }
                } else {
                    Log.e("API_ERROR", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ObatResponse>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }

    private fun setItemData(data: List<Obat>) {
        adapter.submitList(data)  // Update adapter tanpa membuat instance baru
    }
}
