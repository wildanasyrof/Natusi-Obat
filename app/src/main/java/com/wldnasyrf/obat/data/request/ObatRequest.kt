package com.wldnasyrf.obat.data.request

data class ObatRequest(
    val nama: String,
    val harga: Int,
    val kategori: String,
    val stok: Int
)