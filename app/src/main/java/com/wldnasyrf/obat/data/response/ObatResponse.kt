package com.wldnasyrf.obat.data.response

import com.google.gson.annotations.SerializedName

data class ObatResponse(

	@field:SerializedName("data")
	val data: List<Obat>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class Obat(

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
