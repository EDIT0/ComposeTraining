package com.my.presentation.screen.movielist

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.my.common.UrlConvertUtil
import com.my.presentation.component.IsLoading
import com.my.presentation.component.MovieItem
import com.my.presentation.navigation.AppNavigationScreen
import com.my.presentation.screen.movielist.model.MovieListUiState
import com.my.presentation.screen.movielist.presenter.MovieListPresenter
import com.my.presentation.screen.movielist.presenter.MovieListViewModelPresenter
import com.my.presentation.screen.movielist.viewmodel.MovieListViewModel

@Composable
fun MovieListScreen(
    navController: NavController,
    movieListViewModel: MovieListViewModel = hiltViewModel()
) {
    MovieListScreenUI(
        movieListPresenter = {
            when(it) {
                is MovieListPresenter.MoveToMovieInfo -> {
                    val gson = Gson()
                    val movieModelResultJson: String = gson.toJson(it)
                    val encodedJson = UrlConvertUtil.urlEncode(movieModelResultJson)
                    Log.i("MYTAG", "encodedJson: ${encodedJson}")
                    navController.navigate(route = AppNavigationScreen.MovieDetailScreen.name + "/${encodedJson}")
                }
            }
        },
        movieListViewModelPresenter = {
            when(it) {
                is MovieListViewModelPresenter.GetPopularMovies -> {
                    movieListViewModel.getPopularMovies()
                }
                is MovieListViewModelPresenter.UpdateCurrentPosition -> {
                    movieListViewModel.updateCurrentPosition(it.position)
//                    movieListViewModel.currentPosition = it.position
                }
            }
        },
        movieListUiState = movieListViewModel.movieListUiState.collectAsState().value
    )
}

@Composable
fun MovieListScreenUI(
    movieListPresenter : (MovieListPresenter) -> Unit,
    movieListViewModelPresenter: (MovieListViewModelPresenter) -> Unit,
    movieListUiState: MovieListUiState
) {
    val isEffectExecuted = rememberSaveable { mutableStateOf(false) }

    Log.d("MYTAG", "${movieListUiState.movieList}\n" +
            "${movieListUiState.code}\n" +
            "${movieListUiState.message}\n" +
            "${movieListUiState.isLoading}\n" +
            "${movieListUiState.throwable}\n" +
            "${movieListUiState.isDataEmpty}\n" +
            "${movieListUiState.currentPosition}\n"
    )

    val scrollState = rememberLazyListState()
    val cantScrollForward = !scrollState.canScrollForward
    val cantScrollBackward = !scrollState.canScrollBackward

    movieListUiState.movieList?.let { movieList ->
        LazyColumn(
            state = scrollState
        ) {
            itemsIndexed(movieList) {index, item ->
                movieListViewModelPresenter.invoke(MovieListViewModelPresenter.UpdateCurrentPosition(index))
                MovieItem(index, item,
                    onItemClick = {
                        movieListPresenter.invoke(MovieListPresenter.MoveToMovieInfo(it))
                    },
                    onItemLongClick = {

                    }
                )
            }
        }

        if(cantScrollForward) {
            if(!movieListUiState.isLoading && movieListUiState.currentPosition >= (movieList.size - 10)) {
                Log.d("MYTAG", "Request new item ${movieList.size}")
                movieListViewModelPresenter.invoke(MovieListViewModelPresenter.GetPopularMovies())
            }
        }
    }

    IsLoading(movieListUiState.isLoading)

    LaunchedEffect(key1 = isEffectExecuted.value) {
        if (!isEffectExecuted.value) {
            movieListViewModelPresenter.invoke(MovieListViewModelPresenter.GetPopularMovies())
            isEffectExecuted.value = true // Mark the effect as executed
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieListScreen() {
    MovieListScreenUI(
        movieListPresenter = { },
        movieListViewModelPresenter = { },
        movieListUiState = MovieListUiState()
    )
}