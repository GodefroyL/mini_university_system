package com.tumme.scrudstudents.ui.course

import com.tumme.scrudstudents.ui.components.TableHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.student.CourseRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    viewModel: CourseListViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {}
) {
    val courses by viewModel.allCourses.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Courses") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TableHeader(
                cells = listOf("Name", "Level", "ECTS", "Actions"),
                weights = listOf(0.4f, 0.3f, 0.2f, 0.1f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(courses) { course ->
                    CourseRow(
                        course = course,
                        onEdit = { /* Not implemented */ },
                        onDelete = { viewModel.deleteCourse(course) },
                        onView = { onNavigateToDetail(course.idCourse) },
                        onShare = { /* Not implemented */ }
                    )
                }
            }
        }
    }
}
