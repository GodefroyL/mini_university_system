package com.tumme.scrudstudents.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for student-related UI composables.
 * Uses the repository to fetch and manage student data.
 * Injected by Hilt for dependency management.
 */
@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val repo: SCRUDRepository // Repository for database operations
) : ViewModel() {

    /** StateFlow exposing the list of courses from the database. */
    private val _courses: StateFlow<List<CourseEntity>> =
        repo.getAllCourses().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val students: StateFlow<List<CourseEntity>> = _courses

    /** SharedFlow for UI events and error messages. */
    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    /** Deletes a course and emits a success event. */
    fun deleteStudent(course: CourseEntity) = viewModelScope.launch {
        repo.deleteCourse(course)
        _events.emit("Course deleted")
    }

    /** Inserts a course and emits a success event. */
    fun insertCourse(course: CourseEntity) = viewModelScope.launch {
        repo.insertCourse(course)
        _events.emit("Course inserted")
    }

    /** Finds a course by ID. */
    suspend fun findCourse(id: Int) = repo.getCourseById(id)
}