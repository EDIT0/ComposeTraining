package com.example.movieappdemo1.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movieappdemo1.presentation.ui.screen.home.BottomNavItem
import com.example.movieappdemo1.presentation.ui.screen.home.HomeScreenViewModelPresenter
import com.example.movieappdemo1.presentation.ui.screen.movielist.MovieListScreen
import com.example.movieappdemo1.presentation.ui.screen.savedmovie.SavedMovieScreen
import com.example.movieappdemo1.presentation.ui.screen.searchmovie.SearchMovieScreen

@Composable
fun MainBottomNavigation(
    bottomNavHostController: NavHostController,
    navController: NavController,
    homeScreenViewModelPresenter: (HomeScreenViewModelPresenter) -> Unit
) {
    NavHost(navController = bottomNavHostController, startDestination = BottomNavItem.MovieList.screenRoute) {
        /* MovieListScreen */
        composable(BottomNavItem.MovieList.screenRoute) {
            MovieListScreen(bottomNavHostController, navController, hiltViewModel(), homeScreenViewModelPresenter)
        }

        /* SearchMovieScreen */
        composable(BottomNavItem.SearchMovie.screenRoute) {
            SearchMovieScreen(bottomNavHostController, navController, hiltViewModel(), homeScreenViewModelPresenter)
        }

        /* SavedMovieScreen */
        composable(BottomNavItem.SavedMovie.screenRoute) {
            SavedMovieScreen(bottomNavHostController, navController, hiltViewModel())
        }
    }
}