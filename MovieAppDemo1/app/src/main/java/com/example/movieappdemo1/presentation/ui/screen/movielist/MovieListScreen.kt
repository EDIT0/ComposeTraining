package com.example.movieappdemo1.presentation.ui.screen.movielist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.movieappdemo1.BuildConfig
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.presentation.ui.screen.home.moveToMovieInfo
import com.example.movieappdemo1.presentation.ui.screen.home.setLoading
import com.example.movieappdemo1.ui.theme.White

@Preview
@Composable
fun MovieListScreenPreview() {
    val bottomNavController = rememberNavController()
    val navController = rememberNavController()
    MovieListScreen(bottomNavController, navController, hiltViewModel())
}

@Composable
fun MovieListScreen(
    bottomNavController: NavController,
    navController: NavController,
    movieListScreenViewModel: MovieListScreenViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        MovieListActionBar()
        MovieList(navController, movieListScreenViewModel)
    }

    setLoading(movieListScreenViewModel.isLoading.value)
}

@Composable
fun MovieListActionBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Text(
            text = "Popular Movie List",
            modifier = Modifier
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@Composable
fun MovieList(
    navController: NavController,
    movieListScreenViewModel: MovieListScreenViewModel,
) {
    val scrollState = rememberLazyListState()
    val cantScrollForward = !scrollState.canScrollForward
    val cantScrollBackward = !scrollState.canScrollBackward

//    LogUtil.i_dev("MYTAG cantScrollForward: ${cantScrollForward}")
//    LogUtil.i_dev("MYTAG cantScrollBackward: ${cantScrollBackward}")

    LazyColumn (
        state = scrollState
    ){
        itemsIndexed(movieListScreenViewModel.allMovieList) {index, item ->
//            LogUtil.d_dev("NavController: ${navController.currentDestination}\nindex: ${index} / item: ${item?.title}")
            movieListScreenViewModel.currentPosition.value = index
            MovieItem(index, item) {
                moveToMovieInfo(navController, it)
            }
        }
    }

    if(cantScrollForward) {
        if(
            !movieListScreenViewModel.isLoading.value
            && movieListScreenViewModel.currentPosition.value >= (movieListScreenViewModel.allMovieList.size - 5)
            ) {
            LogUtil.d_dev("MYTAG Request new item ${movieListScreenViewModel.allMovieList.size}")
            movieListScreenViewModel.getPopularMovies()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieItem(
    index: Int,
    movieModelResult: MovieModelResult,
    onItemClick: (MovieModelResult) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                LogUtil.d_dev("Clicked title: ${movieModelResult.title}")
                onItemClick.invoke(movieModelResult)
            }
    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
                    .padding(5.dp)
                    .background(White),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                GlideImage(
                    model = BuildConfig.BASE_MOVIE_POSTER + movieModelResult.posterPath,
                    contentDescription = "Movie poster image",
                    contentScale = ContentScale.Fit
                )
            }
            Column(
                modifier = Modifier
                    .weight(2f)
                    .background(White)
            ) {
                Text(
                    text = "${index} / ${movieModelResult.title}",
                    modifier = Modifier
                        .padding(5.dp),
                    maxLines = 3
                )

                Text(
                    text = "${movieModelResult.releaseDate}",
                    modifier = Modifier
                        .padding(5.dp),
                    maxLines = 1
                )
            }
        }
    }
}