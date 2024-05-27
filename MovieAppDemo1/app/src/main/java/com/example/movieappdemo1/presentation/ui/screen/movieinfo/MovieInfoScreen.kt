package com.example.movieappdemo1.presentation.ui.screen.movieinfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieappdemo1.BuildConfig
import com.example.movieappdemo1.R
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.presentation.util.ConvertUtil
import com.google.gson.Gson

@Preview
@Composable
fun MovieInfoScreenPreview() {
    val navController = rememberNavController()
    val movieInfoScreenViewModel = MovieInfoScreenViewModel()
    MovieInfoScreen(
        navController = navController,
        movieInfoScreenViewModel = movieInfoScreenViewModel,
        ""
    )
}

@Composable
fun MovieInfoScreen(
    navController: NavController,
    movieInfoScreenViewModel: MovieInfoScreenViewModel,
    movieModelResultJson: String
) {
    val movieModelResultString = ConvertUtil.urlDecode(movieModelResultJson)
    LogUtil.i_dev("movieModelResultJson: ${movieModelResultString}")
    val gson = Gson()
    val movieModelResult: MovieModelResult =
        gson.fromJson(movieModelResultString, MovieModelResult::class.java)
    LogUtil.i_dev("movieModelResult: ${movieModelResult}")

    val scrollableState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollableState),
    ) {
        ActionBar(navController, movieModelResult.title.toString())
        ThumbnailImage(movieModelResult = movieModelResult)
        MovieInfoText(movieModelResult = movieModelResult)
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ActionBar(
    navController: NavController,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp)
                .clickable {
                    navController.popBackStack()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                model = R.drawable.ic_arrow_back_ios_new_24,
                contentDescription = "Back button"
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(0.dp, 0.dp, 10.dp, 0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ThumbnailImage(
    movieModelResult: MovieModelResult
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        GlideImage(
            model = BuildConfig.BASE_MOVIE_POSTER + movieModelResult.posterPath,
            contentDescription = "Movie poster image"
        )
    }
}

@Composable
fun MovieInfoText(
    movieModelResult: MovieModelResult
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Text(
            text = movieModelResult.title?:"",
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = movieModelResult.releaseDate?:"",
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = movieModelResult.popularity.toString(),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = movieModelResult.overview?:"",
            letterSpacing = 1.sp
        )
    }
}