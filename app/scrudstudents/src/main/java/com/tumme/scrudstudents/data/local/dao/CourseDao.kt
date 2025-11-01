package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.model.CourseWithStudents
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for CourseEntity
 * Defines the data access layer for the Course Entity
 */
@Dao
interface CourseDao {
    /** Returns all courses, ordered by name. */
    @Query("SELECT * FROM courses ORDER BY nameCourse")
    fun getAllCourses(): Flow<List<CourseEntity>>

    /** Inserts or updates a course. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(course: CourseEntity)

    /** Deletes a course. */
    @Delete
    suspend fun delete(course: CourseEntity)

    /** Updates a course. */
    @Update
    suspend fun update(course: CourseEntity)

    /** Retrieves a course by its ID. */
    @Query("SELECT * FROM courses WHERE idCourse = :id LIMIT 1")
    suspend fun getCourseById(id: Int): CourseEntity?

    /** Returns courses by level code. */
    @Query("SELECT * FROM courses WHERE levelCode = :levelCode ORDER BY nameCourse")
    fun getCoursesByLevel(levelCode: String): Flow<List<CourseEntity>>

    /** Returns courses by teacher ID. */
    @Query("SELECT * FROM courses WHERE teacherId = :teacherId ORDER BY nameCourse")
    fun getCoursesByTeacher(teacherId: Int): Flow<List<CourseEntity>>

    @Transaction
    @Query("SELECT * FROM courses WHERE teacherId = :teacherId")
    suspend fun getCoursesWithStudents(teacherId: Int): List<CourseWithStudents>
}
