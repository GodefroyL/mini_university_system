package com.tumme.scrudstudents.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tumme.scrudstudents.ui.auth.LoggedInUser
import com.tumme.scrudstudents.ui.auth.LoginScreen
import com.tumme.scrudstudents.ui.auth.RegisterScreen
import com.tumme.scrudstudents.ui.auth.UserRole
import com.tumme.scrudstudents.ui.course.CourseFormScreen
import com.tumme.scrudstudents.ui.student.*
import com.tumme.scrudstudents.ui.teacher.StudentListByCourseScreen
import com.tumme.scrudstudents.ui.teacher.TeacherCourseDetailScreen
import com.tumme.scrudstudents.ui.teacher.TeacherHomeScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val STUDENT_DASHBOARD = "student_dashboard"
    const val TEACHER_DASHBOARD = "teacher_dashboard"
    const val STUDENT_COURSES = "student_courses"
    const val STUDENT_SUBSCRIPTIONS = "student_subscriptions"
    const val STUDENT_GRADES = "student_grades"
    const val STUDENT_PROFILE = "student_profile"
    const val TEACHER_COURSES = "teacher_courses"
    const val TEACHER_DECLARE_COURSE = "teacher_declare_course"
    const val TEACHER_ENROLLED_STUDENTS = "teacher_enrolled_students"
}

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Routes.LOGIN, modifier = modifier) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { user ->
                    val route = when (user.role) {
                        UserRole.STUDENT -> "${Routes.STUDENT_DASHBOARD}/${user.id}/${user.level}"
                        UserRole.TEACHER -> "${Routes.TEACHER_DASHBOARD}/${user.id}"
                    }
                    navController.navigate(route) { popUpTo(Routes.LOGIN) { inclusive = true } }
                },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { user ->
                    val route = when (user.role) {
                        UserRole.STUDENT -> "${Routes.STUDENT_DASHBOARD}/${user.id}/${user.level}"
                        UserRole.TEACHER -> "${Routes.TEACHER_DASHBOARD}/${user.id}"
                    }
                    navController.navigate(route) { popUpTo(Routes.LOGIN) { inclusive = true } }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.STUDENT_DASHBOARD}/{studentId}/{studentLevel}",
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType },
                navArgument("studentLevel") { type = NavType.StringType }
            )
        ) {
            val studentId = it.arguments?.getInt("studentId") ?: 0
            val studentLevel = it.arguments?.getString("studentLevel") ?: ""
            StudentDashboardScreen(studentId = studentId, studentLevel = studentLevel)
        }

        composable(
            route = "${Routes.TEACHER_DASHBOARD}/{teacherId}",
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) {
            val teacherId = it.arguments?.getInt("teacherId") ?: 0
            TeacherHomeScreen(
                teacherId = teacherId,
                navController = navController,
                onDeclareCourse = { navController.navigate(Routes.TEACHER_DECLARE_COURSE) },
                onCourseClick = { courseId ->
                    navController.navigate("${Routes.TEACHER_ENROLLED_STUDENTS}/$courseId")
                }
            )
        }

        composable(
            route = "${Routes.TEACHER_ENROLLED_STUDENTS}/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) {
            StudentListByCourseScreen()
        }

        composable(Routes.TEACHER_DECLARE_COURSE) {
            CourseFormScreen(onSaved = { navController.popBackStack() })
        }
    }
}

@Composable
private fun StudentDashboardScreen(studentId: Int, studentLevel: String) {
    val studentNavController = rememberNavController()
    Scaffold(bottomBar = { StudentBottomBar(navController = studentNavController) }) {
        StudentDashboardNavHost(studentNavController, it, studentId, studentLevel)
    }
}

@Composable
private fun StudentBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val items = listOf(
        BottomNavItem("Courses", Routes.STUDENT_COURSES, Icons.Default.School),
        BottomNavItem("Subscriptions", Routes.STUDENT_SUBSCRIPTIONS, Icons.Default.List),
        BottomNavItem("Grades", Routes.STUDENT_GRADES, Icons.Default.Assignment),
    )
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun StudentDashboardNavHost(
    navController: NavHostController,
    padding: PaddingValues,
    studentId: Int,
    studentLevel: String
) {
    NavHost(navController = navController, startDestination = Routes.STUDENT_COURSES, modifier = Modifier.padding(padding)) {
        composable(Routes.STUDENT_COURSES) {
            StudentCourseListScreen(levelCode = studentLevel)
        }
        composable(Routes.STUDENT_SUBSCRIPTIONS) {
            StudentSubscriptionsScreen(studentId = studentId)
        }
        composable(Routes.STUDENT_GRADES) {
            StudentGradesScreen(studentId = studentId, studentLevel = studentLevel)
        }
    }
}

private data class BottomNavItem(val title: String, val route: String, val icon: ImageVector)
