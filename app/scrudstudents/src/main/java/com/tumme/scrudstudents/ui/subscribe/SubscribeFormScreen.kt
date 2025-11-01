package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeFormScreen(
    viewModel: SubscribeViewModel = hiltViewModel(),
    onSaved: () -> Unit = {}
) {
    val students by viewModel.students.collectAsState()
    val courses by viewModel.courses.collectAsState()

    var selectedStudent by remember { mutableStateOf<StudentEntity?>(null) }
    var selectedCourse by remember { mutableStateOf<CourseEntity?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Student Dropdown
        var studentExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = studentExpanded, onExpandedChange = { studentExpanded = !studentExpanded }) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedStudent?.let { "${it.firstName} ${it.lastName}" } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Student") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = studentExpanded) }
            )
            ExposedDropdownMenu(expanded = studentExpanded, onDismissRequest = { studentExpanded = false }) {
                students.forEach { student ->
                    DropdownMenuItem(
                        text = { Text("${student.firstName} ${student.lastName}") },
                        onClick = {
                            selectedStudent = student
                            studentExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))

        // Course Dropdown
        var courseExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = courseExpanded, onExpandedChange = { courseExpanded = !courseExpanded }) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedCourse?.nameCourse ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Course") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseExpanded) }
            )
            ExposedDropdownMenu(expanded = courseExpanded, onDismissRequest = { courseExpanded = false }) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course.nameCourse) },
                        onClick = {
                            selectedCourse = course
                            courseExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                selectedStudent?.let { student ->
                    selectedCourse?.let { course ->
                        viewModel.insertSubscription(student.idStudent, course.idCourse)
                        onSaved()
                    }
                }
            },
            enabled = selectedStudent != null && selectedCourse != null
        ) {
            Text("Subscribe")
        }
    }
}
