package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "courses",
    foreignKeys = [
        ForeignKey(
            entity = TeacherEntity::class,
            parentColumns = ["teacherId"],
            childColumns = ["teacherId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class CourseEntity(
    @PrimaryKey(autoGenerate = true) val idCourse: Int = 0,
    val nameCourse: String,
    val ectsCourse: Float,
    val levelCode: String, // Code du niveau (P1, B1, etc.)
    val teacherId: Int? = null, // Clé étrangère
    val description: String? = null
)
