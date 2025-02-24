package com.wldnasyrf.obat.data.retrofit

import com.wldnasyrf.obat.data.request.LoginRequest
import com.wldnasyrf.obat.data.request.ObatRequest
import com.wldnasyrf.obat.data.response.DeleteResponse
import com.wldnasyrf.obat.data.response.LoginResponse
import com.wldnasyrf.obat.data.response.Obat
import com.wldnasyrf.obat.data.response.ObatObjectResponse
import com.wldnasyrf.obat.data.response.ObatResponse
import retrofit2.http.Body
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("admin/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("obats")
    fun getAllObat(): Call<ObatResponse>

    @GET("obats/{id}")
    fun getObatById(@Path("id") id: Int): Call<ObatObjectResponse>

    @POST("obats")
    fun createObat(@Body obat: ObatRequest): Call<ObatObjectResponse>

    @PUT("obats/{id}")
    fun updateObat(@Path("id") id: Int, @Body obat: ObatRequest): Call<ObatObjectResponse>

    @DELETE("obats/{id}")
    fun deleteObat(@Path("id") id: Int): Call<DeleteResponse>
}