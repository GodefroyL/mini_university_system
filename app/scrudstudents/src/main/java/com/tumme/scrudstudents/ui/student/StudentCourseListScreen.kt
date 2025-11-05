package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCourseListScreen(
    studentId: Int,
    viewModel: StudentSubscriptionsViewModel = hiltViewModel()
) {
    val subscriptions by viewModel.subscriptions.collectAsState()
    val teachers by viewModel.teachers.collectAsState()

    // Explicitly load data when the screen is composed with a valid studentId
    LaunchedEffect(studentId) {
        viewModel.loadStudentData(studentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Enrolled Courses") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(subscriptions) { subscription ->
                val teacher = teachers.find { it.teacherId == subscription.course.teacherId }
                val teacherDisplayName = teacher?.let { "${it.firstName} ${it.lastName}" } ?: "N/A"

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
