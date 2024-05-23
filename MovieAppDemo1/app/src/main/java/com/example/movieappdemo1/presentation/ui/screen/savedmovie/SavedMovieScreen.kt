package com.example.movieappdemo1.presentation.ui.screen.savedmovie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
fun SavedMovieScreenPreview() {
    val navController = rememberNavController()
    SavedMovieScreen(navController, hiltViewModel())
}

@Composable
fun SavedMovieScreen(
    navController: NavController,
    savedMovieScreen: SavedMovieScreenViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SavedMovieScreen ${this.hashCode()} ${(1..100).random()}",
        )
    }
}