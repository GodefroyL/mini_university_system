package com.tumme.scrudstudents.data.model

import androidx.room.Embedded
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity

data class CourseWithTeacher(
    @Embedded
    val course: CourseEntity,

    @Embedded(prefix = "teacher_") // Add prefix to avoid column ambiguity
    val teacher: TeacherEntity?
)
