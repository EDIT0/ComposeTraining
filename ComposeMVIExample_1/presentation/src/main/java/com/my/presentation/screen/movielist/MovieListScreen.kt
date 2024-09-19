package com.my.presentation.screen.movielist

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.my.common.NetworkConstant
import com.my.common.UrlConvertUtil
import com.my.domain.model.MovieModel
import com.my.domain.model.MovieModelResult
import com.my.presentation.activity.MainActivity
import com.my.presentation.component.IsLoadMoreLoading
import com.my.presentation.component.MovieItem
import com.my.presentation.component.SearchBarComponent
import com.my.presentation.navigation.AppNavigationScreen
import com.my.presentation.screen.movielist.state.MovieListUiState
import com.my.presentation.screen.movielist.intent.viewtoview.MovieListScreenEvent
import com.my.presentation.screen.movielist.intent.viewtoviewmodel.MovieListViewModelEvent
import com.my.presentation.screen.base.sideeffect.SideEffectEvent
import com.my.presentation.screen.movielist.intent.viewmodeltoview.MovieListUiErrorEvent
import com.my.presentation.screen.movielist.state.MovieCountUiState
import com.my.presentation.screen.movielist.viewmodel.MovieListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MovieListScreen(
    navController: NavController,
    movieListViewModel: MovieListViewModel = hiltViewModel(),
) {
    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()
    Log.d("MYTAG", "MovieListScreen Recomposition")

    MovieListScreenUI(
        localContext = localContext,
        scope = scope,
        movieListScreenEvent = {
            when(it) {
                is MovieListScreenEvent.MoveToMovieInfo -> {
                    val gson = Gson()
                    val movieModelResultJson: String = gson.toJson(it)
                    val encodedJson = UrlConvertUtil.urlEncode(movieModelResultJson)
                    Log.i("MYTAG", "encodedJson: ${encodedJson}")
                    navController.navigate(route = AppNavigationScreen.MovieDetailScreen.name + "/${encodedJson}")
                }
                is MovieListScreenEvent.MoveToMovieSearch -> {
                    navController.navigate(route = AppNavigationScreen.MovieSearchScreen.name)
                }
            }
        },
        sideEffectEvent = movieListViewModel.sideEffects.collectAsState(initial = SideEffectEvent.Init()).value,
        movieListViewModelEvent = {
            when(it) {
                is MovieListViewModelEvent.GetPopularMovies -> {
                    movieListViewModel.handleViewModelEvent(it)
                }
                is MovieListViewModelEvent.UpdateCurrentPosition -> {
                    movieListViewModel.handleViewModelEvent(MovieListViewModelEvent.UpdateCurrentPosition(it.position))
                }
            }
        },
        movieListUiState = movieListViewModel.movieListUiState.collectAsState().value,
        movieListUiErrorEvent = movieListViewModel.movieListUiErrorEvent.collectAsState(initial = MovieListUiErrorEvent.Init()).value,
//        movieCountUiState = movieListViewModel.movieCountUiState.collectAsState().value
    )
}

@Composable
fun MovieListScreenUI(
    localContext: Context,
    scope: CoroutineScope,
    movieListScreenEvent : (MovieListScreenEvent) -> Unit,
    sideEffectEvent: SideEffectEvent,
    movieListViewModelEvent: (MovieListViewModelEvent) -> Unit,
    movieListUiState: MovieListUiState,
    movieListUiErrorEvent: MovieListUiErrorEvent,
//    movieCountUiState: MovieCountUiState
) {
    val isInitData = rememberSaveable { mutableStateOf(false) }

//    Log.d("MYTAG", "${movieListUiState.movieList}\n" +
//            "${movieListUiState.code}\n" +
//            "${movieListUiState.message}\n" +
//            "${movieListUiState.isLoading}\n" +
//            "${movieListUiState.throwable}\n" +
//            "${movieListUiState.isDataEmpty}\n" +
//            "${movieListUiState.currentPosition}\n"
//    )

    InitData(
        isInitData = isInitData,
        movieListListViewModelEvent = movieListViewModelEvent
    )
    SideEffectEvent(
        localContext = localContext,
        sideEffectEvent = sideEffectEvent
    )
    MovieListUiStateView(
        localContext = localContext,
        scope = scope,
        movieListScreenEvent = movieListScreenEvent,
        movieListListViewModelEvent = movieListViewModelEvent,
        movieListUiState = movieListUiState,
        movieListUiErrorEvent = movieListUiErrorEvent,
//        movieCountUiState = movieCountUiState
    )
}

