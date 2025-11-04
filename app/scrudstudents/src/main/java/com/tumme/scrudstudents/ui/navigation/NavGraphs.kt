package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
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

    // Graph Roots
    const val STUDENT_GRAPH = "student_graph"
    const val TEACHER_GRAPH = "teacher_graph"

    // Student Routes
    const val STUDENT_HOME = "student_home/{studentId}/{studentLevel}"

    // Teacher Routes
    const val TEACHER_HOME = "teacher_home/{teacherId}"
    const val DECLARE_COURSE = "declare_course"
    const val TEACHER_COURSE_DETAIL = "teacher_course_detail/{courseId}"
}

/**
 * The main navigation host for the entire application.
 * Handles authentication and redirects to the appropriate role-based navigation graph.
 */
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = Routes.LOGIN, modifier = modifier) {
        // --- Authentication Flow ---
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { user: LoggedInUser ->
                    val route = when (user.role) {
                        UserRole.STUDENT -> Routes.STUDENT_GRAPH
                        UserRole.TEACHER -> Routes.TEACHER_GRAPH
                    }
                    navController.navigate(route) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
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
                        UserRole.TEACHER -> Routes.TEACHER_GRAPH
                    }
                    navController.navigate(route) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
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

/**
 * Defines the navigation graph for the Student role.
 */
private fun NavGraphBuilder.studentGraph(navController: NavHostController) {
    navigation(
        route = Routes.STUDENT_GRAPH,
        startDestination = Routes.STUDENT_HOME
    ) {
        composable(
            route = Routes.STUDENT_HOME,
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType },
                navArgument("studentLevel") { type = NavType.StringType; nullable = true }
            )
        ) {
            val studentId = it.arguments?.getInt("studentId") ?: 0
            val studentLevel = it.arguments?.getString("studentLevel") ?: ""
            StudentHomeScreen(
                studentId = studentId,
                studentLevel = studentLevel,
                navController = navController
            )
        }
    }
}

/**
 * Defines the navigation graph for the Teacher role.
 */
private fun NavGraphBuilder.teacherGraph(navController: NavHostController) {
    navigation(
        route = Routes.TEACHER_GRAPH,
        startDestination = Routes.TEACHER_HOME
    ) {
        composable(
            route = Routes.TEACHER_HOME,
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) {
            val teacherId = it.arguments?.getInt("teacherId") ?: 0
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
