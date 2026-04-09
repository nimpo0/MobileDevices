package com.example.lab3.ui

import com.example.lab3.data.model.EventItem

interface MainContract {

    interface View {
        fun displayScheduleData(eventsMap: Map<Int, List<EventItem>>, selectedDay: Int)
        fun displayError()
    }

    interface Presenter {
        fun loadScheduleData(currentSelectedDay: Int)
    }
}