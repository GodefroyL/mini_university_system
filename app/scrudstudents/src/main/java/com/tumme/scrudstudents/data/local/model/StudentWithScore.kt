package com.tumme.scrudstudents.data.local.model

/**
 * Data class to hold the result of a query joining a student with their score for a specific course.
 */
data class StudentWithScore(
    val idStudent: Int,
    val firstName: String,
    val lastName: String,
    val score: Float
)
