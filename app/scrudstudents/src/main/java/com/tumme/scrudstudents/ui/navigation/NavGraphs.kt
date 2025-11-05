package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tumme.scrudstudents.ui.auth.LoggedInUser
import com.tumme.scrudstudents.ui.auth.LoginScreen
import com.tumme.scrudstudents.ui.auth.RegisterScreen
import com.tumme.scrudstudents.ui.auth.UserRole
import com.tumme.scrudstudents.ui.course.CourseFormScreen
import com.tumme.scrudstudents.ui.student.StudentHomeScreen
import com.tumme.scrudstudents.ui.teacher.TeacherCourseDetailScreen
import com.tumme.scrudstudents.ui.teacher.TeacherHomeScreen

// --- Navigation Routes ---
object Routes {
    // Auth
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Graph Roots with arguments
    const val STUDENT_GRAPH = "student_graph/{studentId}/{studentLevel}"
    const val TEACHER_GRAPH = "teacher_graph/{teacherId}"

    // Screen routes (simplified)
    const val STUDENT_HOME = "student_home"
    const val TEACHER_HOME = "teacher_home"
    const val DECLARE_COURSE = "declare_course"
    const val TEACHER_COURSE_DETAIL = "teacher_course_detail/{courseId}"
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = Routes.LOGIN, modifier = modifier) {
        // --- Authentication Flow ---
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { user: LoggedInUser ->
                    val route = when (user.role) {
                        UserRole.STUDENT -> Routes.STUDENT_GRAPH
                            .replace("{studentId}", user.id.toString())
                            .replace("{studentLevel}", user.level!!)
                        UserRole.TEACHER -> Routes.TEACHER_GRAPH
                            .replace("{teacherId}", user.id.toString())
                    }
                    navController.navigate(route) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { user: LoggedInUser ->
                    val route = when (user.role) {
                        UserRole.STUDENT -> Routes.STUDENT_GRAPH
                            .replace("{studentId}", user.id.toString())
                            .replace("{studentLevel}", user.level!!)
                        UserRole.TEACHER -> Routes.TEACHER_GRAPH
                            .replace("{teacherId}", user.id.toString())
                    }
                    navController.navigate(route) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // --- Role-based Sub-graphs ---
        studentGraph(navController)
        teacherGraph(navController)
    }
}

private fun NavGraphBuilder.studentGraph(navController: NavHostController) {
    navigation(
        startDestination = Routes.STUDENT_HOME,
        route = Routes.STUDENT_GRAPH
    ) {
        composable(Routes.STUDENT_HOME) {
            val parentEntry = remember(it) { navController.getBackStackEntry(Routes.STUDENT_GRAPH) }
            val studentId = parentEntry.arguments?.getInt("studentId") ?: 0
            val studentLevel = parentEntry.arguments?.getString("studentLevel") ?: ""
            StudentHomeScreen(
                studentId = studentId,
                studentLevel = studentLevel,
                navController = navController
            )
        }
    }
}

private fun NavGraphBuilder.teacherGraph(navController: NavHostController) {
    navigation(
        startDestination = Routes.TEACHER_HOME,
        route = Routes.TEACHER_GRAPH
    ) {
        composable(Routes.TEACHER_HOME) {
            val parentEntry = remember(it) { navController.getBackStackEntry(Routes.TEACHER_GRAPH) }
            val teacherId = parentEntry.arguments?.getInt("teacherId") ?: 0
            TeacherHomeScreen(
                teacherId = teacherId,
                navController = navController,
                onDeclareCourse = { navController.navigate(Routes.DECLARE_COURSE) },
                onCourseClick = { courseId -> navController.navigate(Routes.TEACHER_COURSE_DETAIL.replace("{courseId}", courseId.toString())) }
            )
        }
        composable(Routes.DECLARE_COURSE) {
            CourseFormScreen(onSaved = { navController.popBackStack() })
        }
        composable(
            route = Routes.TEACHER_COURSE_DETAIL,
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) {
            TeacherCourseDetailScreen()
        }
    }
}
