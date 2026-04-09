package com.example.lab3.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab3.data.database.dao.AppointmentDetailsDao
import com.example.lab3.data.database.entity.AppointmentDetailsEntity
import com.example.lab3.data.database.dao.UserScheduleDao
import com.example.lab3.data.database.entity.UserScheduleEntity

@Database(
    entities = [UserScheduleEntity::class, AppointmentDetailsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userScheduleDao(): UserScheduleDao
    abstract fun appointmentDetailsDao(): AppointmentDetailsDao
}