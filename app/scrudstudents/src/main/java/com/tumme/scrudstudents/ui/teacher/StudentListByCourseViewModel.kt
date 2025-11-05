package com.tumme.scrudstudents.ui.teacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentWithScore // Corrected import
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
class StudentListByCourseViewModel @Inject constructor(
    private val repository: SCRUDRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _studentsWithScores = MutableStateFlow<List<StudentWithScore>>(emptyList())
    val studentsWithScores: StateFlow<List<StudentWithScore>> = _studentsWithScores.asStateFlow()

    private val courseId: Int = savedStateHandle.get<Int>("courseId") ?: 0

    init {
        if (courseId > 0) {
            repository.getStudentsWithScoresByCourse(courseId)
                .onEach { _studentsWithScores.value = it }
                .launchIn(viewModelScope)
        }
    }

    fun updateStudentScore(studentId: Int, score: Float) {
        viewModelScope.launch {
            repository.updateScore(studentId, courseId, score)
        }
    }
}
