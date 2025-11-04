package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.StudentWithScore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherCourseDetailScreen(
    viewModel: TeacherCourseDetailViewModel = hiltViewModel()
) {
    val students by viewModel.students.collectAsState()
    var selectedStudent by remember { mutableStateOf<StudentWithScore?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var scoreText by remember { mutableStateOf("") }

    if (showDialog && selectedStudent != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Update Score for ${selectedStudent!!.firstName} ${selectedStudent!!.lastName}") },
            text = {
                OutlinedTextField(
                    value = scoreText,
                    onValueChange = { scoreText = it },
                    label = { Text("Score") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                Button(onClick = {
                    val score = scoreText.toFloatOrNull()
                    if (score != null) {
                        viewModel.updateStudentScore(selectedStudent!!.idStudent, score)
                    }
                    showDialog = false
                }) {
                    Text("Update")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(students) { student ->
            ListItem(
                headlineContent = { Text("${student.firstName} ${student.lastName}") },
                supportingContent = { Text("Score: ${student.score}") },
                modifier = Modifier.clickable {
                    selectedStudent = student
                    scoreText = student.score.toString()
                    showDialog = true
                }
            )
        }
    }
}
