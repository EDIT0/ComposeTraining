package com.example.movieappdemo1.presentation.ui.screen.searchmovie

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
fun SearchMovieScreenPreview() {
    val bottomNavController = rememberNavController()
    SearchMovieScreen(bottomNavController, hiltViewModel())
}

@Composable
fun SearchMovieScreen(
    bottomNavController: NavController,
    searchMovieScreenViewModel: SearchMovieScreenViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SearchMovieScreen ${this.hashCode()} ${(1..100).random()}",
        )
    }
}