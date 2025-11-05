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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSubscriptionsScreen(
    studentId: Int,
    levelCode: String,
    viewModel: CourseListViewModel = hiltViewModel()
) {
    val courses by viewModel.coursesWithTeachers.collectAsState()
    val enrollmentMessage by viewModel.enrollmentMessage.collectAsState()

    var selectedCourse by remember { mutableStateOf<CourseWithTeacher?>(null) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Load courses for the student's level
    LaunchedEffect(levelCode) {
        viewModel.loadCoursesForLevel(levelCode)
    }

    // Show snackbar on enrollment confirmation
    LaunchedEffect(enrollmentMessage) {
        enrollmentMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearEnrollmentMessage()
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
            // Course Selection Dropdown
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

            // Selected Course Details and Enroll Button
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
