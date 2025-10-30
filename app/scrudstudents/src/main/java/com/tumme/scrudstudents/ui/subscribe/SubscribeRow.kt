package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubscribeRow(
    studentName: String,
    courseName: String,
    score: Float,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = studentName, modifier = Modifier.weight(0.4f))
        Text(text = courseName, modifier = Modifier.weight(0.4f))
        Text(text = "%.2f".format(score), modifier = Modifier.weight(0.1f))
        IconButton(onClick = onDelete, modifier = Modifier.weight(0.1f)) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Subscription")
        }
    }
    Divider()
}
