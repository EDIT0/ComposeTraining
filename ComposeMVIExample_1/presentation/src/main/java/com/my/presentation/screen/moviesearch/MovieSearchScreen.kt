package com.my.presentation.screen.moviesearch

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.my.common.NetworkConstant
import com.my.common.UrlConvertUtil
import com.my.domain.model.MovieModelResult
import com.my.presentation.activity.MainActivity
import com.my.presentation.component.BackButtonComponent
import com.my.presentation.component.IsLoadMoreLoading
import com.my.presentation.component.MovieItem
import com.my.presentation.component.SearchBarComponent
import com.my.presentation.navigation.AppNavigationScreen
import com.my.presentation.screen.base.sideeffect.SideEffectEvent
import com.my.presentation.screen.moviesearch.intent.viewmodeltoview.SearchAndListUiErrorEvent
import com.my.presentation.screen.moviesearch.state.SearchAndListUiState
import com.my.presentation.screen.moviesearch.intent.viewtoview.MovieSearchScreenEvent
import com.my.presentation.screen.moviesearch.intent.viewtoviewmodel.MovieSearchViewModelEvent
import com.my.presentation.screen.moviesearch.viewmodel.MovieSearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MovieSearchScreen(
    navController: NavController,
    movieSearchViewModel: MovieSearchViewModel = hiltViewModel()
) {
    val localContext = LocalContext.current
    Log.d("MYTAG", "MovieSearchScreen Recomposition")

    MovieSearchScreenUI(
        localContext = localContext,
        back = {
            navController.popBackStack()
        },
        sideEffectEvent = movieSearchViewModel.sideEffects.collectAsState(initial = SideEffectEvent.Init()).value,
        searchAndListUiState = movieSearchViewModel.searchAndListUiState.collectAsState().value,
        searchAndListUiErrorEvent = movieSearchViewModel.searchAndListUiErrorEvent.collectAsState(initial = SearchAndListUiErrorEvent.Init()).value,
        movieSearchScreenEvent = {
            when(it) {
                is MovieSearchScreenEvent.MoveToMovieInfo -> {
                    val gson = Gson()
                    val movieModelResultJson: String = gson.toJson(it)
                    val encodedJson = UrlConvertUtil.urlEncode(movieModelResultJson)
                    Log.i("MYTAG", "encodedJson: ${encodedJson}")
                    navController.navigate(route = AppNavigationScreen.MovieDetailScreen.name + "/${encodedJson}")
                }
            }
        },
        movieSearchViewModelEvent = {
            when(it) {
                is MovieSearchViewModelEvent.GetSearchMovies -> {
                    movieSearchViewModel.handleViewModelEvent(MovieSearchViewModelEvent.GetSearchMovies(it.query, it.isClear))
                }
                is MovieSearchViewModelEvent.UpdateCurrentPosition -> {
                    movieSearchViewModel.handleViewModelEvent(MovieSearchViewModelEvent.UpdateCurrentPosition(it.position))
                }
                is MovieSearchViewModelEvent.UpdateSearchMovieSearchText -> {
                    movieSearchViewModel.handleViewModelEvent(MovieSearchViewModelEvent.UpdateSearchMovieSearchText(it.text))
                }
            }
        }
    )
}

@Composable
fun MovieSearchScreenUI(
    localContext: Context,
    back: () -> Unit,
    sideEffectEvent: SideEffectEvent,
    searchAndListUiState: SearchAndListUiState,
    searchAndListUiErrorEvent: SearchAndListUiErrorEvent,
    movieSearchScreenEvent: (MovieSearchScreenEvent) -> Unit,
    movieSearchViewModelEvent: (MovieSearchViewModelEvent) -> Unit
) {

    SideEffectEvent(
        localContext = localContext,
        sideEffectEvent = sideEffectEvent
    )
    SearchAndListUiStateView(
        back = back,
        searchAndListUiState = searchAndListUiState,
        searchAndListUiErrorEvent = searchAndListUiErrorEvent,
        movieSearchScreenEvent = movieSearchScreenEvent,
        movieSearchViewModelEvent = movieSearchViewModelEvent
    )
}

@Composable
fun SideEffectEvent(
    localContext: Context,
    sideEffectEvent: SideEffectEvent
) {
    LaunchedEffect(key1 = sideEffectEvent) {
        when(sideEffectEvent) {
            is SideEffectEvent.ShowToast -> {
                Log.d("MYTAG", "SideEffectEvent.ShowToast message: ${sideEffectEvent.message}")
                (localContext as MainActivity).showToast(sideEffectEvent.message)
            }
            else -> {

            }
        }
    }
}

