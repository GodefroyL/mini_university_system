package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.StudentWithScore

/**
 * A screen where a teacher can view and edit the grades of all students enrolled in a specific course.
 *
 * @param courseId The ID of the course for which to display the students.
 * @param onBack A lambda function to be invoked to navigate back to the previous screen.
 * @param viewModel The ViewModel providing the data and business logic for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherStudentListScreen(
    courseId: Int,
    onBack: () -> Unit,
    viewModel: TeacherViewModel = hiltViewModel()
) {
    // Observe UI state from the ViewModel.
    val students by viewModel.studentsForCourse.collectAsState()
    val course by viewModel.currentCourse.collectAsState()
    val teacher by viewModel.currentTeacher.collectAsState()

    // Set up a scroll behavior for the top app bar to collapse on scroll.
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // When the screen is first composed or the courseId changes, trigger the ViewModel to load the students.
    LaunchedEffect(courseId) {
        viewModel.loadStudentsForCourse(courseId)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Column {
                        Text(text = course?.nameCourse ?: "Enrolled Students")
                        teacher?.let {
                            Text(
                                text = "Taught by ${it.firstName} ${it.lastName}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        // Display the list of students in a LazyColumn for better performance.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(students) { studentWithScore ->
                StudentGradeRow(studentWithScore, courseId, viewModel)
            }
        }
    }
}

/**
 * A private composable representing a single row in the student grade list.
 * It displays the student's name and a text field to edit their score.
 *
 * @param studentWithScore The data object containing the student's details and their score.
 * @param courseId The ID of the current course.
 * @param viewModel The ViewModel instance to call when updating the grade.
 */
@Composable
private fun StudentGradeRow(
    studentWithScore: StudentWithScore,
    courseId: Int,
    viewModel: TeacherViewModel
) {
    // State for the score text field. It's initialized with the student's current score.
    var score by remember(studentWithScore.score) {
        mutableStateOf(studentWithScore.score?.toString() ?: "")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${studentWithScore.firstName} ${studentWithScore.lastName}",
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = score,
            onValueChange = { score = it },
            label = { Text("Score") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.width(100.dp)
        )
        // The save button is only enabled if the score is a valid number.
        Button(
            onClick = { score.toFloatOrNull()?.let { viewModel.updateScore(studentWithScore.idStudent, courseId, it) } },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text("Save")
        }
    }
}
