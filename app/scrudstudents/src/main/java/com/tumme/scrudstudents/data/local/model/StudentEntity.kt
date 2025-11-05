package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tumme.scrudstudents.data.local.Converters

@Entity(tableName = "students")
@TypeConverters(Converters::class)
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val idStudent: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,  // In a real app, store a hash
    val dateOfBirth: String,
    @field:TypeConverters(Converters::class)
    val gender: Gender,
    val levelCode: String,
    val registrationData: Long = System.currentTimeMillis()  // Changed to match DB schema
)
