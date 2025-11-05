package com.tumme.scrudstudents.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.model.CourseWithTeacher
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and business logic for course-related screens.
 * It handles fetching course lists, enrolling students, and managing UI messages.
 */
@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    // Holds the complete list of all courses, used for administrative purposes.
    private val _allCourses = MutableStateFlow<List<CourseEntity>>(emptyList())
    val allCourses: StateFlow<List<CourseEntity>> = _allCourses.asStateFlow()

    // Holds the list of courses with their assigned teachers, filtered and ready for display.
    private val _coursesWithTeachers = MutableStateFlow<List<CourseWithTeacher>>(emptyList())
    val coursesWithTeachers: StateFlow<List<CourseWithTeacher>> = _coursesWithTeachers.asStateFlow()

    // Holds the complete list of all teachers.
    private val _teachers = MutableStateFlow<List<TeacherEntity>>(emptyList())
    val teachers: StateFlow<List<TeacherEntity>> = _teachers.asStateFlow()

    // Holds a one-time message for the UI, e.g., to confirm enrollment.
    private val _enrollmentMessage = MutableStateFlow<String?>(null)
    val enrollmentMessage: StateFlow<String?> = _enrollmentMessage.asStateFlow()

    /**
     * Pre-loads essential data as soon as the ViewModel is created.
     */
    init {
        repository.getAllTeachers()
            .onEach { _teachers.value = it }
            .launchIn(viewModelScope)

        repository.getAllCourses()
            .onEach { _allCourses.value = it }
            .launchIn(viewModelScope)
    }

    /**
     * Reactively loads the list of available courses for a specific student and level.
     * It fetches all courses for the level and filters out those the student is already enrolled in.
     * @param studentId The ID of the student.
     * @param levelCode The academic level code (e.g., "L1", "M2").
     */
    fun loadAvailableCourses(studentId: Int, levelCode: String) {
        if (levelCode.isEmpty() || studentId <= 0) {
            _coursesWithTeachers.value = emptyList()
            return
        }

        val allCoursesForLevelFlow = repository.getCoursesWithTeacherByLevel(levelCode)
        val studentSubscriptionsFlow = repository.getSubscriptionsWithCourses(studentId)

        // Combine the two flows: all courses for the level and the student's current subscriptions.
        allCoursesForLevelFlow.combine(studentSubscriptionsFlow) { allCourses, subscriptions ->
            val subscribedCourseIds = subscriptions.map { it.course.idCourse }.toSet()
            // Filter the list to keep only courses the student is not yet subscribed to.
            allCourses.filter { it.course.idCourse !in subscribedCourseIds }
        }.onEach { availableCourses ->
            // Update the UI state with the final list of available courses.
            _coursesWithTeachers.value = availableCourses
        }.launchIn(viewModelScope)
    }

    /**
     * Enrolls the current student in a selected course.
     * Posts a confirmation or error message to the UI.
     * @param studentId The ID of the student to enroll.
     * @param courseId The ID of the course to enroll in.
     */
    fun enrollInCourse(studentId: Int, courseId: Int) {
        viewModelScope.launch {
            try {
                repository.enrollInCourse(studentId, courseId)
                _enrollmentMessage.value = "Successfully enrolled!"
            } catch (e: Exception) {
                _enrollmentMessage.value = "Enrollment failed: ${e.message}"
            }
        }
    }

    /**
     * Clears the enrollment message after it has been displayed.
     */
    fun clearEnrollmentMessage() {
        _enrollmentMessage.value = null
    }

    /**
     * Finds a single course by its ID.
     */
    suspend fun findCourse(courseId: Int): CourseEntity? {
        return repository.getCourseById(courseId)
    }

    /**
     * Inserts a new course into the database. (For admin purposes)
     */
    fun insertCourse(course: CourseEntity) {
        viewModelScope.launch {
            repository.insertCourse(course)
        }
    }

    /**
     * Deletes a course from the database. (For admin purposes)
     */
    fun deleteCourse(course: CourseEntity) {
        viewModelScope.launch {
            repository.deleteCourse(course)
        }
    }
}
