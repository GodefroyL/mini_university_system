package com.tumme.scrudstudents.ui.student

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentDashboardViewModel @Inject constructor(
    private val repository: SCRUDRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _student = MutableStateFlow<StudentEntity?>(null)
    val student: StateFlow<StudentEntity?> = _student.asStateFlow()

    init {
        val studentId: Int = savedStateHandle.get<Int>("studentId") ?: 0
        if (studentId > 0) {
            viewModelScope.launch {
                _student.value = repository.getStudentById(studentId)
            }
        }
    }
}
