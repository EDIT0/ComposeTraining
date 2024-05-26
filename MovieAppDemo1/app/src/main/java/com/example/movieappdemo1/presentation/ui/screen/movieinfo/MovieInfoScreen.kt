package com.example.movieappdemo1.presentation.ui.screen.movieinfo

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.movieappdemo1.domain.model.MovieModelResult

@Preview
@Composable
fun MovieInfoScreenPreview() {
    val navController = rememberNavController()
    val movieInfoScreenViewModel = MovieInfoScreenViewModel()
    MovieInfoScreen(
        navController = navController,
        movieInfoScreenViewModel = movieInfoScreenViewModel,
    )
}

@Composable
fun MovieInfoScreen(
    navController: NavController,
    movieInfoScreenViewModel: MovieInfoScreenViewModel
) {

}