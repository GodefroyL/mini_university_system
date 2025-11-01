package com.tumme.scrudstudents.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.SubscribeWithCourse
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentHomeViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    private val _studentSubscriptions = MutableStateFlow<List<SubscribeWithCourse>>(emptyList())
    val studentSubscriptions: StateFlow<List<SubscribeWithCourse>> = _studentSubscriptions.asStateFlow()

    private val _finalGrade = MutableStateFlow(0f)
    val finalGrade: StateFlow<Float> = _finalGrade.asStateFlow()

    fun loadStudentData(studentId: Int, levelCode: String) {
        viewModelScope.launch {
            repository.getSubscriptionsWithCourses(studentId).collect {
                _studentSubscriptions.value = it
            }
        }
        viewModelScope.launch {
            _finalGrade.value = repository.calculateFinalGrade(studentId, levelCode)
        }
    }
}
