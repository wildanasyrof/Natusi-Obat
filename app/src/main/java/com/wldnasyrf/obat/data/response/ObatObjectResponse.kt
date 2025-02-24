package com.wldnasyrf.obat.data.response

import com.google.gson.annotations.SerializedName

data class ObatObjectResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("kategori")
	val kategori: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("stok")
	val stok: Int,

	@field:SerializedName("gambar")
	val gambar: String
)
