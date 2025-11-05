package com.tumme.scrudstudents.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.SubscribeWithCourse
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * ViewModel for screens related to a student's own subscriptions and grades.
 * It fetches the list of courses a student is enrolled in and calculates their overall grade average.
 */
@HiltViewModel
class StudentSubscriptionsViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    // Holds the reactive list of courses the student is subscribed to, including course details.
    private val _subscriptions = MutableStateFlow<List<SubscribeWithCourse>>(emptyList())
    val subscriptions: StateFlow<List<SubscribeWithCourse>> = _subscriptions.asStateFlow()

    // Holds the list of all teachers to easily find teacher details for a course.
    private val _teachers = MutableStateFlow<List<TeacherEntity>>(emptyList())
    val teachers: StateFlow<List<TeacherEntity>> = _teachers.asStateFlow()

    // Holds the calculated weighted average of the student's grades.
    private val _weightedAverage = MutableStateFlow(0f)
    val weightedAverage: StateFlow<Float> = _weightedAverage.asStateFlow()

    /**
     * Loads all relevant data for a specific student.
     * This function is the main entry point for the ViewModel to start collecting data.
     * @param studentId The ID of the student whose data needs to be loaded.
     */
    fun loadStudentData(studentId: Int) {
        if (studentId <= 0) return

        // Load all teachers to find them by ID later.
        repository.getAllTeachers()
            .onEach { _teachers.value = it }
            .launchIn(viewModelScope)

        // Load the student's subscriptions and trigger the grade calculation on each update.
        repository.getSubscriptionsWithCourses(studentId)
            .onEach {
                _subscriptions.value = it
                calculateWeightedAverage(it)
            }
            .launchIn(viewModelScope)
    }

    /**
     * Calculates the weighted average of a student's grades based on their subscriptions and course ECTS credits.
     * @param subscriptions The list of subscriptions with course and grade details.
     */
    private fun calculateWeightedAverage(subscriptions: List<SubscribeWithCourse>) {
        var totalWeightedScore = 0f
        var totalEcts = 0f

        subscriptions.forEach { sub ->
            // Only include courses that have been graded (score > 0).
            if (sub.subscribe.score > 0) {
                totalWeightedScore += sub.subscribe.score * sub.course.ectsCourse
                totalEcts += sub.course.ectsCourse
            }
        }

        _weightedAverage.value = if (totalEcts > 0) totalWeightedScore / totalEcts else 0f
    }
}
