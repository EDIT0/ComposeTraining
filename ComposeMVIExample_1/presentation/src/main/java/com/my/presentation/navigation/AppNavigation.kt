package com.my.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.my.common.IntentConstant
import com.my.presentation.screen.moviedetail.MovieDetailScreen
import com.my.presentation.screen.movielist.MovieListScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppNavigationScreen.MovieListScreen.name) {
        /* MovieListScreen */
        composable(AppNavigationScreen.MovieListScreen.name) {
            MovieListScreen(navController)
        }

        /* MovieDetailScreen */
        composable(
            AppNavigationScreen.MovieDetailScreen.name + "/{${IntentConstant.MOVIE_INFO}}",
            arguments = listOf(
                navArgument(name = IntentConstant.MOVIE_INFO) {type = NavType.StringType}
            )
        ) { backStackEntry ->
            val movieModelResultJson = backStackEntry.arguments?.getString(IntentConstant.MOVIE_INFO)
            MovieDetailScreen(navController = navController, movieModelResultString = movieModelResultJson?:"")
        }
    }

}