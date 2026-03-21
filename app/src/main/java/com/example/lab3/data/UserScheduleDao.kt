package com.example.lab3.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedule: UserScheduleEntity): Long

    @Query("SELECT * FROM user_schedule")
    fun getAll(): List<UserScheduleEntity>

    @Query("SELECT * FROM user_schedule WHERE id = :id")
    fun getById(id: Int): UserScheduleEntity?

    @Query("SELECT * FROM user_schedule WHERE date = :date")
    fun getByDate(date: String): List<UserScheduleEntity>

    @Update
    fun update(schedule: UserScheduleEntity)

    @Delete
    fun delete(schedule: UserScheduleEntity)

    @Query("DELETE FROM user_schedule")
    fun deleteAll()
}