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

@Composable
fun StudentListByCourseScreen(
    viewModel: StudentListByCourseViewModel = hiltViewModel()
) {
    val studentsWithScores by viewModel.studentsWithScores.collectAsState()
    var selectedStudent by remember { mutableStateOf<StudentWithScore?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var scoreInput by remember { mutableStateOf("") }

    val studentForDialog = selectedStudent

    if (showDialog && studentForDialog != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Update Score for ${studentForDialog.firstName}") },
            text = {
                OutlinedTextField(
                    value = scoreInput,
                    onValueChange = { scoreInput = it },
                    label = { Text("Score") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val score = scoreInput.toFloatOrNull()
                        if (score != null) {
                            viewModel.updateStudentScore(studentForDialog.idStudent, score)
                        }
                        showDialog = false
                        scoreInput = ""
                    }
                ) {
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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(studentsWithScores) { studentWithScore ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { // Allow clicking to open the score dialog
                        selectedStudent = studentWithScore
                        scoreInput = studentWithScore.score.toString()
                        showDialog = true
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${studentWithScore.firstName} ${studentWithScore.lastName}")
                Text(text = "Score: ${studentWithScore.score}")
            }
        }
    }
}
