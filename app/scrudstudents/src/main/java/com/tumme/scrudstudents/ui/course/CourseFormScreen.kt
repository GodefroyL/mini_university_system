package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.local.model.TeacherEntity

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
    val teachers by viewModel.teachers.collectAsState()

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

    /** State for the selected teacher. */
    var selectedTeacher by remember { mutableStateOf<TeacherEntity?>(null) }

    /**
     * Column layout for the form.
     */
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
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
         * Text field for entering the course's description.
         */
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        Spacer(Modifier.height(8.dp))

        /**
         * Dropdown menu for selecting the teacher.
         */
        var teacherExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = teacherExpanded, onExpandedChange = { teacherExpanded = !teacherExpanded }) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedTeacher?.let { "${it.firstName} ${it.lastName}" } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Teacher") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = teacherExpanded) }
            )
            ExposedDropdownMenu(expanded = teacherExpanded, onDismissRequest = { teacherExpanded = false }) {
                teachers.forEach { teacher ->
                    DropdownMenuItem(
                        text = { Text("${teacher.firstName} ${teacher.lastName}") },
                        onClick = {
                            selectedTeacher = teacher
                            teacherExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))

        /**
         * Dropdown menu for selecting the course's level.
         */
        var levelExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = levelExpanded, onExpandedChange = { levelExpanded = !levelExpanded }) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = levelCode.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Level of the course") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelExpanded) }
            )
            ExposedDropdownMenu(expanded = levelExpanded, onDismissRequest = { levelExpanded = false }) {
                LevelCourse.entries.forEach { levelOption ->
                    DropdownMenuItem(
                        text = { Text(levelOption.value) },
                        onClick = {
                            levelCode = levelOption
                            levelExpanded = false
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
                teacherId = selectedTeacher?.teacherId,
                description = description
            )
            viewModel.insertCourse(course)
            onSaved()
        }) {
            Text("Save")
        }
    }
}
