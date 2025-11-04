package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.model.*
import com.tumme.scrudstudents.data.model.CourseWithStudents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SCRUDRepository(
    private val studentDao: StudentDao,
    private val teacherDao: TeacherDao,
    private val courseDao: CourseDao,
    private val subscribeDao: SubscribeDao
) {
    // Students
    fun getAllStudents(): Flow<List<StudentEntity>> = studentDao.getAllStudents()
    suspend fun insertStudent(student: StudentEntity) = studentDao.insert(student)
    suspend fun updateStudent(student: StudentEntity) = studentDao.update(student)
    suspend fun deleteStudent(student: StudentEntity) = studentDao.delete(student)
    suspend fun getStudentById(id: Int) = studentDao.getStudentById(id)
    suspend fun authenticateStudent(email: String, password: String) = studentDao.authenticate(email, password)

    // Teachers
    fun getAllTeachers(): Flow<List<TeacherEntity>> = teacherDao.getAllTeachers()
    suspend fun insertTeacher(teacher: TeacherEntity) = teacherDao.insert(teacher)
    suspend fun updateTeacher(teacher: TeacherEntity) = teacherDao.update(teacher)
    suspend fun deleteTeacher(teacher: TeacherEntity) = teacherDao.delete(teacher)
    suspend fun getTeacherById(id: Int) = teacherDao.getTeacherById(id)
    suspend fun authenticateTeacher(email: String, password: String) = teacherDao.authenticate(email, password)

    // Courses
    fun getAllCourses(): Flow<List<CourseEntity>> = courseDao.getAllCourses()
    fun getCoursesByLevel(levelCode: String): Flow<List<CourseEntity>> = courseDao.getCoursesByLevel(levelCode)
    fun getCoursesByTeacher(teacherId: Int): Flow<List<CourseEntity>> = courseDao.getCoursesByTeacher(teacherId)
    suspend fun insertCourse(course: CourseEntity) = courseDao.insert(course)
    suspend fun updateCourse(course: CourseEntity) = courseDao.update(course)
    suspend fun deleteCourse(course: CourseEntity) = courseDao.delete(course)
    suspend fun getCourseById(id: Int) = courseDao.getCourseById(id)
    suspend fun getCoursesWithStudents(teacherId: Int): List<CourseWithStudents> = courseDao.getCoursesWithStudents(teacherId)

    // Subscribes
    fun getAllSubscribes(): Flow<List<SubscribeEntity>> = subscribeDao.getAllSubscribes()
    fun getSubscribesByStudent(studentId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscriptionsByStudent(studentId)
    fun getSubscriptionsWithCourses(studentId: Int): Flow<List<SubscribeWithCourse>> = subscribeDao.getSubscriptionsWithCourses(studentId)
    fun getSubscribesByCourse(courseId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscriptionsByCourse(courseId)
    suspend fun getSubscribe(studentId: Int, courseId: Int) = subscribeDao.getSubscription(studentId, courseId)
    suspend fun insertSubscribe(subscribe: SubscribeEntity) = subscribeDao.insert(subscribe)
    suspend fun updateSubscribe(subscribe: SubscribeEntity) = subscribeDao.update(subscribe)
    suspend fun deleteSubscribe(subscribe: SubscribeEntity) = subscribeDao.delete(subscribe)
    suspend fun updateScore(studentId: Int, courseId: Int, score: Float) = subscribeDao.updateScore(studentId, courseId, score)
    suspend fun enrollInCourse(studentId: Int, courseId: Int) {
        val subscription = SubscribeEntity(studentId = studentId, courseId = courseId)
        subscribeDao.insert(subscription)
    }

    fun getStudentsWithScoresByCourse(courseId: Int): Flow<List<StudentWithScore>> = subscribeDao.getStudentsWithScoresByCourse(courseId)

    suspend fun calculateFinalGrade(studentId: Int, levelCode: String): Float {
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
