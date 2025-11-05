package com.tumme.scrudstudents.ui.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentWithScore
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    private val _teacherCourses = MutableStateFlow<List<CourseEntity>>(emptyList())
    val teacherCourses: StateFlow<List<CourseEntity>> = _teacherCourses.asStateFlow()

    private val _studentsForCourse = MutableStateFlow<List<StudentWithScore>>(emptyList())
    val studentsForCourse: StateFlow<List<StudentWithScore>> = _studentsForCourse.asStateFlow()

    private val _currentCourse = MutableStateFlow<CourseEntity?>(null)
    val currentCourse: StateFlow<CourseEntity?> = _currentCourse

    private val _currentTeacher = MutableStateFlow<TeacherEntity?>(null)
    val currentTeacher: StateFlow<TeacherEntity?> = _currentTeacher

    fun loadTeacherCourses(teacherId: Int) {
        repository.getCoursesByTeacher(teacherId)
            .onEach { courses: List<CourseEntity> ->
                _teacherCourses.value = courses
            }
            .launchIn(viewModelScope)
    }

    fun loadStudentsForCourse(courseId: Int) {
        viewModelScope.launch {
            repository.getCourseById(courseId)?.let { course ->
                _currentCourse.value = course
                val teacher = repository.getAllTeachers().first().find { it.teacherId == course.teacherId }
                _currentTeacher.value = teacher
            }
        }

        repository.getStudentsWithScoresByCourse(courseId)
            .onEach { students: List<StudentWithScore> ->
                _studentsForCourse.value = students
            }
            .launchIn(viewModelScope)
    }

    fun updateScore(studentId: Int, courseId: Int, newScore: Float) {
        viewModelScope.launch {
            repository.updateScore(studentId, courseId, newScore)
        }
    }
}
