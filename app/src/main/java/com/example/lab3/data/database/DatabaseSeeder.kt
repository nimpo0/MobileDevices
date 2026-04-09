package com.example.lab3.data.database

import com.example.lab3.data.database.entity.AppointmentDetailsEntity
import com.example.lab3.data.database.entity.UserScheduleEntity

object DatabaseSeeder {

    fun seedDatabaseIfEmpty(db: AppDatabase) {
        val scheduleDao = db.userScheduleDao()
        val detailsDao = db.appointmentDetailsDao()

        if (scheduleDao.getAll().isNotEmpty()) return

        val id1 = scheduleDao.insert(
            UserScheduleEntity(
                date = "Tue 6",
                time = "10:00",
                title = "Math consultation",
                description = "Preparation for test"
            )
        ).toInt()
        detailsDao.insert(
            AppointmentDetailsEntity(
                scheduleId = id1,
                appointmentType = "Study",
                name = "Kate",
                phone = "0991112233"
            )
        )

        val id2 = scheduleDao.insert(
            UserScheduleEntity(
                date = "Tue 6",
                time = "13:00",
                title = "Dentist appointment",
                description = "Teeth check and cleaning"
            )
        ).toInt()
        detailsDao.insert(
            AppointmentDetailsEntity(
                scheduleId = id2,
                appointmentType = "Medical",
                name = "Kate",
                phone = "0992223344"
            )
        )

        val id3 = scheduleDao.insert(
            UserScheduleEntity(
                date = "Wed 7",
                time = "09:00",
                title = "English practice",
                description = "Speaking lesson"
            )
        ).toInt()
        detailsDao.insert(
            AppointmentDetailsEntity(
                scheduleId = id3,
                appointmentType = "Study",
                name = "Kate",
                phone = "0993334455"
            )
        )

        val id4 = scheduleDao.insert(
            UserScheduleEntity(
                date = "Thu 8",
                time = "15:00",
                title = "Doctor visit",
                description = "Routine check"
            )
        ).toInt()
        detailsDao.insert(
            AppointmentDetailsEntity(
                scheduleId = id4,
                appointmentType = "Medical",
                name = "Kate",
                phone = "0994445566"
            )
        )

        val id5 = scheduleDao.insert(
            UserScheduleEntity(
                date = "Fri 9",
                time = "11:00",
                title = "Project meeting",
                description = "Discuss app screens and database"
            )
        ).toInt()
        detailsDao.insert(
            AppointmentDetailsEntity(
                scheduleId = id5,
                appointmentType = "Work",
                name = "Kate",
                phone = "0995556677"
            )
        )

        val id6 = scheduleDao.insert(
            UserScheduleEntity(
                date = "Fri 9",
                time = "17:00",
                title = "Gym session",
                description = "Cardio and stretching"
            )
        ).toInt()
        detailsDao.insert(
            AppointmentDetailsEntity(
                scheduleId = id6,
                appointmentType = "Personal",
                name = "Kate",
                phone = "0996667788"
            )
        )

        val id7 = scheduleDao.insert(
            UserScheduleEntity(
                date = "Mon 12",
                time = "08:30",
                title = "Hair appointment",
                description = "Cut and styling"
            )
        ).toInt()
        detailsDao.insert(
            AppointmentDetailsEntity(
                scheduleId = id7,
                appointmentType = "Beauty",
                name = "Kate",
                phone = "0997778899"
            )
        )

        val id8 = scheduleDao.insert(
            UserScheduleEntity(
                date = "Tue 13",
                time = "14:00",
                title = "Therapy session",
                description = "Online консультація"
            )
        ).toInt()
        detailsDao.insert(
            AppointmentDetailsEntity(
                scheduleId = id8,
                appointmentType = "Medical",
                name = "Kate",
                phone = "0998889900"
            )
        )

        val id9 = scheduleDao.insert(
            UserScheduleEntity(
                date = "Wed 14",
                time = "16:00",
                title = "Team presentation",
                description = "Present laboratory progress"
            )
        ).toInt()
        detailsDao.insert(
            AppointmentDetailsEntity(
                scheduleId = id9,
                appointmentType = "Study",
                name = "Kate",
                phone = "0971234567"
            )
        )

        val id10 = scheduleDao.insert(
            UserScheduleEntity(
                date = "Fri 16",
                time = "12:00",
                title = "Lunch with friend",
                description = "Cafe meeting in city center"
            )
        ).toInt()
        detailsDao.insert(
            AppointmentDetailsEntity(
                scheduleId = id10,
                appointmentType = "Personal",
                name = "Kate",
                phone = "0977654321"
            )
        )
    }
}