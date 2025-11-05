package com.tumme.scrudstudents.ui.course

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.model.CourseWithTeacher
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val repository: SCRUDRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _allCourses = MutableStateFlow<List<CourseEntity>>(emptyList())
    val allCourses: StateFlow<List<CourseEntity>> = _allCourses.asStateFlow()

    private val _coursesWithTeachers = MutableStateFlow<List<CourseWithTeacher>>(emptyList())
    val coursesWithTeachers: StateFlow<List<CourseWithTeacher>> = _coursesWithTeachers.asStateFlow()

    private val _teachers = MutableStateFlow<List<TeacherEntity>>(emptyList())
    val teachers: StateFlow<List<TeacherEntity>> = _teachers.asStateFlow()

    private val _enrollmentMessage = MutableStateFlow<String?>(null)
    val enrollmentMessage: StateFlow<String?> = _enrollmentMessage.asStateFlow()

    private val studentId: Int = savedStateHandle.get<Int>("studentId") ?: 0

    init {
        val levelCode = savedStateHandle.get<String>("levelCode") ?: ""
        if (levelCode.isNotEmpty()) {
            repository.getCoursesWithTeacherByLevel(levelCode)
                .onEach { _coursesWithTeachers.value = it }
                .launchIn(viewModelScope)
        }

        repository.getAllTeachers()
            .onEach { _teachers.value = it }
            .launchIn(viewModelScope)

        repository.getAllCourses()
            .onEach { _allCourses.value = it }
            .launchIn(viewModelScope)
    }

    fun enrollInCourse(courseId: Int) {
        viewModelScope.launch {
            try {
                repository.enrollInCourse(studentId, courseId)
                _enrollmentMessage.value = "Successfully enrolled!"
            } catch (e: Exception) {
                _enrollmentMessage.value = "Enrollment failed."
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
