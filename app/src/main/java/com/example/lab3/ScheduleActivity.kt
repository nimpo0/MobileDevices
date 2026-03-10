package com.example.lab3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import java.util.GregorianCalendar

class ScheduleActivity : ComponentActivity() {

    private val year = 2023
    private val month = 6

    private val selectedDay = 14

    private val eventsMap: MutableMap<Int, List<EventItem>> = mutableMapOf(
        10 to listOf(
            EventItem(
                "Lorem ipsum",
                "Lorem ipsum dolor sit amet",
                "10:00 - 12:00",
                "21, James Street"
            )
        ),
        14 to listOf(
            EventItem("Meeting", "Project discussion", "13:00 - 14:00", "Office 2nd floor"),
            EventItem("Dentist", "Regular check", "16:30 - 17:00", "Dental Clinic")
        ),
        22 to listOf(
            EventItem(
                "Lorem ipsum",
                "Lorem ipsum dolor sit amet",
                "10:00 - 12:00",
                "21, James Street"
            )
        )
    )

    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val rvCalendar = findViewById<RecyclerView>(R.id.rvCalendar)
        val rvEvents = findViewById<RecyclerView>(R.id.rvEvents)

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        rvCalendar.layoutManager = GridLayoutManager(this, 7)
        calendarAdapter = CalendarAdapter(buildCalendarCells())
        rvCalendar.adapter = calendarAdapter

        rvEvents.layoutManager = LinearLayoutManager(this)
        eventAdapter = EventAdapter(eventsMap[selectedDay] ?: emptyList())
        rvEvents.adapter = eventAdapter
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
                val hasEvents = eventsMap.containsKey(day)
                cells.add(
                    DayCell(
                        dayNumber = day,
                        isSelected = (day == selectedDay),
                        hasEvents = hasEvents
                    )
                )
                day++
            }
        }
        return cells
    }
}