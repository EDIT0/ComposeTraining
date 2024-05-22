package com.example.movieappdemo1.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieappdemo1.common.constant.MOVIE_LIST
import com.example.movieappdemo1.common.constant.SAVED_MOVIE
import com.example.movieappdemo1.common.constant.SEARCH_MOVIE
import com.example.movieappdemo1.presentation.ui.screen.home.HomeScreen
import com.example.movieappdemo1.presentation.ui.screen.home.HomeScreenViewModel
import com.example.movieappdemo1.presentation.ui.screen.intro.IntroScreen
import com.example.movieappdemo1.presentation.ui.screen.intro.IntroScreenViewModel
import com.example.movieappdemo1.presentation.ui.screen.movielist.MovieListScreen
import com.example.movieappdemo1.presentation.ui.screen.savedmovie.SavedMovieScreen
import com.example.movieappdemo1.presentation.ui.screen.searchmovie.SearchMovieScreen

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
    NavHost(navController = navController, startDestination = NavScreen.IntroScreen.name) {
        /* IntroScreen */
        composable(NavScreen.IntroScreen.name) {
            IntroScreen(navController, hiltViewModel())
        }

        /* HomeScreen */
        composable(
            NavScreen.HomeScreen.name + "/{${intro}}",
            arguments = listOf(
                navArgument(name = intro) {type = NavType.StringType}
            )
        ) {backStackEntry ->
            HomeScreen(navController = navController, hiltViewModel(), backStackEntry.arguments?.getString(intro))
        }

//        /* MovieListScreen */
//        composable(NavScreen.MovieListScreen.name + "/$MOVIE_LIST") {
//            MovieListScreen()
//        }
//
//        /* SearchMovieScreen */
//        composable(NavScreen.SearchMovieScreen.name + "/$SEARCH_MOVIE") {
//            SearchMovieScreen()
//        }
//
//        /* SavedMovieScreen */
//        composable(NavScreen.SavedMovieScreen.name + "/$SAVED_MOVIE") {
//            SavedMovieScreen()
//        }
    }
}