package com.example.movieappdemo1.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieappdemo1.presentation.ui.screen.home.HomeScreen
import com.example.movieappdemo1.presentation.ui.screen.intro.IntroScreen

/**
 * Navigation Component
 *  -> Nav Controller
 *      -> Nav Host
 *          -> Nav Graph
 * */

val intro = "intro"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppNavigationScreen.IntroScreen.name) {
        /* IntroScreen */
        composable(AppNavigationScreen.IntroScreen.name) {
            IntroScreen(navController, hiltViewModel())
        }

        /* HomeScreen */
        composable(
            AppNavigationScreen.HomeScreen.name + "/{${intro}}",
            arguments = listOf(
                navArgument(name = intro) {type = NavType.StringType}
            )
        ) {backStackEntry ->
            HomeScreen(navController = navController, hiltViewModel(), backStackEntry.arguments?.getString(intro))
        }
    }
}