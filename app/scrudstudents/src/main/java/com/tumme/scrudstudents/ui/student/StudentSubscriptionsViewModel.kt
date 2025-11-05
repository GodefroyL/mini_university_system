package com.tumme.scrudstudents.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.SubscribeWithCourse
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class StudentSubscriptionsViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    private val _subscriptions = MutableStateFlow<List<SubscribeWithCourse>>(emptyList())
    val subscriptions: StateFlow<List<SubscribeWithCourse>> = _subscriptions.asStateFlow()

    private val _teachers = MutableStateFlow<List<TeacherEntity>>(emptyList())
    val teachers: StateFlow<List<TeacherEntity>> = _teachers.asStateFlow()

    private val _weightedAverage = MutableStateFlow(0f)
    val weightedAverage: StateFlow<Float> = _weightedAverage.asStateFlow()

    fun loadStudentData(studentId: Int) {
        if (studentId <= 0) return

        // Load all teachers to find them by ID later
        repository.getAllTeachers()
            .onEach { _teachers.value = it }
            .launchIn(viewModelScope)

        // Load student's subscriptions and calculate average
        repository.getSubscriptionsWithCourses(studentId)
            .onEach {
                _subscriptions.value = it
                calculateWeightedAverage(it)
            }
            .launchIn(viewModelScope)
    }

    private fun calculateWeightedAverage(subscriptions: List<SubscribeWithCourse>) {
        var totalWeightedScore = 0f
        var totalEcts = 0f

        subscriptions.forEach { sub ->
            if (sub.subscribe.score > 0) { // Only include graded courses
                totalWeightedScore += sub.subscribe.score * sub.course.ectsCourse
                totalEcts += sub.course.ectsCourse
            }
        }

        _weightedAverage.value = if (totalEcts > 0) totalWeightedScore / totalEcts else 0f
    }
}
