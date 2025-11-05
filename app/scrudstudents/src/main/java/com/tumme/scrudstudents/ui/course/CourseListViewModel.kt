package com.tumme.scrudstudents.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.model.CourseWithTeacher
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    private val _allCourses = MutableStateFlow<List<CourseEntity>>(emptyList())
    val allCourses: StateFlow<List<CourseEntity>> = _allCourses.asStateFlow()

    private val _coursesWithTeachers = MutableStateFlow<List<CourseWithTeacher>>(emptyList())
    val coursesWithTeachers: StateFlow<List<CourseWithTeacher>> = _coursesWithTeachers.asStateFlow()

    private val _teachers = MutableStateFlow<List<TeacherEntity>>(emptyList())
    val teachers: StateFlow<List<TeacherEntity>> = _teachers.asStateFlow()

    private val _enrollmentMessage = MutableStateFlow<String?>(null)
    val enrollmentMessage: StateFlow<String?> = _enrollmentMessage.asStateFlow()

    init {
        repository.getAllTeachers()
            .onEach { _teachers.value = it }
            .launchIn(viewModelScope)

        repository.getAllCourses()
            .onEach { _allCourses.value = it }
            .launchIn(viewModelScope)
    }

    fun loadAvailableCourses(studentId: Int, levelCode: String) {
        if (levelCode.isEmpty() || studentId <= 0) {
            _coursesWithTeachers.value = emptyList()
            return
        }

        val allCoursesForLevelFlow = repository.getCoursesWithTeacherByLevel(levelCode)
        val studentSubscriptionsFlow = repository.getSubscriptionsWithCourses(studentId)

        allCoursesForLevelFlow.combine(studentSubscriptionsFlow) { allCourses, subscriptions ->
            val subscribedCourseIds = subscriptions.map { it.course.idCourse }.toSet()
            allCourses.filter { it.course.idCourse !in subscribedCourseIds }
        }.onEach { availableCourses ->
            _coursesWithTeachers.value = availableCourses
        }.launchIn(viewModelScope)
    }

    fun enrollInCourse(studentId: Int, courseId: Int) {
        viewModelScope.launch {
            try {
                repository.enrollInCourse(studentId, courseId)
                _enrollmentMessage.value = "Successfully enrolled!"
            } catch (e: Exception) {
                _enrollmentMessage.value = "Enrollment failed: ${e.message}"
            }
        }
    }

    fun clearEnrollmentMessage() {
        _enrollmentMessage.value = null
    }

    suspend fun findCourse(courseId: Int): CourseEntity? {
        return repository.getCourseById(courseId)
    }

    fun insertCourse(course: CourseEntity) {
        viewModelScope.launch {
            repository.insertCourse(course)
        }
    }

    fun deleteCourse(course: CourseEntity) {
        viewModelScope.launch {
            repository.deleteCourse(course)
        }
    }
}
