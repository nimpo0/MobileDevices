package com.example.lab3.network

import com.example.lab3.data.model.ScheduleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ScheduleApi {
    @GET("b/69becdbcb7ec241ddc8cbe4c/latest?meta=false")
    fun getScheduleData(
        @Header("X-MASTER-KEY") secretKey: String
    ): Call<ScheduleResponse>
}