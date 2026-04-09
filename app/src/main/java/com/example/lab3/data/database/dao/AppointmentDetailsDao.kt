package com.example.lab3.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lab3.data.database.entity.AppointmentDetailsEntity

@Dao
interface AppointmentDetailsDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insert(details: AppointmentDetailsEntity): Long

    @Query("SELECT * FROM appointment_details")
    fun getAll(): List<AppointmentDetailsEntity>

    @Query("SELECT * FROM appointment_details WHERE id = :id")
    fun getById(id: Int): AppointmentDetailsEntity?

    @Query("SELECT * FROM appointment_details WHERE schedule_id = :scheduleId")
    fun getByScheduleId(scheduleId: Int): List<AppointmentDetailsEntity>

    @Update
    fun update(details: AppointmentDetailsEntity)

    @Delete
    fun delete(details: AppointmentDetailsEntity)

    @Query("DELETE FROM appointment_details")
    fun deleteAll()
}