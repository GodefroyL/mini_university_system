package com.tumme.scrudstudents.ui.teacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentWithScore
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherCourseDetailViewModel @Inject constructor(
    private val repository: SCRUDRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val courseId: Int = checkNotNull(savedStateHandle["courseId"])

    val students: StateFlow<List<StudentWithScore>> = repository.getStudentsWithScoresByCourse(courseId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateStudentScore(studentId: Int, score: Float) {
        viewModelScope.launch {
            repository.updateScore(studentId, courseId, score)
        }
    }
}
