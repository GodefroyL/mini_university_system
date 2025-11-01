package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.local.model.StudentEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Composable screen for creating or editing a student record.
 *
 * @param viewModel The ViewModel handling student data operations.
 * @param onSaved Callback triggered after successfully saving a student.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFormScreen(
    viewModel: StudentListViewModel = hiltViewModel(),
    onSaved: () -> Unit = {}
) {
    /** Randomly generated ID for new students. */
    var id by remember { mutableStateOf((0..10000).random()) }

    /** State for the student's last name input. */
    var lastName by remember { mutableStateOf("") }

    /** State for the student's first name input. */
    var firstName by remember { mutableStateOf("") }

    /** State for email */
    var email by remember { mutableStateOf("") }

    /** State for password */
    var password by remember { mutableStateOf("") }

    /** State for level */
    var levelCode by remember { mutableStateOf(LevelCourse.B1) }

    /** State for the student's date of birth input (format: yyyy-MM-dd). */
    var dobText by remember { mutableStateOf("2000-01-01") }

    /** State for the student's gender selection. */
    var gender by remember { mutableStateOf(Gender.NotConcerned) }

    /** Date formatter for parsing the date of birth string. */
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        /**
         * Text field for entering the student's last name.
         */
        TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
        Spacer(Modifier.height(8.dp))

        /**
         * Text field for entering the student's first name.
         */
        TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
        Spacer(Modifier.height(8.dp))

        /**
         * Text field for email
         */
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))

        /**
         * Text field for password
         */
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(8.dp))

        /**
         * Text field for entering the student's date of birth.
         * Expected format: yyyy-MM-dd
         */
        TextField(value = dobText, onValueChange = { dobText = it }, label = { Text("Date of birth (yyyy-MM-dd)") })
        Spacer(Modifier.height(8.dp))

        /**
         * Dropdown for level
         */
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = levelCode.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Level") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                LevelCourse.entries.forEach { level ->
                    DropdownMenuItem(
                        text = { Text(level.value) },
                        onClick = {
                            levelCode = level
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))

        /**
         * Row of buttons for gender selection.
         * Options: Male, Female, NotConcerned
         */
        Row {
            listOf(Gender.Male, Gender.Female, Gender.NotConcerned).forEach { g ->
                Button(onClick = { gender = g }, modifier = Modifier.padding(end = 8.dp)) {
                    Text(g.value)
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        /**
         * Save button that creates a StudentEntity and inserts it via ViewModel.
         * Triggers the onSaved callback after insertion.
         */
        Button(onClick = {
            val dob = dateFormat.parse(dobText) ?: Date()
            val student = StudentEntity(
                idStudent = id,
                lastName = lastName,
                firstName = firstName,
                email = email,
                password = password, // In a real app, hash this
                dateOfBirth = dob.time,
                gender = gender,
                levelCode = levelCode.name
            )
            viewModel.insertStudent(student)
            onSaved()
        }) {
            Text("Save")
        }
    }
}
