package com.tumme.scrudstudents.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.* 
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: LoggedInUser) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    /**
     * Exposes a flow of unique level codes from the courses for the registration dropdown.
     */
    val availableLevels: StateFlow<List<String>> = repository.getAllCourses()
        .map { courses -> courses.map { it.levelCode }.distinct().filter { it.isNotBlank() } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun login(email: String, password: String, role: UserRole) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = when (role) {
                    UserRole.STUDENT -> {
                        val student = repository.authenticateStudent(email, password)
                        student?.let { LoggedInUser(it.idStudent, UserRole.STUDENT, it.levelCode) }
                    }
                    UserRole.TEACHER -> {
                        val teacher = repository.authenticateTeacher(email, password)
                        teacher?.let { LoggedInUser(it.teacherId, UserRole.TEACHER) }
                    }
                }

                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                } else {
                    _authState.value = AuthState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun registerStudent(student: StudentEntity) {
        viewModelScope.launch {
            repository.insertStudent(student)
            val newStudent = repository.authenticateStudent(student.email, student.password)
            newStudent?.let {
                val user = LoggedInUser(it.idStudent, UserRole.STUDENT, it.levelCode)
                _authState.value = AuthState.Authenticated(user)
            }
        }
    }

    fun registerTeacher(teacher: TeacherEntity) {
        viewModelScope.launch {
            repository.insertTeacher(teacher)
            val newTeacher = repository.authenticateTeacher(teacher.email, teacher.password)
            newTeacher?.let {
                val user = LoggedInUser(it.teacherId, UserRole.TEACHER)
                _authState.value = AuthState.Authenticated(user)
            }
        }
    }
}
