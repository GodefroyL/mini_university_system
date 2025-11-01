package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tumme.scrudstudents.ui.course.CourseDetailScreen
import com.tumme.scrudstudents.ui.course.CourseFormScreen
import com.tumme.scrudstudents.ui.course.CourseListScreen
import com.tumme.scrudstudents.ui.student.StudentDetailScreen
import com.tumme.scrudstudents.ui.student.StudentFormScreen
import com.tumme.scrudstudents.ui.student.StudentListScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeFormScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeListScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeViewModel

/**
 * Navigation routes for the application.
 * Defines all the navigation destinations as constants.
 */
object Routes {
    // Student routes
    /** Route for the student list screen */
    const val STUDENT_LIST = "student_list"

    /** Route for the student form screen */
    const val STUDENT_FORM = "student_form"

    /** Route for the student detail screen with student ID parameter */
    const val STUDENT_DETAIL = "student_detail/{studentId}"

    // Course routes
    /** Route for the course list screen */
    const val COURSE_LIST = "course_list"

    /** Route for the course form screen */
    const val COURSE_FORM = "course_form"

    /** Route for the course detail screen with course ID parameter */
    const val COURSE_DETAIL = "course_detail/{courseId}"

    // Subscription routes
    /** Route for the subscription list screen */
    const val SUBSCRIBE_LIST = "subscribe_list"

    /** Route for the subscription form screen */
    const val SUBSCRIBE_FORM = "subscribe_form"
}

/**
 * Main navigation host for the application.
 * Sets up the navigation graph and handles navigation between screens.
 */
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    /** Navigation graph with all application routes */
    NavHost(navController, startDestination = Routes.STUDENT_LIST, modifier = modifier) {
        // Student destinations
        composable(Routes.STUDENT_LIST) {
            StudentListScreen(
                onNavigateToForm = { navController.navigate(Routes.STUDENT_FORM) },
                onNavigateToDetail = { id -> navController.navigate("student_detail/$id") }
            )
        }

        composable(Routes.STUDENT_FORM) {
            StudentFormScreen(onSaved = { navController.popBackStack() })
        }

        composable(
            route = "student_detail/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("studentId") ?: 0
            StudentDetailScreen(
                studentId = id,
                onBack = { navController.popBackStack() })
        }

        // Course destinations
        composable(Routes.COURSE_LIST) {
            CourseListScreen(
                onNavigateToForm = { navController.navigate(Routes.COURSE_FORM) },
                onNavigateToDetail = { id -> navController.navigate("course_detail/$id") }
            )
        }

        composable(Routes.COURSE_FORM) {
            CourseFormScreen(onSaved = { navController.popBackStack() })
        }

        composable(
            route = "course_detail/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("courseId") ?: 0
            CourseDetailScreen(
                courseId = id,
                onBack = { navController.popBackStack() })
        }

        // Subscription destinations
        composable(Routes.SUBSCRIBE_LIST) {
            SubscribeListScreen(navController = navController)
        }

        composable(Routes.SUBSCRIBE_FORM) {
            val viewModel: SubscribeViewModel = hiltViewModel()
            SubscribeFormScreen(
                viewModel = viewModel,
                onSaved = { navController.popBackStack() })
        }
    }
}
