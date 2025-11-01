package com.tumme.scrudstudents.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class SubscribeWithCourse(
    @Embedded val subscribe: SubscribeEntity,
    @Relation(
        parentColumn = "courseId",
        entityColumn = "idCourse"
    )
    val course: CourseEntity
)
