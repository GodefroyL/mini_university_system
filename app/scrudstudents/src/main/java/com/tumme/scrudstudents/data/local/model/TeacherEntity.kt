package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teachers")
data class TeacherEntity(
    @PrimaryKey(autoGenerate = true) val teacherId: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,  // Dans une application réelle, stockez un hash
    val gender: Gender, // Added gender property
    val registrationDate: Long = System.currentTimeMillis()  // Timestamp
)
