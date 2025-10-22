package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository for the SCRUD application. It defines all of the DAOs to interact with the database.
 */
class SCRUDRepository(
    private val studentDao: StudentDao,
    private val courseDao: CourseDao,
    private val subscribeDao: SubscribeDao
) {
    // Students

    /**
     * Returns all students from the table, ordered by last name and first name
     */
    fun getAllStudents(): Flow<List<StudentEntity>> = studentDao.getAllStudents()

    /**
     * Inserts a student into the table
     * @param student The student to insert
     */
    suspend fun insertStudent(student: StudentEntity) = studentDao.insert(student)

    /**
     * Deletes a student from the table
     * @param student The student to delete
     */
    suspend fun deleteStudent(student: StudentEntity) = studentDao.delete(student)

    /**
     * Retrieves a student by their ID
     * @param id The ID of the student to retrieve
     * @return The student with the specified ID, or null if not found
     */
    suspend fun getStudentById(id: Int) = studentDao.getStudentById(id)

    // Courses

    /**
     * Returns all courses from the table
     */
    fun getAllCourses(): Flow<List<CourseEntity>> = courseDao.getAllCourses()

    /**
     * Inserts a course into the table
     * @param course The course to insert
     */
    suspend fun insertCourse(course: CourseEntity) = courseDao.insert(course)

    /**
     * Deletes a course from the table
     * @param course The course to delete
     */
    suspend fun deleteCourse(course: CourseEntity) = courseDao.delete(course)

    /**
     * Retrieves a course by its ID
     * @param id The ID of the course to retrieve
     * @return The course with the specified ID, or null if not found
     */
    suspend fun getCourseById(id: Int) = courseDao.getCourseById(id)

    // Subscribes

    /**
     * Returns all subscriptions from the table
     */
    fun getAllSubscribes(): Flow<List<SubscribeEntity>> = subscribeDao.getAllSubscribes()

    /**
     * Returns subscriptions for a specific student
     * @param sId The ID of the student
     */
    fun getSubscribesByStudent(sId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByStudent(sId)

    /**
     * Returns subscriptions for a specific course
     * @param cId The ID of the course
     */
    fun getSubscribesByCourse(cId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByCourse(cId)

    /**
     * Inserts a subscription into the table
     * @param subscribe The subscription to insert
     */
    suspend fun insertSubscribe(subscribe: SubscribeEntity) = subscribeDao.insert(subscribe)

    /**
     * Deletes a subscription from the table
     * @param subscribe The subscription to delete
     */
    suspend fun deleteSubscribe(subscribe: SubscribeEntity) = subscribeDao.delete(subscribe)
}