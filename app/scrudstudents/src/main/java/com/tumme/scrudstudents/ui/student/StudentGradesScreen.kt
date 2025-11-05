package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.DecimalFormat

/**
 * A screen that displays a student's grades for all their enrolled courses.
 * It also shows the calculated weighted average grade.
 *
 * @param studentId The ID of the student whose grades are to be displayed.
 * @param viewModel The ViewModel that provides the data for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentGradesScreen(
    studentId: Int,
    studentLevel: String, // Note: studentLevel is passed but not used here, could be removed.
    viewModel: StudentSubscriptionsViewModel = hiltViewModel()
) {
    // Observe the list of student's subscriptions (which include grades) from the ViewModel.
    val subscriptions by viewModel.subscriptions.collectAsState()
    // Observe the calculated weighted average from the ViewModel.
    val weightedAverage by viewModel.weightedAverage.collectAsState()
    // Formatter for displaying the average grade neatly.
    val averageFormatter = remember { DecimalFormat("#.##") }

    // When the screen is first composed or the studentId changes, trigger the ViewModel to load the data.
    LaunchedEffect(studentId) {
        viewModel.loadStudentData(studentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Grades") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Table Header
            Row(Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
                TableCell(text = "Course", weight = .6f, fontWeight = FontWeight.Bold)
                TableCell(text = "Grade", weight = .4f, fontWeight = FontWeight.Bold)
            }

            // Table Body: Iterate through each subscription to display the grade.
            subscriptions.forEach { subscription ->
                Row {
                    TableCell(text = subscription.course.nameCourse, weight = .6f)
                    // Display "N/A" if the course has not been graded yet (score is 0 or less).
                    val scoreText = if (subscription.subscribe.score > 0) {
                        subscription.subscribe.score.toString()
                    } else {
                        "N/A"
                    }
                    TableCell(text = scoreText, weight = .4f)
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Pushes the footer to the bottom

            // Footer displaying the overall weighted average.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Overall Average: ${averageFormatter.format(weightedAverage)}/20",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

/**
 * A helper composable to create a single cell in a table-like structure.
 * It takes care of borders, padding, and text styling.
 * @param text The text to display in the cell.
 * @param weight The column weight for sizing.
 * @param fontWeight The font weight for the text.
 */
@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        modifier = Modifier
            .border(1.dp, Color.Gray)
            .weight(weight)
            .padding(8.dp),
        fontWeight = fontWeight
    )
}
