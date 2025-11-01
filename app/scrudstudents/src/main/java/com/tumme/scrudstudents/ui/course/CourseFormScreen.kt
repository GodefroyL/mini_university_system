package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.LevelCourse

/**
 * Composable screen for creating or editing a course record.
 *
 * @param viewModel The ViewModel handling course data operations.
 * @param onSaved Callback triggered after successfully saving a course.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseFormScreen(
    viewModel: CourseListViewModel = hiltViewModel(),
    onSaved: () -> Unit = {}
) {
    /** Randomly generated ID for new course. */
    var id by remember { mutableStateOf((0..10000).random()) }

    /** State for the course's name input. */
    var nameCourse by remember { mutableStateOf("") }

    /** State for the course's ECTS input (as String for TextField). */
    var ectsCourseText by remember { mutableStateOf("0") }

    /** State for the course's level input. */
    var levelCode by remember { mutableStateOf(LevelCourse.B1) }

    /** State for the course's description input. */
    var description by remember { mutableStateOf("") }

    /** State for the course's teacher input. */
    var teacher by remember { mutableStateOf("") }

    /**
     * Column layout for the form.
     */
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        /**
         * Text field for entering the course's name.
         */
        TextField(
            value = nameCourse,
            onValueChange = { nameCourse = it },
            label = { Text("Course Name") }
        )
        Spacer(Modifier.height(8.dp))

        /**
         * Text field for entering the course's ECTS points.
         */
        TextField(
            value = ectsCourseText,
            onValueChange = { ectsCourseText = it },
            label = { Text("ECTS points for this course") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(Modifier.height(8.dp))

        /**
         * Dropdown menu for selecting the course's level.
         */
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = levelCode.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Level of the course") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                LevelCourse.entries.forEach { levelOption ->
                    DropdownMenuItem(
                        text = { Text(levelOption.value) },
                        onClick = {
                            levelCode = levelOption
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        /**
         * Save button that creates a CourseEntity and inserts it via ViewModel.
         * Triggers the onSaved callback after insertion.
         */
        Button(onClick = {
            val ectsCourse = ectsCourseText.toFloatOrNull() ?: 0f
            val course = CourseEntity(
                idCourse = id,
                nameCourse = nameCourse,
                ectsCourse = ectsCourse,
                levelCode = levelCode.name,
                teacherId = teacher.toIntOrNull(),
                description = description
            )
            viewModel.insertCourse(course)
            onSaved()
        }) {
            Text("Save")
        }
    }
}
