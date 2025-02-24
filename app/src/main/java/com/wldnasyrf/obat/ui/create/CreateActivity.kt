package com.wldnasyrf.obat.ui.create

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wldnasyrf.obat.data.request.ObatRequest
import com.wldnasyrf.obat.data.response.DeleteResponse
import com.wldnasyrf.obat.data.response.ObatObjectResponse
import com.wldnasyrf.obat.data.retrofit.ApiConfig
import com.wldnasyrf.obat.databinding.ActivityCreateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private var obatId: Int? = null
    private var isUpdateMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cek apakah sedang dalam mode Update
        obatId = intent.getIntExtra("EXTRA_OBAT_ID", -1)
        isUpdateMode = obatId != -1

        if (isUpdateMode) {
            fetchObatData(obatId!!)
            binding.btnSave.text = "Update"
            binding.btnDelete.visibility = View.VISIBLE
        } else {
            binding.btnDelete.visibility = View.GONE
        }

        binding.btnSave.setOnClickListener { saveOrUpdateObat() }
        binding.btnDelete.setOnClickListener { deleteObat() }
    }

    private fun fetchObatData(obatId: Int) {
        ApiConfig.getApiService().getObatById(obatId).enqueue(object : Callback<ObatObjectResponse> {
            override fun onResponse(call: Call<ObatObjectResponse>, response: Response<ObatObjectResponse>) {
                if (response.isSuccessful) {
                    val obat = response.body()?.data
                    obat?.let {
                        binding.etNamaObat.setText(it.nama)
                        binding.etHargaObat.setText(it.harga.toString())
                        binding.etKategoriObat.setText(it.kategori)
                        binding.etStockObat.setText(it.stok.toString())
                    }
                }
            }

            override fun onFailure(call: Call<ObatObjectResponse>, t: Throwable) {
                Toast.makeText(this@CreateActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveOrUpdateObat() {
        val nama = binding.etNamaObat.text.toString()
        val harga = binding.etHargaObat.text.toString().toIntOrNull() ?: 0
        val kategori = binding.etKategoriObat.text.toString()
        val stok = binding.etStockObat.text.toString().toIntOrNull() ?: 0

        if (nama.isEmpty() || kategori.isEmpty() || stok <= 0) {
            Toast.makeText(this, "Harap isi semua data dengan benar!", Toast.LENGTH_SHORT).show()
            return
        }

        val obatRequest = ObatRequest(nama, harga, kategori, stok)

        if (isUpdateMode) {
            updateObat(obatId!!, obatRequest)
        } else {
            createObat(obatRequest)
        }
    }

    private fun createObat(obatRequest: ObatRequest) {
        ApiConfig.getApiService().createObat(obatRequest).enqueue(object : Callback<ObatObjectResponse> {
            override fun onResponse(call: Call<ObatObjectResponse>, response: Response<ObatObjectResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateActivity, "Obat berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CreateActivity, "Gagal menambahkan obat!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ObatObjectResponse>, t: Throwable) {
                Toast.makeText(this@CreateActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateObat(obatId: Int, obatRequest: ObatRequest) {
        ApiConfig.getApiService().updateObat(obatId, obatRequest).enqueue(object : Callback<ObatObjectResponse> {
            override fun onResponse(call: Call<ObatObjectResponse>, response: Response<ObatObjectResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateActivity, "Obat berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CreateActivity, "Gagal memperbarui obat!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ObatObjectResponse>, t: Throwable) {
                Toast.makeText(this@CreateActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteObat() {
        ApiConfig.getApiService().deleteObat(obatId!!).enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateActivity, "Obat berhasil dihapus!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CreateActivity, "Gagal menghapus obat!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Toast.makeText(this@CreateActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
