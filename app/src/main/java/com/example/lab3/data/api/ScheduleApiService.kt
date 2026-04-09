package com.example.lab3.data.api

import com.example.lab3.data.model.ScheduleResponse
import com.example.lab3.data.source.IDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleApiService(
    private val api: ScheduleApi
) : IDataSource {

    private val SECRET_KEY = "\$2a\$10\$oH.VD27DOsw9CAuthuYfnO7n8Am66YDLzLoT5jXRUaUvTGo/DC09q"

    override fun getScheduleData(callback: IDataSource.ScheduleCallback) {
        api.getScheduleData(SECRET_KEY).enqueue(object : Callback<ScheduleResponse> {

            override fun onResponse(
                call: Call<ScheduleResponse>,
                response: Response<ScheduleResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
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
}