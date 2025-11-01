package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ColumnInfo

@Entity(
    tableName = "subscribes",
    primaryKeys = ["studentId", "courseId"],
    foreignKeys = [
        ForeignKey(entity = StudentEntity::class, parentColumns = ["idStudent"], childColumns = ["studentId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = CourseEntity::class, parentColumns = ["idCourse"], childColumns = ["courseId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("studentId"), Index("courseId")]
)
data class SubscribeEntity(
    @ColumnInfo(name = "studentId") val studentId: Int,
    @ColumnInfo(name = "courseId") val courseId: Int,
    val score: Float,
    val enrollmentDate: Long = System.currentTimeMillis()
)