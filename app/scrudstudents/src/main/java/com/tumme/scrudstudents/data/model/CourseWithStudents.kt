package com.tumme.scrudstudents.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity

data class CourseWithStudents(
    @Embedded val course: CourseEntity,
    @Relation(
        parentColumn = "idCourse",
        entityColumn = "idStudent",
        associateBy = Junction(
            value = SubscribeEntity::class,
            parentColumn = "courseId",
            entityColumn = "studentId"
        )
    )
    val students: List<StudentEntity>
)
