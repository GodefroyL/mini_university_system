package com.tumme.scrudstudents.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tumme.scrudstudents.data.local.dao.*
import com.tumme.scrudstudents.data.local.model.*

@Database(
    entities = [
        StudentEntity::class,
        TeacherEntity::class,
        CourseEntity::class,
        SubscribeEntity::class,
    ],
    version = 4, // Version incremented
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao
    abstract fun teacherDao(): TeacherDao
    abstract fun courseDao(): CourseDao
    abstract fun subscribeDao(): SubscribeDao
}
