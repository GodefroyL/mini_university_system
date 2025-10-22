package com.tumme.scrudstudents.ui.student

import com.tumme.scrudstudents.ui.components.TableHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    /**
     * ViewModel for this screen, injected by Hilt.
     */
    viewModel: StudentListViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {}
) {
    /** Collects the list of students as state for Compose to observe.*/
    val students by viewModel.students.collectAsState()

    /**
     * Coroutine scope for the ViewModel.
     * It is used to launch coroutines from the ViewModel.
     */
    val coroutineScope = rememberCoroutineScope()

    // Scaffold provides a basic layout structure with a top bar and FAB.
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Students") })
        },
        floatingActionButton = {
            /** FAB to navigate to the student form. */
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
            /** Displays the table header with column titles.*/
            TableHeader(cells = listOf("DOB", "Last", "First", "Gender", "Actions"),
                weights = listOf(0.25f, 0.25f, 0.25f, 0.15f, 0.10f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            /** LazyColumn for efficient rendering of the student list.*/
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(students) { student ->
                    /** Displays each student as a row with actions.*/
                    StudentRow(
                        student = student,
                        onEdit = { /* navigate to form prefilled (not implemented here) */ },
                        onDelete = { viewModel.deleteStudent(student) },
                        onView = { onNavigateToDetail(student.idStudent) },
                        onShare = { /* share intent */ }
                    )
                }
            }
        }
    }
}
