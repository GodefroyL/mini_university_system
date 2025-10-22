package com.tumme.scrudstudents.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
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
class StudentListViewModel @Inject constructor(
    private val repo: SCRUDRepository // Repository for database operations
) : ViewModel() {

    /** StateFlow exposing the list of students from the database. */
    private val _students: StateFlow<List<StudentEntity>> =
        repo.getAllStudents().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val students: StateFlow<List<StudentEntity>> = _students

    /** SharedFlow for UI events and error messages. */
    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    /** Deletes a student and emits a success event. */
    fun deleteStudent(student: StudentEntity) = viewModelScope.launch {
        repo.deleteStudent(student)
        _events.emit("Student deleted")
    }

    /** Inserts a student and emits a success event. */
    fun insertStudent(student: StudentEntity) = viewModelScope.launch {
        repo.insertStudent(student)
        _events.emit("Student inserted")
    }

    /** Finds a student by ID. */
    suspend fun findStudent(id: Int) = repo.getStudentById(id)
}