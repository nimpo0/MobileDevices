package com.example.lab3.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.R
import com.example.lab3.data.model.DayCell
import com.example.lab3.data.model.EventItem
import com.example.lab3.di.DiHelper
import java.util.Calendar
import java.util.GregorianCalendar

class ScheduleActivity : ComponentActivity(), MainContract.View {

    private var year = 2026
    private var month = 3
    private var selectedDay = 25

    private val eventsMap: MutableMap<Int, MutableList<EventItem>> = mutableMapOf()

    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventAdapter: EventAdapter
    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        presenter = DiHelper.getPresenter(this)

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

        presenter.loadScheduleData(selectedDay)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadScheduleData(selectedDay)
    }

    override fun displayScheduleData(eventsMap: Map<Int, List<EventItem>>, selectedDay: Int) {
        this.eventsMap.clear()

        for ((day, events) in eventsMap) {
            this.eventsMap[day] = events.toMutableList()
        }

        this.selectedDay = selectedDay
        refreshUi()
    }

    override fun displayError() {
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