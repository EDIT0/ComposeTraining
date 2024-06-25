package com.example.movieappdemo1.presentation.ui.screen.movielist

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.movieappdemo1.BuildConfig
import com.example.movieappdemo1.R
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.presentation.ui.screen.home.HomeScreenViewModelPresenter
import com.example.movieappdemo1.presentation.ui.screen.home.SetLoading
import com.example.movieappdemo1.presentation.ui.screen.home.moveToMovieInfo
import com.example.movieappdemo1.ui.theme.White
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Preview
@Composable
fun PreviewMovieListScreen() {
    val bottomNavController = rememberNavController()
    val navController = rememberNavController()

    val mockMovie1 = MovieModelResult(
        idx = 1,
        adult = false,
        backdropPath = "/path/to/backdrop1.jpg",
        genreIds = listOf(28, 12, 878),
        id = 101,
        originalLanguage = "en",
        originalTitle = "Mock Movie 1",
        overview = "This is the overview of Mock Movie 1. It's a thrilling adventure with lots of action.",
        popularity = 1200.5,
        posterPath = "/path/to/poster1.jpg",
        releaseDate = "2023-06-01",
        title = "Mock Movie 1",
        video = false,
        voteAverage = 8.5,
        voteCount = 1500
    )

    val mockMovie2 = MovieModelResult(
        idx = 2,
        adult = false,
        backdropPath = "/path/to/backdrop2.jpg",
        genreIds = listOf(16, 35, 10751),
        id = 102,
        originalLanguage = "en",
        originalTitle = "Mock Movie 2",
        overview = "Mock Movie 2 is a delightful animated comedy that will entertain the whole family.",
        popularity = 900.3,
        posterPath = "/path/to/poster2.jpg",
        releaseDate = "2022-12-15",
        title = "Mock Movie 2",
        video = false,
        voteAverage = 7.8,
        voteCount = 800
    )

    val mockMovie3 = MovieModelResult(
        idx = 3,
        adult = false,
        backdropPath = "/path/to/backdrop3.jpg",
        genreIds = listOf(18, 10749),
        id = 103,
        originalLanguage = "en",
        originalTitle = "Mock Movie 3",
        overview = "Mock Movie 3 is a dramatic love story that explores the depths of human emotion.",
        popularity = 1100.0,
        posterPath = "/path/to/poster3.jpg",
        releaseDate = "2021-09-20",
        title = "Mock Movie 3",
        video = false,
        voteAverage = 9.0,
        voteCount = 2000
    )

    UiMovieListScreen(
        navController,
        listOf(mockMovie1, mockMovie2, mockMovie3),
        isLoading = false,
        currentPosition = 1,
        homeScreenViewModelPresenter = { },
        movieListScreenViewModelPresenter = { }
    )
}

sealed class MovieListScreenViewModelPresenter {
    class UpdateCurrentPosition(val position: Int) : MovieListScreenViewModelPresenter()
    class UpdateIsLoading(val isLoading: Boolean) : MovieListScreenViewModelPresenter()
    class GetPopularMovies : MovieListScreenViewModelPresenter()
}

@Composable
fun MovieListScreen(
    bottomNavController: NavController,
    navController: NavController,
    movieListScreenViewModel: MovieListScreenViewModel,
    homeScreenViewModelPresenter : (HomeScreenViewModelPresenter) -> Unit
) {
    val allMovieList = movieListScreenViewModel.allMovieList
    val isLoading = movieListScreenViewModel.isLoading.value
    val currentPosition = movieListScreenViewModel.currentPosition.value

    LogUtil.d_dev("MYTAG Recomposition ${movieListScreenViewModel.hashCode()} size: ${movieListScreenViewModel.allMovieList.size} isLoading: ${movieListScreenViewModel.isLoading.value}")

    UiMovieListScreen(
        navController = navController,
        allMovieList = allMovieList,
        isLoading = isLoading,
        currentPosition = currentPosition,
        homeScreenViewModelPresenter
    ) {
        val callback: MovieListScreenViewModelPresenter = it
        when (callback) {
            is MovieListScreenViewModelPresenter.UpdateCurrentPosition -> {
                movieListScreenViewModel.currentPosition.value = callback.position
            }
            is MovieListScreenViewModelPresenter.GetPopularMovies -> {
                movieListScreenViewModel.getPopularMovies()
            }
            is MovieListScreenViewModelPresenter.UpdateIsLoading -> {
                movieListScreenViewModel.isLoading.value = callback.isLoading
            }
        }
    }

}

@Composable
fun UiMovieListScreen(
    navController: NavController,
    allMovieList: List<MovieModelResult>,
    isLoading: Boolean,
    currentPosition: Int,
    homeScreenViewModelPresenter: (HomeScreenViewModelPresenter) -> Unit,
    movieListScreenViewModelPresenter: (MovieListScreenViewModelPresenter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        MovieListActionBar()
        MovieList(
            navController,
            movieListScreenViewModelPresenter,
            allMovieList,
            isLoading,
            currentPosition
        )
    }

    SetLoading(homeScreenViewModelPresenter, isLoading)
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
    movieListScreenViewModelPresenter: (MovieListScreenViewModelPresenter) -> Unit,
    allMovieList: List<MovieModelResult>,
    isLoading: Boolean,
    currentPosition: Int
) {
    val scrollState = rememberLazyListState()
    val cantScrollForward = !scrollState.canScrollForward
    val cantScrollBackward = !scrollState.canScrollBackward

//    LogUtil.i_dev("MYTAG cantScrollForward: ${cantScrollForward}")
//    LogUtil.i_dev("MYTAG cantScrollBackward: ${cantScrollBackward}")

    LazyColumn (
        state = scrollState
    ){
        itemsIndexed(allMovieList) {index, item ->
//            LogUtil.d_dev("NavController: ${navController.currentDestination}\nindex: ${index} / item: ${item?.title}")
//            movieListScreenViewModel.currentPosition.value = index
            movieListScreenViewModelPresenter(MovieListScreenViewModelPresenter.UpdateCurrentPosition(index))
            MovieItem(index, item,
                onItemClick = {
                    moveToMovieInfo(navController, it)
                },
                onItemLongClick = {

                }
            )
        }
    }

    if(cantScrollForward) {
        if(!isLoading && currentPosition >= (allMovieList.size - 5)) {
            LogUtil.d_dev("MYTAG Request new item ${allMovieList.size}")
//            movieListScreenViewModel.getPopularMovies()
            movieListScreenViewModelPresenter(MovieListScreenViewModelPresenter.GetPopularMovies())
        }
    }
}

@Composable
fun MovieItem(
    index: Int,
    movieModelResult: MovieModelResult,
    onItemClick: (MovieModelResult) -> Unit,
    onItemLongClick: (MovieModelResult) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
//            .clickable {
//                LogUtil.d_dev("Clicked title: ${movieModelResult.title}")
//                onItemClick.invoke(movieModelResult)
//            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        LogUtil.d_dev("Clicked title: ${movieModelResult.title}")
                        onItemClick.invoke(movieModelResult)
                    },
                    onLongPress = {
                        onItemLongClick.invoke(movieModelResult)
                    }
                )
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
                    imageModel = { BuildConfig.BASE_MOVIE_POSTER + movieModelResult.posterPath },
                    previewPlaceholder = painterResource(id = R.drawable.ic_launcher_background),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.FillHeight
                    )
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