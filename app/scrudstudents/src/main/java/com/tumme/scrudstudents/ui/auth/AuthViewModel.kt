package com.tumme.scrudstudents.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class UserRole {
    STUDENT, TEACHER
}

data class LoggedInUser(
    val userId: Int,
    val name: String,
    val role: UserRole
)

sealed class AuthState {
    object Loading : AuthState()
    data class Authenticated(val user: LoggedInUser) : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // Try to authenticate as a student first
            val student = repository.authenticateStudent(email, password)
            if (student != null) {
                val user = LoggedInUser(
                    userId = student.idStudent,
                    name = "${student.firstName} ${student.lastName}",
                    role = UserRole.STUDENT
                )
                _authState.value = AuthState.Authenticated(user)
                return@launch
            }

            // If not a student, try to authenticate as a teacher
            val teacher = repository.authenticateTeacher(email, password)
            if (teacher != null) {
                val user = LoggedInUser(
                    userId = teacher.teacherId,
                    name = "${teacher.firstName} ${teacher.lastName}",
                    role = UserRole.TEACHER
                )
                _authState.value = AuthState.Authenticated(user)
                return@launch
            }

            // If neither, authentication failed
            _authState.value = AuthState.Error("Invalid email or password")
        }
    }

    fun registerStudent(student: StudentEntity) {
        viewModelScope.launch {
            repository.insertStudent(student)
            // Log the user in directly after registration
            login(student.email, student.password)
        }
    }

    fun registerTeacher(teacher: TeacherEntity) {
        viewModelScope.launch {
            repository.insertTeacher(teacher)
            // Log the user in directly after registration
            login(teacher.email, teacher.password)
        }
    }

    fun logout() {
        _authState.value = AuthState.Unauthenticated
    }
}
