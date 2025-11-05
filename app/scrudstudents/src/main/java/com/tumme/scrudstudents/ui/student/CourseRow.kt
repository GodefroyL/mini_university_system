package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tumme.scrudstudents.data.local.model.CourseEntity

@Composable
fun CourseRow(
    course: CourseEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onView: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onView)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = course.nameCourse, modifier = Modifier.weight(0.4f))
        Text(text = course.levelCode, modifier = Modifier.weight(0.3f))
        Text(text = course.ectsCourse.toString(), modifier = Modifier.weight(0.2f))
        Row(modifier = Modifier.weight(0.1f)) {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
            IconButton(onClick = onShare) {
                Icon(Icons.Default.Share, contentDescription = "Share")
            }
        }
    }
}
