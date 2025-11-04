package com.tumme.scrudstudents.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onRegisterSuccess: (LoggedInUser) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    val availableLevels by viewModel.availableLevels.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(Gender.Male) }
    var genderMenuExpanded by remember { mutableStateOf(false) }
    var levelMenuExpanded by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onRegisterSuccess((authState as AuthState.Authenticated).user)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Register", style = MaterialTheme.typography.headlineLarge)

            TabRow(selectedTabIndex = selectedRole.ordinal) {
                Tab(selected = selectedRole == UserRole.STUDENT, onClick = { selectedRole = UserRole.STUDENT }) {
                    Text("Student", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = selectedRole == UserRole.TEACHER, onClick = { selectedRole = UserRole.TEACHER }) {
                    Text("Teacher", modifier = Modifier.padding(16.dp))
                }
            }

            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())

            // Gender is common to both roles
            ExposedDropdownMenuBox(expanded = genderMenuExpanded, onExpandedChange = { genderMenuExpanded = !genderMenuExpanded }) {
                OutlinedTextField(
                    value = selectedGender.value,
                    onValueChange = {},
                    label = { Text("Gender") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderMenuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = genderMenuExpanded, onDismissRequest = { genderMenuExpanded = false }) {
                    Gender.values().forEach { gender ->
                        DropdownMenuItem(
                            text = { Text(gender.value) },
                            onClick = {
                                selectedGender = gender
                                genderMenuExpanded = false
                            }
                        )
                    }
                }
            }

            if (selectedRole == UserRole.STUDENT) {
                // Fields specific to Students
                ExposedDropdownMenuBox(expanded = levelMenuExpanded, onExpandedChange = { levelMenuExpanded = !levelMenuExpanded }) {
                    OutlinedTextField(
                        value = selectedLevel,
                        onValueChange = {},
                        label = { Text("Level") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelMenuExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = levelMenuExpanded, onDismissRequest = { levelMenuExpanded = false }) {
                        availableLevels.forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level) },
                                onClick = {
                                    selectedLevel = level
                                    levelMenuExpanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    label = { Text("Date of Birth (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = {
                    if (selectedRole == UserRole.STUDENT) {
                        val student = StudentEntity(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password,
                            levelCode = selectedLevel,
                            dateOfBirth = dateOfBirth,
                            gender = selectedGender
                        )
                        viewModel.registerStudent(student)
                    } else {
                        val teacher = TeacherEntity(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password,
                            gender = selectedGender // Fix: Added missing gender parameter
                        )
                        viewModel.registerTeacher(teacher)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = authState !is AuthState.Loading
            ) {
                Text("Register")
            }

            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Login")
            }
        }
    }
}
