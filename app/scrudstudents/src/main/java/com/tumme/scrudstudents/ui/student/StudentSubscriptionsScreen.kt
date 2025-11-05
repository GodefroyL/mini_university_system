package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.model.CourseWithTeacher
import com.tumme.scrudstudents.ui.course.CourseListViewModel
import kotlinx.coroutines.launch

/**
 * A screen that allows a student to enroll in available courses for their level.
 * It displays a dropdown of available courses and shows details for the selected one.
 *
 * @param studentId The ID of the current student.
 * @param levelCode The academic level of the student.
 * @param viewModel The ViewModel that provides the data and logic for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSubscriptionsScreen(
    studentId: Int,
    levelCode: String,
    viewModel: CourseListViewModel = hiltViewModel()
) {
    // Observe the list of available courses from the ViewModel.
    val courses by viewModel.coursesWithTeachers.collectAsState()
    // Observe the one-time enrollment message for showing confirmations.
    val enrollmentMessage by viewModel.enrollmentMessage.collectAsState()

    // State for the currently selected course in the dropdown.
    var selectedCourse by remember { mutableStateOf<CourseWithTeacher?>(null) }
    // State to manage whether the dropdown menu is expanded or not.
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // When the screen is first displayed or studentId/levelCode changes, trigger the ViewModel
    // to load the list of courses the student can enroll in.
    LaunchedEffect(studentId, levelCode) {
        viewModel.loadAvailableCourses(studentId, levelCode)
    }

    // When an enrollment message is received from the ViewModel, show a snackbar.
    // After showing, clear the message and reset the course selection to provide clear visual feedback.
    LaunchedEffect(enrollmentMessage) {
        enrollmentMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearEnrollmentMessage()
                selectedCourse = null // Clear the selection for immediate visual feedback
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Enroll in a Course") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // A dropdown menu that allows the user to select a course.
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                TextField(
                    value = selectedCourse?.course?.nameCourse ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select a Course") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    courses.forEach { courseWithTeacher ->
                        DropdownMenuItem(
                            text = { Text(courseWithTeacher.course.nameCourse) },
                            onClick = {
                                selectedCourse = courseWithTeacher
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // When a course is selected, display its details in a card.
            selectedCourse?.let { course ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = course.course.nameCourse, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Professor: ${course.teacher?.firstName} ${course.teacher?.lastName}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "ECTS Credits: ${course.course.ectsCourse}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Description:", style = MaterialTheme.typography.bodyMedium)
                        Text(text = course.course.description ?: "", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.enrollInCourse(studentId, course.course.idCourse) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Enroll")
                        }
                    }
                }
            }
        }
    }
}
