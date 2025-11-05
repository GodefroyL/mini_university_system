package com.tumme.scrudstudents.data.model

import androidx.room.Embedded
import com.tumme.scrudstudents.data.local.model.StudentEntity

/**
 * A data class that holds the combined information of a student and their grade for a specific course.
 * This is typically constructed by the repository layer.
 */
data class StudentWithGrade(
    @Embedded val student: StudentEntity,
    val score: Float? // The score can be null if the student is not yet graded
)
