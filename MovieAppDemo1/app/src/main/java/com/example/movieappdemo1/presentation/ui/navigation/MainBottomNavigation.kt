package com.example.movieappdemo1.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movieappdemo1.common.constant.MOVIE_LIST
import com.example.movieappdemo1.common.constant.SAVED_MOVIE
import com.example.movieappdemo1.common.constant.SEARCH_MOVIE
import com.example.movieappdemo1.presentation.ui.screen.home.BottomNavItem
import com.example.movieappdemo1.presentation.ui.screen.movielist.MovieListScreen
import com.example.movieappdemo1.presentation.ui.screen.savedmovie.SavedMovieScreen
import com.example.movieappdemo1.presentation.ui.screen.searchmovie.SearchMovieScreen

@Composable
fun MainBottomNavigation(
    navHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = BottomNavItem.MovieList.screenRoute) {
        /* MovieListScreen */
        composable(BottomNavItem.MovieList.screenRoute) {
            MovieListScreen()
        }

        /* SearchMovieScreen */
        composable(BottomNavItem.SearchMovie.screenRoute) {
            SearchMovieScreen()
        }

        /* SavedMovieScreen */
        composable(BottomNavItem.SavedMovie.screenRoute) {
            SavedMovieScreen()
        }
    }
}