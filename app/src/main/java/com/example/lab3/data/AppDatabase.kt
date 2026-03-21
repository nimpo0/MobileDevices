package com.example.lab3.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserScheduleEntity::class, AppointmentDetailsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userScheduleDao(): UserScheduleDao
    abstract fun appointmentDetailsDao(): AppointmentDetailsDao
}