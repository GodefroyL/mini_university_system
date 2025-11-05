package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun StudentSubscriptionsScreen(
    studentId: Int, // Passed from navigation
    viewModel: StudentSubscriptionsViewModel = hiltViewModel()
) {
    val subscriptions by viewModel.subscriptions.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(subscriptions) { subscription ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = subscription.course.nameCourse, style = MaterialTheme.typography.titleMedium)
                    Text(text = "ECTS: ${subscription.course.ectsCourse}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    val scoreText = if (subscription.subscribe.score > 0) {
                        "Grade: ${subscription.subscribe.score}/20"
                    } else {
                        "Not graded yet"
                    }
                    Text(text = scoreText, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
