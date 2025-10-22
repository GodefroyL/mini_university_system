package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeListScreen(
    navController: NavController,
    viewModel: SubscribeViewModel = hiltViewModel()
) {
    val subscribes by viewModel.subscribes.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Subscriptions") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.SUBSCRIBE_FORM) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Subscription")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(subscribes) { subscribe ->
                SubscribeRow(
                    subscribe = subscribe,
                    onEdit = { /* Navigate to form with pre-filled data */ },
                    onDelete = { viewModel.deleteSubscribe(subscribe) }
                )
            }
        }
    }
}

@Composable
fun SubscribeRow(
    subscribe: SubscribeEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Student ID: ${subscribe.studentId}", style = MaterialTheme.typography.bodyLarge)
                Text("Course ID: ${subscribe.courseId}", style = MaterialTheme.typography.bodyMedium)
                Text("Score: ${subscribe.score}", style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
