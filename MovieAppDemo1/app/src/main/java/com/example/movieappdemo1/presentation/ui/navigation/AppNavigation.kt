package com.example.movieappdemo1.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.presentation.ui.screen.home.HomeScreen
import com.example.movieappdemo1.presentation.ui.screen.intro.IntroScreen
import com.example.movieappdemo1.presentation.ui.screen.movieinfo.MovieInfoScreen

/**
 * Navigation Component
 *  -> Nav Controller
 *      -> Nav Host
 *          -> Nav Graph
 * */

/* Intent Value */
val intro = "intro"
val movie_info = "movie_info"

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

        /* MovieInfoScreen */
        composable(
            AppNavigationScreen.MovieInfoScreen.name,
//            arguments = listOf(
//                navArgument(name = movie_info) {type = NavType.StringType}
//            )
        ) {backStackEntry ->
//            val movieModelResult = it.arguments?.getSerializable(movie_info) as MovieModelResult
            MovieInfoScreen(navController = navController, movieInfoScreenViewModel = hiltViewModel())
        }
    }
}