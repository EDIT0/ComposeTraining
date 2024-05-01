package com.example.movieapp.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.movieapp.model.Movie
import com.example.movieapp.model.getMovies
import com.example.movieapp.widgets.MovieRow

@Preview
@Composable
fun PreviewDetailScreen() {
    val navController = rememberNavController()
    DetailsScreen(navController = navController, movieId = "MovieData")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    movieId: String?
) {

    val newMovieList: List<Movie> = getMovies()
        .filter {
            it.id == movieId
        }



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
            Content(navController, newMovieList)
        }
    }
}

@Composable
fun Content(
    navController: NavController,
    newMovieList: List<Movie>
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            MovieRow(
                movie = newMovieList.first()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Text(text = "Movie Image")
            HorizontalScrollableImageView(newMovieList)
//            Text(
//                textAlign = TextAlign.Justify,
//                text = newMovieList[0].title,
//                style = MaterialTheme.typography.headlineMedium
//            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text(text = "Go Back")
            }
        }
    }
}

@Composable
fun HorizontalScrollableImageView(newMovieList: List<Movie>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(newMovieList[0].images) { index, item ->
            Card(
                modifier = Modifier
                    .padding(3.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Image(
                    modifier = Modifier
                        .width(100.dp)
                        .height(60.dp),
                    painter = rememberImagePainter(data = item),
                    contentDescription = "Movie Poster",
                    contentScale = ContentScale.Crop
                )
//                Image(
//                    painter = rememberImagePainter(
//                        data = item,
//                        builder = {
//                            size(OriginalSize)
//                        },
//                    ),
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxWidth()
//                )
            }
        }
    }
}