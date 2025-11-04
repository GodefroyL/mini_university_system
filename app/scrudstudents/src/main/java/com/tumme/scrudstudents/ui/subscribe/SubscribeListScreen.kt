package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.ui.components.TableHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeListScreen(
    navController: NavController,
    viewModel: SubscribeViewModel = hiltViewModel()
) {
    val subscribes by viewModel.subscribes.collectAsState()
    val students by viewModel.students.collectAsState()
    val courses by viewModel.courses.collectAsState()

    // Create maps for quick lookup of names by ID
    val studentMap = remember(students) {
        students.associateBy(
            keySelector = { student: StudentEntity -> student.idStudent },
            valueTransform = { student: StudentEntity -> "${student.firstName} ${student.lastName}" }
        )
    }
    val courseMap = remember(courses) {
        courses.associateBy(
            keySelector = { course: CourseEntity -> course.idCourse },
            valueTransform = { course: CourseEntity -> course.nameCourse }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Subscriptions") }) }
        // The FloatingActionButton has been removed as it referenced a route
        // that is no longer part of the new role-based navigation flow.
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TableHeader(
                cells = listOf("Student", "Course", "Score", "Actions"),
                weights = listOf(0.4f, 0.4f, 0.1f, 0.1f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(subscribes) { subscribe ->
                    val studentName = studentMap[subscribe.studentId] ?: "Unknown Student"
                    val courseName = courseMap[subscribe.courseId] ?: "Unknown Course"

                    SubscribeRow(
                        studentName = studentName,
                        courseName = courseName,
                        score = subscribe.score,
                        onDelete = { viewModel.deleteSubscribe(subscribe) }
                    )
                }
            }
        }
    }
}
