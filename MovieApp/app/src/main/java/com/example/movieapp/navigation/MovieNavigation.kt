package com.example.movieapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieapp.screens.detail.DetailsScreen
import com.example.movieapp.screens.home.HomeScreen

/**
 * Navigation Component
 *  -> Nav Controller
 *      -> Nav Host
 *          -> Nav Graph
 * */

val intentMovie = "movie"

@Composable
fun MovieNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MovieScreens.HomeScreen.name) {
        composable(
            MovieScreens.HomeScreen.name
        ) {
            HomeScreen(navController = navController)
        }
        composable(
            MovieScreens.DetailsScreen.name + "/{${intentMovie}}",
            arguments = listOf(
                navArgument(name = intentMovie) {type = NavType.StringType}
            )
        ) {backStackEntry ->
            DetailsScreen(navController = navController, backStackEntry.arguments?.getString(intentMovie))
        }
    }
}