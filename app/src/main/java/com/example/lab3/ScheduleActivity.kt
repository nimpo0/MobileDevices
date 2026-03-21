package com.example.lab3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.data.model.ScheduleResponse
import com.example.lab3.network.ScheduleApiService
import java.util.Calendar
import java.util.GregorianCalendar

class ScheduleActivity : ComponentActivity() {

    private var year = 2026
    private var month = 3

    private var selectedDay = 25

    private val eventsMap: MutableMap<Int, MutableList<EventItem>> = mutableMapOf()

    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        selectedDay = intent.getIntExtra("selected_day", 25)

        val rvCalendar = findViewById<RecyclerView>(R.id.rvCalendar)
        val rvEvents = findViewById<RecyclerView>(R.id.rvEvents)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        btnAdd.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("selected_day", selectedDay)
            startActivity(intent)
        }

        rvCalendar.layoutManager = GridLayoutManager(this, 7)
        rvEvents.layoutManager = LinearLayoutManager(this)

        calendarAdapter = CalendarAdapter(buildCalendarCells()) { clickedDay ->
            selectedDay = clickedDay
            refreshUi()
        }
        rvCalendar.adapter = calendarAdapter

        eventAdapter = EventAdapter(
            items = eventsMap[selectedDay] ?: emptyList(),
            onEditClick = { event ->
                openEditScreen(event)
            },
            onDeleteClick = { event ->
                deleteEvent(event)
            }
        )
        rvEvents.adapter = eventAdapter

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        Log.d("API", "loadData")

        val service = ScheduleApiService()
        service.getScheduleData(object : ScheduleApiService.ScheduleCallback {
            override fun onSuccess(scheduleResponse: ScheduleResponse) {
                runOnUiThread {
                    displayScheduleData(scheduleResponse)
                }
            }

            override fun onFailure() {
                runOnUiThread {
                    displayError()
                }
            }
        })
    }

    private fun displayScheduleData(scheduleResponse: ScheduleResponse) {
        val schedules = scheduleResponse.userSchedule ?: emptyList()
        val details = scheduleResponse.appointmentDetails ?: emptyList()

        Log.d("API", "Schedules loaded: ${schedules.size}")
        Log.d("API", "Appointment details loaded: ${details.size}")

        val detailsByScheduleId = details.associateBy { it.scheduleId }

        eventsMap.clear()

        for (schedule in schedules) {
            val day = extractDayNumber(schedule.date) ?: continue
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

            val currentList = eventsMap[day] ?: mutableListOf()
            currentList.add(event)
            eventsMap[day] = currentList
        }

        Log.d("API", "Loaded events from server: $eventsMap")

        if (!eventsMap.containsKey(selectedDay)) {
            selectedDay = eventsMap.keys.minOrNull() ?: selectedDay
        }

        refreshUi()
    }

    private fun displayError() {
        Log.d("API", "error loading data")
        Toast.makeText(this, "Failed to load data", Toast.LENGTH_LONG).show()
    }

    private fun refreshUi() {
        calendarAdapter.update(buildCalendarCells())
        eventAdapter.update(eventsMap[selectedDay] ?: emptyList())
    }

    private fun deleteEvent(event: EventItem) {
        val dayEvents = eventsMap[selectedDay]
        dayEvents?.removeAll { it.scheduleId == event.scheduleId }

        if (dayEvents.isNullOrEmpty()) {
            eventsMap.remove(selectedDay)
        }

        refreshUi()
        Toast.makeText(this, "Event deleted from screen", Toast.LENGTH_SHORT).show()
    }

    private fun openEditScreen(event: EventItem) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("edit_schedule_id", event.scheduleId)
        intent.putExtra("selected_day", selectedDay)
        startActivity(intent)
    }

    private fun extractDayNumber(dateText: String): Int? {
        val parts = dateText.split("-")
        return if (parts.size == 3) parts[2].toIntOrNull() else null
    }

    private fun buildCalendarCells(): List<DayCell> {
        val cal = GregorianCalendar(year, month - 1, 1)

        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        val offset = when (firstDayOfWeek) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> 0
        }

        val cells = mutableListOf<DayCell>()
        var day = 1

        for (i in 0 until 42) {
            if (i < offset || day > daysInMonth) {
                cells.add(DayCell(null, isSelected = false, hasEvents = false))
            } else {
                cells.add(
                    DayCell(
                        dayNumber = day,
                        isSelected = (day == selectedDay),
                        hasEvents = eventsMap.containsKey(day)
                    )
                )
                day++
            }
        }

        return cells
    }
}