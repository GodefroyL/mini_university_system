package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.SubscribeEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeFormScreen(
    viewModel: SubscribeViewModel = hiltViewModel(),
    students: List<Pair<Int, String>>,
    courses: List<Pair<Int, String>>,
    onSaved: () -> Unit
) {
    var studentId by remember { mutableStateOf(0) }
    var courseId by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf("0") }
    var expandedStudent by remember { mutableStateOf(false) }
    var expandedCourse by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Student Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedStudent,
            onExpandedChange = { expandedStudent = !expandedStudent }
        ) {
            OutlinedTextField(
                value = students.find { it.first == studentId }?.second ?: "Select Student",
                onValueChange = {},
                readOnly = true,
                label = { Text("Student") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStudent) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedStudent,
                onDismissRequest = { expandedStudent = false }
            ) {
                students.forEach { student ->
                    DropdownMenuItem(
                        text = { Text(student.second) },
                        onClick = {
                            studentId = student.first
                            expandedStudent = false
                        }
                    )
                }
            }
        }

        // Course Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedCourse,
            onExpandedChange = { expandedCourse = !expandedCourse }
        ) {
            OutlinedTextField(
                value = courses.find { it.first == courseId }?.second ?: "Select Course",
                onValueChange = {},
                readOnly = true,
                label = { Text("Course") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCourse) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedCourse,
                onDismissRequest = { expandedCourse = false }
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course.second) },
                        onClick = {
                            courseId = course.first
                            expandedCourse = false
                        }
                    )
                }
            }
        }

        // Score Field
        OutlinedTextField(
            value = score,
            onValueChange = { score = it },
            label = { Text("Score") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.insertSubscribe(
                    SubscribeEntity(
                        studentId = studentId,
                        courseId = courseId,
                        score = score.toFloatOrNull() ?: 0f
                    )
                )
                onSaved()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
