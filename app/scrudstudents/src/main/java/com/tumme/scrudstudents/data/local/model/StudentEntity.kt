package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Room Entity representing a student in the local database.
 * Mapped to the "students" table.
 */
@Entity(tableName = "students")
data class StudentEntity(

    /** Unique identifier for the student (primary key). */
    @PrimaryKey val idStudent: Int,

    /** Last name of the student. */
    val lastName: String,

    /** First name of the student. */
    val firstName: String,

    /** Date of birth of the student. */
    val dateOfBirth: Date,

    /** Gender of the student (enum: MALE, FEMALE, OTHER, etc.). */
    val gender: Gender
)
