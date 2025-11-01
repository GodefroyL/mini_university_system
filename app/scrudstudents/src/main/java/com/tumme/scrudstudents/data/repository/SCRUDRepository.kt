package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.model.CourseWithStudents
import com.tumme.scrudstudents.data.local.model.SubscribeWithCourse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Repository for the SCRUD application.
 * It defines all of the DAOs to interact with the database and provides
 * a single source of truth for data operations.
 */
class SCRUDRepository(
    private val studentDao: StudentDao,
    private val teacherDao: TeacherDao,
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
     * Updates a student in the table
     * @param student The student to update
     */
    suspend fun updateStudent(student: StudentEntity) = studentDao.update(student)

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

    /**
     * Authenticates a student
     * @param email The student's email
     * @param password The student's password
     * @return The authenticated student or null if authentication failed
     */
    suspend fun authenticateStudent(email: String, password: String) = studentDao.authenticate(email, password)

    // Teachers
    /**
     * Returns all teachers from the table, ordered by last name and first name
     */
    fun getAllTeachers(): Flow<List<TeacherEntity>> = teacherDao.getAllTeachers()

    /**
     * Inserts a teacher into the table
     * @param teacher The teacher to insert
     */
    suspend fun insertTeacher(teacher: TeacherEntity) = teacherDao.insert(teacher)

    /**
     * Updates a teacher in the table
     * @param teacher The teacher to update
     */
    suspend fun updateTeacher(teacher: TeacherEntity) = teacherDao.update(teacher)

    /**
     * Deletes a teacher from the table
     * @param teacher The teacher to delete
     */
    suspend fun deleteTeacher(teacher: TeacherEntity) = teacherDao.delete(teacher)

    /**
     * Retrieves a teacher by their ID
     * @param id The ID of the teacher to retrieve
     * @return The teacher with the specified ID, or null if not found
     */
    suspend fun getTeacherById(id: Int) = teacherDao.getTeacherById(id)

    /**
     * Authenticates a teacher
     * @param email The teacher's email
     * @param password The teacher's password
     * @return The authenticated teacher or null if authentication failed
     */
    suspend fun authenticateTeacher(email: String, password: String) = teacherDao.authenticate(email, password)

    // Courses
    /**
     * Returns all courses from the table
     */
    fun getAllCourses(): Flow<List<CourseEntity>> = courseDao.getAllCourses()

    /**
     * Returns courses by level code
     * @param levelCode The level code to filter by
     */
    fun getCoursesByLevel(levelCode: String): Flow<List<CourseEntity>> = courseDao.getCoursesByLevel(levelCode)

    /**
     * Returns courses by teacher ID
     * @param teacherId The teacher ID to filter by
     */
    fun getCoursesByTeacher(teacherId: Int): Flow<List<CourseEntity>> = courseDao.getCoursesByTeacher(teacherId)

    /**
     * Inserts a course into the table
     * @param course The course to insert
     */
    suspend fun insertCourse(course: CourseEntity) = courseDao.insert(course)

    /**
     * Updates a course in the table
     * @param course The course to update
     */
    suspend fun updateCourse(course: CourseEntity) = courseDao.update(course)

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

    suspend fun getCoursesWithStudents(teacherId: Int): List<CourseWithStudents> = courseDao.getCoursesWithStudents(teacherId)


    // Subscribes
    /**
     * Returns all subscriptions from the table
     */
    fun getAllSubscribes(): Flow<List<SubscribeEntity>> = subscribeDao.getAllSubscribes()

    /**
     * Returns subscriptions for a specific student
     * @param studentId The ID of the student
     */
    fun getSubscribesByStudent(studentId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscriptionsByStudent(studentId)

    fun getSubscriptionsWithCourses(studentId: Int): Flow<List<com.tumme.scrudstudents.data.local.model.SubscribeWithCourse>> = subscribeDao.getSubscriptionsWithCourses(studentId)

    /**
     * Returns subscriptions for a specific course
     * @param courseId The ID of the course
     */
    fun getSubscribesByCourse(courseId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscriptionsByCourse(courseId)

    /**
     * Returns a subscription by student ID and course ID
     * @param studentId The ID of the student
     * @param courseId The ID of the course
     */
    suspend fun getSubscribe(studentId: Int, courseId: Int) = subscribeDao.getSubscription(studentId, courseId)

    /**
     * Inserts a subscription into the table
     * @param subscribe The subscription to insert
     */
    suspend fun insertSubscribe(subscribe: SubscribeEntity) = subscribeDao.insert(subscribe)

    /**
     * Updates a subscription in the table
     * @param subscribe The subscription to update
     */
    suspend fun updateSubscribe(subscribe: SubscribeEntity) = subscribeDao.update(subscribe)

    /**
     * Deletes a subscription from the table
     * @param subscribe The subscription to delete
     */
    suspend fun deleteSubscribe(subscribe: SubscribeEntity) = subscribeDao.delete(subscribe)

    /**
     * Updates the score for a subscription
     * @param studentId The ID of the student
     * @param courseId The ID of the course
     * @param score The new score
     */
    suspend fun updateScore(studentId: Int, courseId: Int, score: Float) = subscribeDao.updateScore(studentId, courseId, score)

    /**
     * Enrolls a student in a course
     * @param studentId The ID of the student
     * @param courseId The ID of the course
     */
    suspend fun enrollInCourse(studentId: Int, courseId: Int) {
        val subscription = SubscribeEntity(
            studentId = studentId,
            courseId = courseId,
            score = 0f, // Default score
            enrollmentDate = System.currentTimeMillis()
        )
        subscribeDao.insert(subscription)
    }

    /**
     * Calculates the final grade for a student in a specific level
     * @param studentId The ID of the student
     * @param levelCode The level code
     * @return The calculated final grade
     */
    suspend fun calculateFinalGrade(studentId: Int, levelCode: String): Float {
        // This is a simplified version - in a real app, you might want to do this in SQL
        val subscriptions = getSubscribesByStudent(studentId).first()
        val courses = getCoursesByLevel(levelCode).first()

        val studentCourses = subscriptions.filter { sub ->
            courses.any { it.idCourse == sub.courseId }
        }

        if (studentCourses.isEmpty()) return 0f

        val totalWeightedScore = studentCourses.sumOf { sub ->
            val course = courses.find { it.idCourse == sub.courseId }
            (sub.score * (course?.ectsCourse ?: 0f)).toDouble()
        }

        val totalEcts = studentCourses.sumOf { sub ->
            val course = courses.find { it.idCourse == sub.courseId }
            (course?.ectsCourse ?: 0f).toDouble()
        }

        return if (totalEcts > 0) (totalWeightedScore / totalEcts).toFloat() else 0f
    }
}
