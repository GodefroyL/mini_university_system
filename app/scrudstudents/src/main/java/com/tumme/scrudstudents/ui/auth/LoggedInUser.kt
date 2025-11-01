package com.tumme.scrudstudents.ui.auth

data class LoggedInUser(
    val id: Int,
    val role: UserRole,
    val level: String? = null // Nullable for teachers
)

enum class UserRole {
    STUDENT,
    TEACHER
}
