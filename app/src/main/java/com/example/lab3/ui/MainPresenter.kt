package com.example.lab3.ui

import android.util.Log
import com.example.lab3.data.model.EventItem
import com.example.lab3.data.model.ScheduleResponse
import com.example.lab3.data.source.IDataSource
import com.example.lab3.di.DiHelper

class MainPresenter(
    private val view: MainContract.View
) : MainContract.Presenter {

    private val service: IDataSource = DiHelper.getService()

    override fun loadScheduleData(currentSelectedDay: Int) {
        Log.d("API", "Load data")

        service.getScheduleData(object : IDataSource.ScheduleCallback {
            override fun onSuccess(scheduleResponse: ScheduleResponse) {
                val eventsMap = mapResponseToEvents(scheduleResponse)

                Log.d("API", "Loaded events from server: $eventsMap")

                val finalSelectedDay = if (eventsMap.containsKey(currentSelectedDay)) {
                    currentSelectedDay
                } else {
                    eventsMap.keys.minOrNull() ?: currentSelectedDay
                }

                view.displayScheduleData(eventsMap, finalSelectedDay)
            }

            override fun onFailure() {
                view.displayError()
            }
        })
    }

    private fun mapResponseToEvents(scheduleResponse: ScheduleResponse): Map<Int, List<EventItem>> {
        val schedules = scheduleResponse.userSchedule ?: emptyList()
        val details = scheduleResponse.appointmentDetails ?: emptyList()

        Log.d("API", "Schedules loaded: ${schedules.size}")
        Log.d("API", "Appointment details loaded: ${details.size}")

        val detailsByScheduleId = details.associateBy { it.scheduleId }
        val result = mutableMapOf<Int, MutableList<EventItem>>()

        for (schedule in schedules) {
            val day = extractDayNumber(schedule.date ?: "") ?: continue
            val detail = detailsByScheduleId[schedule.id]

            val subtitle = buildString {
                append(schedule.description ?: "")
                if (detail != null) {
                    append(" | ")
                    append(detail.appointmentType ?: "")
                    append(" | ")
                    append(detail.name ?: "")
                }
            }

            val phoneInfo = if (detail != null) {
                "Phone: ${detail.phone ?: "-"}"
            } else {
                "No phone"
            }

            val event = EventItem(
                scheduleId = schedule.id ?: 0,
                title = schedule.title ?: "No title",
                desc = subtitle,
                time = schedule.time ?: "",
                appointmentType = phoneInfo
            )

            if (!result.containsKey(day)) {
                result[day] = mutableListOf()
            }
            result[day]?.add(event)
        }

        return result
    }

    private fun extractDayNumber(dateText: String): Int? {
        val parts = dateText.split("-")
        return if (parts.size == 3) parts[2].toIntOrNull() else null
    }
}