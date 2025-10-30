package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumme.scrudstudents.ui.components.TableHeader
import com.tumme.scrudstudents.ui.course.CourseListViewModel
import com.tumme.scrudstudents.ui.navigation.Routes
import com.tumme.scrudstudents.ui.student.StudentListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeListScreen(
    navController: NavController,
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
    studentViewModel: StudentListViewModel = hiltViewModel(),
    courseViewModel: CourseListViewModel = hiltViewModel()
) {
    val subscribes by subscribeViewModel.subscribes.collectAsState()
    val students by studentViewModel.students.collectAsState()
    val courses by courseViewModel.courses.collectAsState()

    // Create maps for quick lookup of names by ID
    val studentMap = remember(students) { students.associateBy({ it.idStudent }, { "${it.firstName} ${it.lastName}" }) }
    val courseMap = remember(courses) { courses.associateBy({ it.idCourse }, { it.nameCourse }) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Subscriptions") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.SUBSCRIBE_FORM) }) {
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
                        onDelete = { subscribeViewModel.deleteSubscribe(subscribe) }
                    )
                }
            }
        }
    }
}
