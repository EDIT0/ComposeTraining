package com.my.presentation.screen.moviesearch

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.my.domain.model.MovieModelResult
import com.my.presentation.component.IsLoading
import com.my.presentation.component.MovieItem
import com.my.presentation.component.SearchBarComponent
import com.my.presentation.screen.movielist.presenter.MovieListPresenter
import com.my.presentation.screen.movielist.presenter.MovieListViewModelPresenter
import com.my.presentation.screen.moviesearch.model.SearchAndListUiState
import com.my.presentation.screen.moviesearch.presenter.MovieSearchPresenter
import com.my.presentation.screen.moviesearch.presenter.MovieSearchViewModelPresenter
import com.my.presentation.screen.moviesearch.viewmodel.MovieSearchViewModel

@Composable
fun MovieSearchScreen(
    navController: NavController,
    movieSearchViewModel: MovieSearchViewModel = hiltViewModel()
) {
    MovieSearchScreenUI(
        searchAndListState = SearchAndListUiState(),
        movieSearchPresenter = {

        },
        movieSearchViewModelPresenter = {

        }
    )
}

@Composable
fun MovieSearchScreenUI(
    searchAndListState: SearchAndListUiState,
    movieSearchPresenter: (MovieSearchPresenter) -> Unit,
    movieSearchViewModelPresenter: (MovieSearchViewModelPresenter) -> Unit
) {
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
            fakeButton = false,
            fakeButtonClick = {
                Log.d("MYTAG", "fake button clicked")
            },
            startKeyboardUp = true
        )
        searchAndListState.searchedMovieList?.let { searchedMovieList ->
            LazyColumn(
                state = scrollState
            ) {
                itemsIndexed(searchedMovieList) {index, item ->
                    movieSearchViewModelPresenter.invoke(MovieSearchViewModelPresenter.UpdateCurrentPosition(index))
                    MovieItem(index, item,
                        onItemClick = {
                            movieSearchPresenter.invoke(MovieSearchPresenter.MoveToMovieInfo(it))
                        },
                        onItemLongClick = {

                        }
                    )
                }
            }

            if(cantScrollForward) {
                if(!searchAndListState.isLoading && searchAndListState.currentPosition >= (searchedMovieList.size - 10)) {
                    Log.d("MYTAG", "Request new item ${searchedMovieList.size}")
                    movieSearchViewModelPresenter.invoke(MovieSearchViewModelPresenter.GetPopularMovies())
                }
            }
        }

        IsLoading(searchAndListState.isLoading)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieSearchScreen() {
    MovieSearchScreenUI(
        searchAndListState = SearchAndListUiState(listOf(MovieModelResult(), MovieModelResult(), MovieModelResult())),
        movieSearchPresenter = { },
        movieSearchViewModelPresenter = { }
    )
}