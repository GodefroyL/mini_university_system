package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tumme.scrudstudents.data.local.Converters

@Entity(
    tableName = "courses",
    foreignKeys = [
        ForeignKey(
            entity = TeacherEntity::class,
            parentColumns = ["teacherId"],
            childColumns = ["teacherId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["teacherId"]),
        Index(value = ["levelCode"])
    ]
)
@TypeConverters(Converters::class)
data class CourseEntity(
    @PrimaryKey(autoGenerate = true) val idCourse: Int = 0,
    val nameCourse: String,
    val ectsCourse: Float,
    val levelCode: String,  // Code du niveau (P1, B1, etc.)
    val teacherId: Int?,    // ID de l'enseignant
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis()  // Timestamp
) {
    companion object {
        fun create(
            nameCourse: String,
            ectsCourse: Float,
            levelCode: LevelCourse,
            teacherId: Int?,
            description: String? = null
        ): CourseEntity {
            return CourseEntity(
                nameCourse = nameCourse,
                ectsCourse = ectsCourse,
                levelCode = levelCode.value,
                teacherId = teacherId,
                description = description
            )
        }
    }

    fun getLevel(): LevelCourse {
        return LevelCourse.from(levelCode)
    }
}