@Composable
fun InitData(
    isInitData : MutableState<Boolean>,
    movieListListViewModelEvent: (MovieListViewModelEvent) -> Unit
) {
    LaunchedEffect(key1 = isInitData.value) {
        if (!isInitData.value) {
            movieListListViewModelEvent.invoke(MovieListViewModelEvent.GetPopularMovies(page = 0, size = 20))
            isInitData.value = true
        }
    }
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MovieListUiStateView(
    localContext: Context,
    scope: CoroutineScope,
    movieListScreenEvent : (MovieListScreenEvent) -> Unit,
    movieListListViewModelEvent: (MovieListViewModelEvent) -> Unit,
    movieListUiState: MovieListUiState,
    movieListUiErrorEvent: MovieListUiErrorEvent,
//    movieCountUiState: MovieCountUiState
) {
    val scrollState = rememberLazyListState()
//    val cantScrollForward = !scrollState.canScrollForward
//    val cantScrollBackward = !scrollState.canScrollBackward

    val endReached = remember {
        derivedStateOf {
            with (scrollState.layoutInfo) {
                visibleItemsInfo.lastOrNull()?.index == totalItemsCount - 1
            }
        }
    }
    val endReachedSupport = rememberSaveable {
        mutableStateOf(true)
    }
    // 스크롤 끝 도달 시 새로운 페이지 요청
    if(!movieListUiState.isLoading && endReached.value && endReachedSupport.value) {
        endReachedSupport.value = false

        if(movieListUiState.movieModel?.page != movieListUiState.movieModel?.totalPages) {
            movieListListViewModelEvent.invoke(
                MovieListViewModelEvent.GetPopularMovies(
                    page = movieListUiState.movieModel?.page?:0,
                    size = 20,
                ))
        }

        scope.launch {
            delay(200L)
            endReachedSupport.value = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(5.dp)
    ) {

        SearchBarComponent(
            searchText = "",
            heightDp = 50,
            changeSearchText = { },
            searchIconClick = { },
            fakeButton = true,
            fakeButtonClick = {
                Log.d("MYTAG", "fake button clicked")
                movieListScreenEvent.invoke(MovieListScreenEvent.MoveToMovieSearch())
            },
            startKeyboardUp = false
        )

        val pullToRefreshState = rememberPullToRefreshState()

        LaunchedEffect(key1 = pullToRefreshState.isRefreshing) {
            if (pullToRefreshState.isRefreshing) {
                movieListListViewModelEvent.invoke(MovieListViewModelEvent.GetPopularMovies(page = 0, size = 20))
                scope.launch {
                    delay(500)
                    pullToRefreshState.endRefresh()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            if(movieListUiState.movieModel == null || movieListUiState.movieModel?.movieModelResults?.size!! == 0 && !movieListUiState.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
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
                    itemsIndexed(movieListUiState.movieModel?.movieModelResults!!) { index, item ->
                        movieListListViewModelEvent.invoke(MovieListViewModelEvent.UpdateCurrentPosition(index))
                        MovieItem(index, item,
                            onItemClick = {
                                movieListScreenEvent.invoke(MovieListScreenEvent.MoveToMovieInfo(it))
                            },
                            onItemLongClick = {

                            }
                        )
                    }

                    item {
                        IsLoadMoreLoading(movieListUiState.isLoading)
                    }
                }

//                Log.d("MYTAG", "${cantScrollForward} / ${movieListUiState.currentPosition} / ${movieListUiState.movieModel!!.size}")
//                if(cantScrollForward) {
//                    if(!movieListUiState.isLoading && movieListUiState.currentPosition >= (movieListUiState.movieModel!!.size - 10)) {
//                        Log.d("MYTAG", "Request new item ${movieListUiState.movieModel!!.size}")
//                        movieListListViewModelEvent.invoke(MovieListViewModelEvent.GetPopularMovies())
//                    }
//                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End
            ) {
                Text(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    textAlign = TextAlign.End,
                    text = "전체 영화 수: ${movieListUiState.movieModel?.totalResults?:0} | 현재 영화 수: ${movieListUiState.movieModel?.movieModelResults?.size?:0}"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (pullToRefreshState.progress > 0.5f) {
                    PullToRefreshContainer(state = pullToRefreshState)
                }
            }

        }
    }

    LaunchedEffect(key1 = movieListUiErrorEvent) {
        when(movieListUiErrorEvent) {
            is MovieListUiErrorEvent.Fail -> {
                when (movieListUiErrorEvent.code) {
                    NetworkConstant.ERROR -> {
                        Log.d("MYTAG", "movieListUiErrorEvent.code Error")
                        (localContext as MainActivity).showToast(movieListUiErrorEvent.code)
                    }
                    NetworkConstant.ERROR_2 -> {
                        Log.d("MYTAG", "movieListUiErrorEvent.code ERROR_2")
                        (localContext as MainActivity).showToast(movieListUiErrorEvent.code)
                    }
                }
            }
            is MovieListUiErrorEvent.DataEmpty -> {
                Log.d("MYTAG", "movieListUiErrorEvent.isDataEmpty")
                (localContext as MainActivity).showToast("DataEmpty")
            }
            is MovieListUiErrorEvent.ExceptionHandle -> {
                Log.d("MYTAG", "movieListUiErrorEvent.throwable ${movieListUiErrorEvent.throwable}")
                (localContext as MainActivity).showToast(movieListUiErrorEvent.throwable.toString())
            }
            else -> {

            }
        }
    }

//    if(movieListUiState.code != null) {
//        when(movieListUiState.code) {
//            NetworkConstant.ERROR -> {
//                Log.d("MYTAG", "movieListUiState.code Error")
//                (localContext as MainActivity).showToast(movieListUiState.code)
//            }
//            NetworkConstant.ERROR_2 -> {
//                Log.d("MYTAG", "movieListUiState.code ERROR_2")
//                (localContext as MainActivity).showToast(movieListUiState.code)
//            }
//        }
//    } else if(movieListUiState.isDataEmpty) {
//        Log.d("MYTAG", "movieListUiState.isDataEmpty")
//        (localContext as MainActivity).showToast("DataEmpty")
//    } else if(movieListUiState.throwable != null) {
//        Log.d("MYTAG", "movieListUiState.throwable ${movieListUiState.throwable}")
//        (localContext as MainActivity).showToast(movieListUiState.throwable.toString())
//    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieListScreen() {
    MovieListScreenUI(
        localContext = LocalContext.current,
        scope = rememberCoroutineScope(),
        movieListScreenEvent = { },
        sideEffectEvent = SideEffectEvent.Init(),
        movieListViewModelEvent = { },
        movieListUiState = MovieListUiState(movieModel = MovieModel(
            page = 1,
            movieModelResults = listOf(MovieModelResult(), MovieModelResult(), MovieModelResult()),
            totalPages = 10,
            totalResults = 100,
        )),
        movieListUiErrorEvent = MovieListUiErrorEvent.Init(),
//        movieCountUiState = MovieCountUiState(100, 1000)
    )
}