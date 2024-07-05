package com.my.presentation.screen.movielist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.my.common.UrlConvertUtil
import com.my.domain.model.MovieModelResult
import com.my.presentation.component.IsLoadMoreLoading
import com.my.presentation.component.IsLoading
import com.my.presentation.component.MovieItem
import com.my.presentation.component.SearchBarComponent
import com.my.presentation.navigation.AppNavigationScreen
import com.my.presentation.screen.movielist.model.MovieListUiState
import com.my.presentation.screen.movielist.presenter.MovieListPresenter
import com.my.presentation.screen.movielist.presenter.MovieListViewModelPresenter
import com.my.presentation.screen.movielist.viewmodel.MovieListViewModel
import kotlinx.coroutines.launch

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
                is MovieListPresenter.MoveToMovieSearch -> {
                    navController.navigate(route = AppNavigationScreen.MovieSearchScreen.name)
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

    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(5.dp)
    ) {

        SearchBarComponent(
            searchText = "",
            changeSearchText = { },
            searchIconClick = { },
            fakeButton = true,
            fakeButtonClick = {
                Log.d("MYTAG", "fake button clicked")
                movieListPresenter.invoke(MovieListPresenter.MoveToMovieSearch())
            },
            startKeyboardUp = false
        )

        if(movieListUiState.movieList == null || movieListUiState.movieList?.size!! == 0) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "영화 리스트 없음")
            }
        } else {
            LazyColumn(
                state = scrollState
            ) {
                itemsIndexed(movieListUiState.movieList!!) { index, item ->
                    movieListViewModelPresenter.invoke(MovieListViewModelPresenter.UpdateCurrentPosition(index))
                    MovieItem(index, item,
                        onItemClick = {
                            movieListPresenter.invoke(MovieListPresenter.MoveToMovieInfo(it))
                        },
                        onItemLongClick = {

                        }
                    )
                }

                item {
                    IsLoadMoreLoading(movieListUiState.isLoading)
                }
            }

            Log.d("MYTAG", "${cantScrollForward} / ${movieListUiState.currentPosition} / ${movieListUiState.movieList!!.size}")
//                if(cantScrollForward) {
            if(!movieListUiState.isLoading && movieListUiState.currentPosition >= (movieListUiState.movieList!!.size - 10)) {
                Log.d("MYTAG", "Request new item ${movieListUiState.movieList!!.size}")
                movieListViewModelPresenter.invoke(MovieListViewModelPresenter.GetPopularMovies())
            }
//                }
        }
        
    }
    

    LaunchedEffect(key1 = isEffectExecuted.value) {
        if (!isEffectExecuted.value) {
            movieListViewModelPresenter.invoke(MovieListViewModelPresenter.GetPopularMovies())
            isEffectExecuted.value = true
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieListScreen() {
    MovieListScreenUI(
        movieListPresenter = { },
        movieListViewModelPresenter = { },
        movieListUiState = MovieListUiState(movieList = listOf(MovieModelResult(), MovieModelResult(), MovieModelResult()))
    )
}