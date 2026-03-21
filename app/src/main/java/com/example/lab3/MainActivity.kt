package com.example.lab3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.lab3.data.AppDatabase
import com.example.lab3.data.AppointmentDetailsEntity
import com.example.lab3.data.DatabaseSeeder
import com.example.lab3.data.UserScheduleEntity
import com.example.lab3.data.RoomCrudTester

class MainActivity : ComponentActivity() {

    private lateinit var db: AppDatabase

    private var selectedDayText: String = "Tue 6"
    private var selectedTimeText: String = "13:00"

    private var editScheduleId: Int = -1
    private var editDetailsId: Int = -1
    private var isEditMode: Boolean = false

    private var selectedDayView: TextView? = null
    private var selectedTimeView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "lab3_database"
        )
            .allowMainThreadQueries()
            .build()

        DatabaseSeeder.seedDatabaseIfEmpty(db)

        RoomCrudTester.runCrudTests(db)

        editScheduleId = intent.getIntExtra("edit_schedule_id", -1)
        isEditMode = editScheduleId != -1

        setupDaySelection()
        setupTimeSelection()

        if (isEditMode) {
            loadDataForEditing(editScheduleId)
        }

        applyInitialSelection()

        val btnGo = findViewById<Button>(R.id.btnGo)
        btnGo.text = if (isEditMode) "Update event" else "Save event"

        btnGo.setOnClickListener {
            val success = if (isEditMode) {
                updateAppointmentFromForm()
            } else {
                saveAppointmentFromForm()
            }

            if (success) {
                val intent = Intent(this, ScheduleActivity::class.java)
                intent.putExtra("selected_day", extractDayNumber(selectedDayText))
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupDaySelection() {
        val dayScrollContainer = getDayContainer()

        for (i in 0 until dayScrollContainer.childCount) {
            val view = dayScrollContainer.getChildAt(i)
            if (view is TextView) {
                view.setOnClickListener {
                    selectedDayText = view.text.toString().replace("\n", " ")
                    updateDaySelection(view)
                }
            }
        }
    }

    private fun setupTimeSelection() {
        val gridSlots = findViewById<GridLayout>(R.id.gridSlots)

        for (i in 0 until gridSlots.childCount) {
            val view = gridSlots.getChildAt(i)
            if (view is TextView) {
                view.setOnClickListener {
                    selectedTimeText = view.text.toString()
                    updateTimeSelection(view)
                }
            }
        }
    }

    private fun applyInitialSelection() {
        applyDaySelectionFromValue(selectedDayText)
        applyTimeSelectionFromValue(selectedTimeText)
    }

    private fun applyDaySelectionFromValue(dayValue: String) {
        val dayScrollContainer = getDayContainer()

        for (i in 0 until dayScrollContainer.childCount) {
            val view = dayScrollContainer.getChildAt(i)
            if (view is TextView) {
                val textValue = view.text.toString().replace("\n", " ")
                if (textValue == dayValue) {
                    updateDaySelection(view)
                    break
                }
            }
        }
    }

    private fun applyTimeSelectionFromValue(timeValue: String) {
        val gridSlots = findViewById<GridLayout>(R.id.gridSlots)

        for (i in 0 until gridSlots.childCount) {
            val view = gridSlots.getChildAt(i)
            if (view is TextView) {
                if (view.text.toString() == timeValue) {
                    updateTimeSelection(view)
                    break
                }
            }
        }
    }

    private fun updateDaySelection(newSelectedView: TextView) {
        clearAllDaySelections()
        setSelectedChipStyle(newSelectedView)
        selectedDayView = newSelectedView
    }

    private fun updateTimeSelection(newSelectedView: TextView) {
        clearAllTimeSelections()
        setSelectedSlotStyle(newSelectedView)
        selectedTimeView = newSelectedView
    }

    private fun clearAllDaySelections() {
        val dayScrollContainer = getDayContainer()

        for (i in 0 until dayScrollContainer.childCount) {
            val view = dayScrollContainer.getChildAt(i)
            if (view is TextView) {
                resetChipStyle(view)
            }
        }
        selectedDayView = null
    }

    private fun clearAllTimeSelections() {
        val gridSlots = findViewById<GridLayout>(R.id.gridSlots)

        for (i in 0 until gridSlots.childCount) {
            val view = gridSlots.getChildAt(i)
            if (view is TextView) {
                resetSlotStyle(view)
            }
        }
        selectedTimeView = null
    }

    private fun getDayContainer(): LinearLayout {
        val scrollView = findViewById<HorizontalScrollView>(R.id.dayScroll)
        return scrollView.getChildAt(0) as LinearLayout
    }

    private fun resetChipStyle(view: TextView) {
        view.background = ContextCompat.getDrawable(this, R.drawable.bg_chip)
        view.setTextColor(ContextCompat.getColor(this, R.color.text))
    }

    private fun setSelectedChipStyle(view: TextView) {
        view.background = ContextCompat.getDrawable(this, R.drawable.bg_chip_selected)
        view.setTextColor(ContextCompat.getColor(this, R.color.on_selected))
    }

    private fun resetSlotStyle(view: TextView) {
        view.background = ContextCompat.getDrawable(this, R.drawable.bg_chip)
        view.setTextColor(ContextCompat.getColor(this, R.color.text))
    }

    private fun setSelectedSlotStyle(view: TextView) {
        view.background = ContextCompat.getDrawable(this, R.drawable.bg_chip_selected)
        view.setTextColor(ContextCompat.getColor(this, R.color.on_selected))
    }

    private fun saveAppointmentFromForm(): Boolean {
        val name = findViewById<EditText>(R.id.etName).text.toString().trim()
        val phone = findViewById<EditText>(R.id.etPhone).text.toString().trim()
        val appointmentType = findViewById<EditText>(R.id.etAppointmentType).text.toString().trim()

        if (name.isEmpty() || phone.isEmpty() || appointmentType.isEmpty()) {
            Toast.makeText(this, "Enter name, phone and appointment type", Toast.LENGTH_SHORT).show()
            return false
        }

        val scheduleItem = UserScheduleEntity(
            date = selectedDayText,
            time = selectedTimeText,
            title = appointmentType,
            description = "Created from appointment screen"
        )

        val scheduleId = db.userScheduleDao().insert(scheduleItem).toInt()

        val appointmentDetails = AppointmentDetailsEntity(
            scheduleId = scheduleId,
            appointmentType = appointmentType,
            name = name,
            phone = phone
        )

        db.appointmentDetailsDao().insert(appointmentDetails)

        Toast.makeText(this, "Saved to database", Toast.LENGTH_SHORT).show()
        Log.d(
            "ROOM_DB",
            "Appointment saved: $selectedDayText, $selectedTimeText, $appointmentType, $name, $phone"
        )
        return true
    }

    private fun updateAppointmentFromForm(): Boolean {
        val name = findViewById<EditText>(R.id.etName).text.toString().trim()
        val phone = findViewById<EditText>(R.id.etPhone).text.toString().trim()
        val appointmentType = findViewById<EditText>(R.id.etAppointmentType).text.toString().trim()

        if (name.isEmpty() || phone.isEmpty() || appointmentType.isEmpty()) {
            Toast.makeText(this, "Enter name, phone and appointment type", Toast.LENGTH_SHORT).show()
            return false
        }

        val oldSchedule = db.userScheduleDao().getById(editScheduleId)
        if (oldSchedule == null) {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show()
            return false
        }

        val updatedSchedule = oldSchedule.copy(
            date = selectedDayText,
            time = selectedTimeText,
            title = appointmentType,
            description = "Updated from appointment screen"
        )
        db.userScheduleDao().update(updatedSchedule)

        val oldDetails = db.appointmentDetailsDao().getByScheduleId(editScheduleId).firstOrNull()

        if (oldDetails != null) {
            editDetailsId = oldDetails.id

            val updatedDetails = oldDetails.copy(
                appointmentType = appointmentType,
                name = name,
                phone = phone
            )
            db.appointmentDetailsDao().update(updatedDetails)
        } else {
            val newDetails = AppointmentDetailsEntity(
                scheduleId = editScheduleId,
                appointmentType = appointmentType,
                name = name,
                phone = phone
            )
            db.appointmentDetailsDao().insert(newDetails)
        }

        Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show()
        Log.d(
            "ROOM_DB",
            "Appointment updated: $selectedDayText, $selectedTimeText, $appointmentType, $name, $phone"
        )
        return true
    }

    private fun loadDataForEditing(scheduleId: Int) {
        val schedule = db.userScheduleDao().getById(scheduleId)
        val details = db.appointmentDetailsDao().getByScheduleId(scheduleId).firstOrNull()

        if (schedule == null) {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show()
            return
        }

        selectedDayText = schedule.date
        selectedTimeText = schedule.time

        findViewById<EditText>(R.id.etAppointmentType).setText(schedule.title)
        findViewById<EditText>(R.id.etName).setText(details?.name ?: "")
        findViewById<EditText>(R.id.etPhone).setText(details?.phone ?: "")

        if (details != null) {
            editDetailsId = details.id
        }

        Log.d("ROOM_DB", "Loaded for editing: scheduleId=$scheduleId")
    }

    private fun extractDayNumber(dateText: String): Int {
        return Regex("""\d+""").find(dateText)?.value?.toIntOrNull() ?: 14
    }
}