@Composable
fun SearchAndListUiStateView(
    back: () -> Unit,
    searchAndListUiState: SearchAndListUiState,
    searchAndListUiErrorEvent: SearchAndListUiErrorEvent,
    movieSearchScreenEvent: (MovieSearchScreenEvent) -> Unit,
    movieSearchViewModelEvent: (MovieSearchViewModelEvent) -> Unit
) {
    val scrollState = rememberLazyListState()
    val cantScrollForward = !scrollState.canScrollForward
    val cantScrollBackward = !scrollState.canScrollBackward

    // 검색 관련 변수
    val scope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }
    val lastText = rememberSaveable {
        mutableStateOf("")
    }

    // 키보드 관련 변수
    var startKeyboardUp by rememberSaveable {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(5.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .height(50.dp),
                verticalArrangement = Arrangement.Center
            ) {
                BackButtonComponent(
                    enable = true,
                    backButtonClick = {
                        back.invoke()
                    }
                )
            }

            SearchBarComponent(
                searchText = searchAndListUiState.searchText?:"",
                heightDp = 50,
                changeSearchText = {
                    movieSearchViewModelEvent.invoke(MovieSearchViewModelEvent.UpdateSearchMovieSearchText(it))
                    lastText.value = it
                },
                searchIconClick = {
                    job?.cancel()
                    movieSearchViewModelEvent.invoke(MovieSearchViewModelEvent.GetSearchMovies(searchAndListUiState.searchText?:"", true))
                },
                fakeButton = false,
                fakeButtonClick = {
                    Log.d("MYTAG", "TextInput clicked")
                },
                startKeyboardUp = startKeyboardUp
            )

            LaunchedEffect(key1 = lastText.value) {
                job?.cancel()
                job = scope.launch {
                    delay(1000L)
                    movieSearchViewModelEvent.invoke(MovieSearchViewModelEvent.GetSearchMovies(searchAndListUiState.searchText?:"", true))
                }
            }
        }

        searchAndListUiState.searchedMovieList?.let { searchedMovieList ->
            LazyColumn(
                state = scrollState
            ) {
                itemsIndexed(searchedMovieList) {index, item ->
                    movieSearchViewModelEvent.invoke(MovieSearchViewModelEvent.UpdateCurrentPosition(index))
                    MovieItem(index, item,
                        onItemClick = {
                            movieSearchScreenEvent.invoke(MovieSearchScreenEvent.MoveToMovieInfo(it))
                        },
                        onItemLongClick = {

                        }
                    )
                }

                item {
                    IsLoadMoreLoading(searchAndListUiState.isLoading)
                }
            }

            if(!searchAndListUiState.isPagingDone) {
                if(cantScrollForward) {
                    if(!searchAndListUiState.isLoading && searchAndListUiState.currentPosition >= searchedMovieList.size - 10) {
                        Log.d("MYTAG", "Request new item ${searchedMovieList.size}")
                        movieSearchViewModelEvent.invoke(MovieSearchViewModelEvent.GetSearchMovies(searchAndListUiState.searchText?:"", false))
                    }
                }
            }
        }

        if((searchAndListUiState.searchedMovieList?.size ?: 0) == 0) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "검색된 영화 없음")
            }
        }
    }

    LaunchedEffect(key1 = searchAndListUiErrorEvent) {
        when(searchAndListUiErrorEvent) {
            is SearchAndListUiErrorEvent.Fail -> {
                when (searchAndListUiErrorEvent.code) {
                    NetworkConstant.ERROR -> {
                        Log.d("MYTAG", "searchAndListUiErrorEvent.code Error")
                    }
                    NetworkConstant.ERROR_2 -> {
                        Log.d("MYTAG", "searchAndListUiErrorEvent.code ERROR_2")
                    }
                }
            }
            is SearchAndListUiErrorEvent.DataEmpty -> {
                Log.d("MYTAG", "movieListUiErrorEvent.isDataEmpty")
            }
            is SearchAndListUiErrorEvent.ExceptionHandle -> {
                Log.d("MYTAG", "movieListUiErrorEvent.throwable ${searchAndListUiErrorEvent.throwable}")
            }
            else -> {

            }
        }
    }

    // 최초 1회 키보드 Up
    LaunchedEffect(key1 = true) {
        startKeyboardUp = false
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieSearchScreen() {
    MovieSearchScreenUI(
        localContext = LocalContext.current,
        back = { },
        sideEffectEvent = SideEffectEvent.Init(),
        searchAndListUiState = SearchAndListUiState(listOf(MovieModelResult(), MovieModelResult(), MovieModelResult())),
        searchAndListUiErrorEvent = SearchAndListUiErrorEvent.Init(),
        movieSearchScreenEvent = { },
        movieSearchViewModelEvent = { }
    )
}