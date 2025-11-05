package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.model.CourseWithStudents
import com.tumme.scrudstudents.data.model.CourseWithTeacher
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Query("SELECT * FROM courses ORDER BY nameCourse")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(course: CourseEntity)

    @Update
    suspend fun update(course: CourseEntity)

    @Delete
    suspend fun delete(course: CourseEntity)

    @Query("SELECT * FROM courses WHERE idCourse = :id LIMIT 1")
    suspend fun getCourseById(id: Int): CourseEntity?

    @Query("SELECT * FROM courses WHERE levelCode = :levelCode ORDER BY nameCourse")
    fun getCoursesByLevel(levelCode: String): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE teacherId = :teacherId ORDER BY nameCourse")
    fun getCoursesByTeacher(teacherId: Int): Flow<List<CourseEntity>>

    @Transaction
    @Query("""
        SELECT
            c.*, 
            t.teacherId AS teacher_teacherId, 
            t.firstName AS teacher_firstName, 
            t.lastName AS teacher_lastName, 
            t.email AS teacher_email, 
            t.password AS teacher_password, 
            t.gender AS teacher_gender, 
            t.registrationDate AS teacher_registrationDate
        FROM courses AS c
        LEFT JOIN teachers AS t ON c.teacherId = t.teacherId
        WHERE c.levelCode = :levelCode
    """)
    fun getCoursesWithTeacherByLevel(levelCode: String): Flow<List<CourseWithTeacher>>

    @Transaction
    @Query("SELECT * FROM courses WHERE idCourse = :courseId")
    suspend fun getCourseWithStudents(courseId: Int): CourseWithStudents?
}
