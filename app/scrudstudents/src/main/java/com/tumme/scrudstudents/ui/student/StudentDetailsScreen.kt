package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.StudentEntity
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Composable screen for displaying detailed information about a student.
 *
 * @param studentId The ID of the student to display
 * @param viewModel The ViewModel handling student data operations
 * @param onBack Callback triggered when the user wants to navigate back
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailScreen(
    studentId: Int,
    viewModel: StudentListViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    /** Date formatter for displaying the date of birth */
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /** State for storing the student data */
    var student by remember { mutableStateOf<StudentEntity?>(null) }

    /**
     * LaunchedEffect to fetch student data when the screen is first composed
     * or when the studentId changes
     */
    LaunchedEffect(studentId) {
        student = viewModel.findStudent(studentId)
    }

    /** Scaffold providing a top app bar and content layout */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        /** Main content column with padding */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            /** Loading state while fetching student data */
            if (student == null) {
                Text("Loading...")
            } else {
                /** Display student details */
                Text("ID: ${student!!.idStudent}")
                Text("Name: ${student!!.firstName} ${student!!.lastName}")
                Text("DOB: ${sdf.format(student!!.dateOfBirth)}")
                Text("Gender: ${student!!.gender.value}")
            }
        }
    }
}
