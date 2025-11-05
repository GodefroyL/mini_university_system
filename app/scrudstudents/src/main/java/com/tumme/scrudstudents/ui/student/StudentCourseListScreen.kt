package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.course.CourseListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCourseListScreen(
    levelCode: String,
    viewModel: CourseListViewModel = hiltViewModel()
) {
    val coursesWithTeachers by viewModel.coursesWithTeachers.collectAsState()
    val enrollmentMessage by viewModel.enrollmentMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(enrollmentMessage) {
        enrollmentMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearEnrollmentMessage() // Clear message after showing
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(coursesWithTeachers) { courseWithTeacher ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { // Make the whole card clickable
                            viewModel.enrollInCourse(courseWithTeacher.course.idCourse)
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = courseWithTeacher.course.nameCourse, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Teacher: ${courseWithTeacher.teacher?.firstName} ${courseWithTeacher.teacher?.lastName}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "ECTS: ${courseWithTeacher.course.ectsCourse}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
