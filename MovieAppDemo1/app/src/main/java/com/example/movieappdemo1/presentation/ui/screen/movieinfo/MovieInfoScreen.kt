package com.example.movieappdemo1.presentation.ui.screen.movieinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.movieappdemo1.BuildConfig
import com.example.movieappdemo1.R
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.presentation.util.ConvertUtil
import com.example.movieappdemo1.ui.theme.DeepBlue
import com.example.movieappdemo1.ui.theme.LightGray
import com.example.movieappdemo1.ui.theme.White
import com.google.gson.Gson
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Preview
@Composable
fun PreviewMovieInfoScreen() {
    val navController = rememberNavController()
    UiMovieInfoScreen(
        navController = navController,
        "{\"adult\":false,\"backdrop_path\":\"/en3GU5uGkKaYmSyetHV4csHHiH3.jpg\",\"genre_ids\":[10752,28,18],\"id\":929590,\"original_language\":\"en\",\"original_title\":\"Civil War\",\"overview\":\"In the near future, a group of war journalists attempt to survive while reporting the truth as the United States stands on the brink of civil war.\",\"popularity\":1977.095,\"poster_path\":\"/sh7Rg8Er3tFcN9BpKIPOMvALgZd.jpg\",\"release_date\":\"2024-04-10\",\"title\":\"Civil War\",\"video\":false,\"vote_average\":7.313,\"vote_count\":922}",
        movieInfoScreenViewModelPresenter = { }
    )
}

sealed class MovieInfoScreenViewModelPresenter {
    data class SaveMovie(val movieModelResult: MovieModelResult) : MovieInfoScreenViewModelPresenter()
}

@Composable
fun MovieInfoScreen(
    navController: NavController,
    movieInfoScreenViewModel: MovieInfoScreenViewModel,
    movieModelResultJson: String
) {

    UiMovieInfoScreen(
        navController,
        movieModelResultJson,
        movieInfoScreenViewModelPresenter = {
            val callback: MovieInfoScreenViewModelPresenter = it
            when (callback) {
                is MovieInfoScreenViewModelPresenter.SaveMovie -> {
                    movieInfoScreenViewModel.saveMovie(callback.movieModelResult)
                }
            }
        }
    )

}

@Composable
fun UiMovieInfoScreen(
    navController: NavController,
    movieModelResultJson: String,
    movieInfoScreenViewModelPresenter: (MovieInfoScreenViewModelPresenter) -> Unit
) {
    val movieModelResultString = ConvertUtil.urlDecode(movieModelResultJson)
    LogUtil.i_dev("movieModelResultJson: ${movieModelResultString}")
    val gson = Gson()
    val movieModelResult: MovieModelResult =
        gson.fromJson(movieModelResultString, MovieModelResult::class.java)
    LogUtil.i_dev("movieModelResult: ${movieModelResult}")

    val scrollableState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = White
            )
    ) {
        Column {
            ActionBar(navController, movieModelResult.title.toString())
            Column(
                modifier = Modifier
                    .verticalScroll(scrollableState)
            ) {
                ThumbnailImage(movieModelResult = movieModelResult)
                MovieInfoText(movieModelResult = movieModelResult)
            }
        }
        SaveMovieButton(movieInfoScreenViewModelPresenter, movieModelResult = movieModelResult)
    }
}

@Composable
fun ActionBar(
    navController: NavController,
    title: String
) {
    var duplicated = remember { mutableStateOf(false) }
    val timer = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                color = LightGray
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp)
                .clickable(
                    enabled = !duplicated.value
                ) {
                    duplicated.value = true

                    navController.popBackStack()

//                    timer.launch {
//                        delay(500)
//                        duplicated.value  = false
//                    }

                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                imageModel = { R.drawable.ic_arrow_back_ios_new_24 },
                previewPlaceholder = painterResource(id = R.drawable.ic_arrow_back_ios_new_24),
                imageOptions = ImageOptions(
                    alignment = Alignment.Center
                )
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

@Preview
@Composable
private fun PreviewBackButton() {
    GlideImage(
        imageModel = { R.drawable.ic_arrow_back_ios_new_24 },
        previewPlaceholder = painterResource(id = R.drawable.ic_arrow_back_ios_new_24),
        imageOptions = ImageOptions(
            alignment = Alignment.Center
        )
    )
}

@Composable
fun ThumbnailImage(
    movieModelResult: MovieModelResult
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        GlideImage(
            imageModel = { BuildConfig.BASE_MOVIE_POSTER + movieModelResult.posterPath },
            modifier = Modifier.fillMaxWidth(),
            previewPlaceholder = painterResource(id = R.drawable.ic_launcher_background),
            imageOptions = ImageOptions(
                contentScale = ContentScale.FillWidth
            )
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
            text = movieModelResult.title ?: "",
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = movieModelResult.releaseDate ?: "",
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = movieModelResult.popularity.toString(),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = movieModelResult.overview ?: "",
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun SaveMovieButton(
    movieInfoScreenViewModelPresenter: (MovieInfoScreenViewModelPresenter) -> Unit,
    movieModelResult: MovieModelResult
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                movieInfoScreenViewModelPresenter.invoke(
                    MovieInfoScreenViewModelPresenter.SaveMovie(
                        movieModelResult
                    )
                )
                LogUtil.i_dev("Save movie ${movieModelResult.title}")
            },
            containerColor = DeepBlue,
            shape = CircleShape,
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "This is Add Fab",
                tint = White,
            )
        }
    }
}