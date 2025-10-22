package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity

/**
 * Composable screen for displaying detailed information about a course.
 *
 * @param courseId The ID of the course to display
 * @param viewModel The ViewModel handling course data operations
 * @param onBack Callback triggered when the user wants to navigate back
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    courseId: Int,
    viewModel: CourseListViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    /** State for storing the course data */
    var course by remember { mutableStateOf<CourseEntity?>(null) }

    /**
     * LaunchedEffect to fetch course data when the screen is first composed
     * or when the courseId changes
     */
    LaunchedEffect(courseId) {
        course = viewModel.findCourse(courseId)
    }

    /** Scaffold providing a top app bar and content layout */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Course details") },
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
            /** Loading state while fetching course data */
            if (course == null) {
                Text("Loading...")
            } else {
                /** Display course details */
                Text("ID: ${course!!.idCourse}")
                Text("Name: ${course!!.nameCourse}")
                Text("ECTS: ${course!!.ectsCourse}")
                Text("Level: ${course!!.levelCourse}")
            }
        }
    }
}
