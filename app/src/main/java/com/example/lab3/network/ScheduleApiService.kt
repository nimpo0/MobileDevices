package com.example.lab3.network

import com.example.lab3.data.model.ScheduleResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScheduleApiService {

    private val api: ScheduleApi

    companion object {
        private const val BASE_URL = "https://api.jsonbin.io/v3/"
        private const val SECRET_KEY = "\$2a\$10\$oH.VD27DOsw9CAuthuYfnO7n8Am66YDLzLoT5jXRUaUvTGo/DC09q"
    }

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ScheduleApi::class.java)
    }

    fun getScheduleData(callback: ScheduleCallback) {
        api.getScheduleData(SECRET_KEY).enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(
                call: Call<ScheduleResponse>,
                response: Response<ScheduleResponse>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onFailure()
                }
            }

            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                callback.onFailure()
            }
        })
    }

    interface ScheduleCallback {
        fun onSuccess(scheduleResponse: ScheduleResponse)
        fun onFailure()
    }
}