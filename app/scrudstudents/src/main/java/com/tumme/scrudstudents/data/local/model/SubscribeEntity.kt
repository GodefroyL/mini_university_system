package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "subscribes",
    primaryKeys = ["studentId", "courseId"],
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["idStudent"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = ["idCourse"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SubscribeEntity(
    val studentId: Int,
    val courseId: Int,
    val score: Float = 0f,
    val enrollmentDate: Long = System.currentTimeMillis()
)
