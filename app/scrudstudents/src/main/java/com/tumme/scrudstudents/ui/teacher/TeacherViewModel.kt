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

/**
 * ViewModel for teacher-related screens.
 * It handles fetching the courses taught by a teacher, the students enrolled in those courses,
 * and updating student grades.
 */
@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    // Holds the list of courses taught by the current teacher.
    private val _teacherCourses = MutableStateFlow<List<CourseEntity>>(emptyList())
    val teacherCourses: StateFlow<List<CourseEntity>> = _teacherCourses.asStateFlow()

    // Holds the list of students enrolled in a specific course, along with their grades.
    private val _studentsForCourse = MutableStateFlow<List<StudentWithScore>>(emptyList())
    val studentsForCourse: StateFlow<List<StudentWithScore>> = _studentsForCourse.asStateFlow()

    // Holds the details of the currently selected course.
    private val _currentCourse = MutableStateFlow<CourseEntity?>(null)
    val currentCourse: StateFlow<CourseEntity?> = _currentCourse

    // Holds the details of the teacher for the currently selected course.
    private val _currentTeacher = MutableStateFlow<TeacherEntity?>(null)
    val currentTeacher: StateFlow<TeacherEntity?> = _currentTeacher

    /**
     * Loads the list of courses taught by a specific teacher.
     * @param teacherId The ID of the teacher.
     */
    fun loadTeacherCourses(teacherId: Int) {
        repository.getCoursesByTeacher(teacherId)
            .onEach { courses: List<CourseEntity> ->
                _teacherCourses.value = courses
            }
            .launchIn(viewModelScope)
    }

    /**
     * Loads all students enrolled in a specific course, along with their current grades.
     * It also fetches the details of the course and the teacher for display in the UI.
     * @param courseId The ID of the course.
     */
    fun loadStudentsForCourse(courseId: Int) {
        viewModelScope.launch {
            repository.getCourseById(courseId)?.let { course ->
                _currentCourse.value = course
                // Find the teacher details from the pre-loaded list of all teachers.
                val teacher = repository.getAllTeachers().first().find { it.teacherId == course.teacherId }
                _currentTeacher.value = teacher
            }
        }

        // Reactively observe the list of students and their scores for the given course.
        repository.getStudentsWithScoresByCourse(courseId)
            .onEach { students: List<StudentWithScore> ->
                _studentsForCourse.value = students
            }
            .launchIn(viewModelScope)
    }

    /**
     * Updates the grade for a specific student in a specific course.
     * @param studentId The ID of the student.
     * @param courseId The ID of the course.
     * @param newScore The new grade to be saved.
     */
    fun updateScore(studentId: Int, courseId: Int, newScore: Float) {
        viewModelScope.launch {
            repository.updateScore(studentId, courseId, newScore)
        }
    }
}
