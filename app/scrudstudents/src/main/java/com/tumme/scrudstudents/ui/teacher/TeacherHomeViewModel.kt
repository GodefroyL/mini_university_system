package com.tumme.scrudstudents.ui.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.model.CourseWithStudents
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherHomeViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    private val _teacherCourses = MutableStateFlow<List<CourseWithStudents>>(emptyList())
    val teacherCourses: StateFlow<List<CourseWithStudents>> = _teacherCourses.asStateFlow()

    fun loadTeacherData(teacherId: Int) {
        viewModelScope.launch {
            _teacherCourses.value = repository.getCoursesWithStudents(teacherId)
        }
    }
}
