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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentGradesScreen(
    studentId: Int,
    studentLevel: String,
    viewModel: StudentSubscriptionsViewModel = hiltViewModel()
) {
    val subscriptions by viewModel.subscriptions.collectAsState()
    val weightedAverage by viewModel.weightedAverage.collectAsState()
    val averageFormatter = remember { DecimalFormat("#.##") }

    // Explicitly load data when the screen is composed with a valid studentId
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
            // Header
            Row(Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
                TableCell(text = "Course", weight = .6f, fontWeight = FontWeight.Bold)
                TableCell(text = "Grade", weight = .4f, fontWeight = FontWeight.Bold)
            }

            // Grades Table
            subscriptions.forEach { subscription ->
                Row {
                    TableCell(text = subscription.course.nameCourse, weight = .6f)
                    val scoreText = if (subscription.subscribe.score > 0) {
                        subscription.subscribe.score.toString()
                    } else {
                        "N/A"
                    }
                    TableCell(text = scoreText, weight = .4f)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer with Weighted Average
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
