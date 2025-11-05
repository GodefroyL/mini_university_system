package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * The main screen for a logged-in teacher.
 * It displays a list of all courses taught by the teacher.
 *
 * @param teacherId The ID of the currently logged-in teacher.
 * @param onCourseClick A lambda function to be invoked when a course card is clicked, usually for navigation.
 * @param viewModel The ViewModel providing the data for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherCourseListScreen(
    teacherId: Int,
    onCourseClick: (Int) -> Unit,
    viewModel: TeacherViewModel = hiltViewModel()
) {
    // Observe the list of courses taught by the teacher from the ViewModel.
    val courses by viewModel.teacherCourses.collectAsState()

    // When the screen is first composed or the teacherId changes, trigger the ViewModel to load the courses.
    LaunchedEffect(teacherId) {
        viewModel.loadTeacherCourses(teacherId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Courses") })
        }
    ) { padding ->
        // Display the list of courses in a LazyColumn for better performance.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(courses) { course ->
                // Each course is displayed in a clickable card.
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCourseClick(course.idCourse) }, // Navigate on click
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = course.nameCourse, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Level: ${course.levelCode}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "ECTS: ${course.ectsCourse}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = course.description ?: "No description available.",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 3
                        )
                    }
                }
            }
        }
    }
}
