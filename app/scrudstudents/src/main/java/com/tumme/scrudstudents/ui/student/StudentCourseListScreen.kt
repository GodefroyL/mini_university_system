package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * A screen that displays the list of courses a student is currently enrolled in.
 * It observes the student's subscriptions from the [StudentSubscriptionsViewModel].
 *
 * @param studentId The ID of the student whose courses are to be displayed.
 * @param viewModel The ViewModel that provides the data for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCourseListScreen(
    studentId: Int,
    viewModel: StudentSubscriptionsViewModel = hiltViewModel()
) {
    // Observe the list of student's subscriptions from the ViewModel.
    val subscriptions by viewModel.subscriptions.collectAsState()
    // Observe the list of all teachers to find teacher details.
    val teachers by viewModel.teachers.collectAsState()

    // When the screen is first composed or the studentId changes, trigger the ViewModel to load the data.
    LaunchedEffect(studentId) {
        viewModel.loadStudentData(studentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Enrolled Courses") })
        }
    ) { padding ->
        // Display the list of enrolled courses in a LazyColumn for performance.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(subscriptions) { subscription ->
                // Find the teacher for the current course.
                val teacher = teachers.find { it.teacherId == subscription.course.teacherId }
                val teacherDisplayName = teacher?.let { "${it.firstName} ${it.lastName}" } ?: "N/A"

                // Display each course's details in a separate card.
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = subscription.course.nameCourse, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Professor: $teacherDisplayName",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "ECTS Credits: ${subscription.course.ectsCourse}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = subscription.course.description ?: "",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
