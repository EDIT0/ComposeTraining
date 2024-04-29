package com.example.movieapp.screens.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.screens.home.MainContent

@Preview
@Composable
fun PreviewDetailScreen() {
    val navController = rememberNavController()
    DetailsScreen(navController = navController, movieData = "MovieData")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    movieData: String?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Movies")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.LightGray),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(it)
        ) {
            Content(navController, movieData)
        }
    }
}

@Composable
fun Content(
    navController: NavController,
    movieData: String?
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Justify,
                text = movieData.toString(),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(23.dp))
            Button(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text(text = "Go Back")
            }
        }
    }
}