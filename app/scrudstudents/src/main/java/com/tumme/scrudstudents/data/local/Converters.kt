package com.tumme.scrudstudents.data.local

import androidx.room.TypeConverter
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.LevelCourse
import java.util.Date

class Converters {
    // Convertisseurs pour Gender
    @TypeConverter
    fun fromGender(gender: Gender): String {
        return gender.name
    }

    @TypeConverter
    fun toGender(value: String): Gender {
        return Gender.valueOf(value)
    }

    // Convertisseurs pour LevelCourse
    @TypeConverter
    fun fromLevelCourse(levelCourse: LevelCourse): String {
        return levelCourse.name
    }

    @TypeConverter
    fun toLevelCourse(value: String): LevelCourse {
        return LevelCourse.valueOf(value)
    }

    // Converts Long to Date and vice-versa for Room
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
