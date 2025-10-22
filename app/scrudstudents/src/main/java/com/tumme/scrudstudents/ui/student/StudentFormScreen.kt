package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.StudentEntity

/**
 * Composable screen for creating or editing a student record.
 *
 * @param viewModel The ViewModel handling student data operations.
 * @param onSaved Callback triggered after successfully saving a student.
 */
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

    /** State for the student's date of birth input (format: yyyy-MM-dd). */
    var dobText by remember { mutableStateOf("2000-01-01") }

    /** State for the student's gender selection. */
    var gender by remember { mutableStateOf(Gender.NotConcerned) }

    /** Date formatter for parsing the date of birth string. */
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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
         * Text field for entering the student's date of birth.
         * Expected format: yyyy-MM-dd
         */
        TextField(value = dobText, onValueChange = { dobText = it }, label = { Text("Date of birth (yyyy-MM-dd)") })
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
                dateOfBirth = dob,
                gender = gender
            )
            viewModel.insertStudent(student)
            onSaved()
        }) {
            Text("Save")
        }
    }
}
