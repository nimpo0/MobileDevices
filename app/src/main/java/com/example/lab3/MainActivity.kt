package com.example.lab3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        val fromSchedule = intent.getBooleanExtra("from_schedule", false)

        findViewById<Button>(R.id.btnGo).setOnClickListener {
            if (fromSchedule) {
                finish()
            } else {
                startActivity(Intent(this, ScheduleActivity::class.java))
            }
        }
    }
}