package com.example.lab3.data.source

import com.example.lab3.data.model.ScheduleResponse

interface IDataSource {

    fun getScheduleData(callback: ScheduleCallback)

    interface ScheduleCallback {
        fun onSuccess(scheduleResponse: ScheduleResponse)
        fun onFailure()
    }
}