package com.example.lab3.data

import android.util.Log

object RoomCrudTester {

    fun runCrudTests(db: AppDatabase) {
        Log.d("ROOM_DB", "CRUD TESTS START")
        testRead(db)
        testInsert(db)
        testRead(db)
        testUpdate(db)
        testRead(db)
        testDelete(db)
        testRead(db)
        Log.d("ROOM_DB", "CRUD TESTS END")
    }

    fun testInsert(db: AppDatabase) {
        val scheduleDao = db.userScheduleDao()
        val detailsDao = db.appointmentDetailsDao()

        val scheduleId = scheduleDao.insert(
            UserScheduleEntity(
                date = "Fri 9",
                time = "12:00",
                title = "Test event",
                description = "Insert test"
            )
        ).toInt()

        detailsDao.insert(
            AppointmentDetailsEntity(
                scheduleId = scheduleId,
                appointmentType = "Test type",
                name = "Test User",
                phone = "0000000000"
            )
        )

        Log.d("ROOM_DB", "INSERT test completed, scheduleId=$scheduleId")
    }

    fun testRead(db: AppDatabase) {
        val schedules = db.userScheduleDao().getAll()
        val details = db.appointmentDetailsDao().getAll()

        Log.d("ROOM_DB", "--- READ schedules ---")
        for (item in schedules) {
            Log.d("ROOM_DB", "SCHEDULE -> $item")
        }

        Log.d("ROOM_DB", "--- READ details ---")
        for (item in details) {
            Log.d("ROOM_DB", "DETAILS -> $item")
        }
    }

    fun testUpdate(db: AppDatabase) {
        val scheduleDao = db.userScheduleDao()
        val detailsDao = db.appointmentDetailsDao()

        val allSchedules = scheduleDao.getAll()
        if (allSchedules.isNotEmpty()) {
            val first = allSchedules.first()
            val updatedSchedule = first.copy(
                title = "Updated event",
                description = "Update test"
            )
            scheduleDao.update(updatedSchedule)

            val details = detailsDao.getByScheduleId(first.id).firstOrNull()
            if (details != null) {
                val updatedDetails = details.copy(
                    appointmentType = "Updated type",
                    name = "Updated User",
                    phone = "1111111111"
                )
                detailsDao.update(updatedDetails)
            }

            Log.d("ROOM_DB", "UPDATE test completed for scheduleId=${first.id}")
        } else {
            Log.d("ROOM_DB", "UPDATE test skipped: no data")
        }
    }

    fun testDelete(db: AppDatabase) {
        val scheduleDao = db.userScheduleDao()
        val detailsDao = db.appointmentDetailsDao()

        val allSchedules = scheduleDao.getAll()
        if (allSchedules.isNotEmpty()) {
            val last = allSchedules.last()

            val detailsList = detailsDao.getByScheduleId(last.id)
            for (details in detailsList) {
                detailsDao.delete(details)
            }

            scheduleDao.delete(last)
            Log.d("ROOM_DB", "DELETE test completed for scheduleId=${last.id}")
        } else {
            Log.d("ROOM_DB", "DELETE test skipped: no data")
        }
    }
}