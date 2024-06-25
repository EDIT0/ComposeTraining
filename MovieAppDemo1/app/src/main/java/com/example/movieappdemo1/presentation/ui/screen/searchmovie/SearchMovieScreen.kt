package com.example.movieappdemo1.presentation.ui.screen.searchmovie

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.presentation.ui.screen.home.moveToMovieInfo
import com.example.movieappdemo1.presentation.ui.screen.movielist.MovieItem
import com.example.movieappdemo1.ui.theme.LightGray
import com.example.movieappdemo1.ui.theme.White
import kotlinx.coroutines.launch

@Preview
@Composable
fun SearchMovieScreenPreview() {
    val bottomNavController = rememberNavController()
    val navController = rememberNavController()
    UiSearchMovieScreen(
        navController,
        emptyList(),
        false,
        "",
        1,
        searchMovieScreenViewModelPresenter = { }
    )
}

sealed class SearchMovieScreenViewModelPresenter {
    class UpdateSearchMovieSearchText(val text: String) : SearchMovieScreenViewModelPresenter()
    class GetSearchMovies(val searchText: String, val isClear: Boolean) : SearchMovieScreenViewModelPresenter()
    class UpdateCurrentPosition(val position: Int) : SearchMovieScreenViewModelPresenter()
    class SearchLogic() : SearchMovieScreenViewModelPresenter()
}

@Composable
fun SearchMovieScreen(
    bottomNavController: NavController,
    navController: NavController,
    searchMovieScreenViewModel: SearchMovieScreenViewModel
) {
    val searchedMoviesList = searchMovieScreenViewModel.searchedMovies

    val isLoading = searchMovieScreenViewModel.isLoading.value

    val searchMovieSearchText = searchMovieScreenViewModel.searchMovieSearchText.value
    val currentPosition = searchMovieScreenViewModel.currentPosition.value

    UiSearchMovieScreen(
        navController,
        searchedMoviesList,
        isLoading,
        searchMovieSearchText,
        currentPosition,
        searchMovieScreenViewModelPresenter = {
            val callback : SearchMovieScreenViewModelPresenter = it
            when(callback) {
                is SearchMovieScreenViewModelPresenter.GetSearchMovies -> {
                    searchMovieScreenViewModel.getSearchMovies(callback.searchText, callback.isClear)
                }
                is SearchMovieScreenViewModelPresenter.UpdateCurrentPosition -> {
                    searchMovieScreenViewModel.currentPosition.value = callback.position
                }
                is SearchMovieScreenViewModelPresenter.UpdateSearchMovieSearchText -> {
                    searchMovieScreenViewModel.searchMovieSearchText.value = callback.text
                }
                is SearchMovieScreenViewModelPresenter.SearchLogic -> {
                    searchMovieScreenViewModel.searchLogic()
                }
            }
        }
    )
}

@Composable
fun UiSearchMovieScreen(
    navController: NavController,
    searchedMoviesList: List<MovieModelResult>,
    isLoading: Boolean,
    searchMovieSearchText: String,
    currentPosition: Int,
    searchMovieScreenViewModelPresenter: (SearchMovieScreenViewModelPresenter) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        SearchMovieActionBar(
            searchMovieSearchText,
            searchMovieScreenViewModelPresenter
        )
        SearchMoviesList(
            navController,
            isLoading,
            currentPosition,
            searchedMoviesList,
            searchMovieSearchText,
            searchMovieScreenViewModelPresenter
        )
    }
}

@Composable
fun SearchMovieActionBar(
    searchMovieSearchText: String,
    searchMovieScreenViewModelPresenter: (SearchMovieScreenViewModelPresenter) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight(),
            shape = RoundedCornerShape(10.dp),
            value = searchMovieSearchText,
            onValueChange = {
//                searchMovieScreenViewModel.searchMovieSearchText.value = it
                searchMovieScreenViewModelPresenter(SearchMovieScreenViewModelPresenter.UpdateSearchMovieSearchText(it))
                searchMovieScreenViewModelPresenter.invoke(SearchMovieScreenViewModelPresenter.SearchLogic())
//                val isPass = SearchDelayUtil.onDelay {
//                    LogUtil.i_dev("MYTAG 라스트 검색어: ${searchMovieSearchText}")
////                    searchMovieScreenViewModel.getSearchMovies(searchMovieScreenViewModel.searchMovieSearchText.value, true)
//                    searchMovieScreenViewModelPresenter(SearchMovieScreenViewModelPresenter.GetSearchMovies(searchMovieSearchText, true))
//                }
//                if(isPass) {
//                    LogUtil.i_dev("MYTAG 검색어: ${searchMovieSearchText}")
////                    searchMovieScreenViewModel.getSearchMovies(searchMovieScreenViewModel.searchMovieSearchText.value, true)
//                    searchMovieScreenViewModelPresenter(SearchMovieScreenViewModelPresenter.GetSearchMovies(searchMovieSearchText, true))
//                }
            },
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = LightGray
            ),
            placeholder = {
                Text(text = "영화 검색")
            }
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SearchMoviesList(
    navController: NavController,
    isLoading: Boolean,
    currentPosition: Int,
    searchedMoviesList: List<MovieModelResult>,
    searchMovieSearchText: String,
    searchMovieScreenViewModelPresenter: (SearchMovieScreenViewModelPresenter) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val cantScrollForward = !scrollState.canScrollForward
    val cantScrollBackward = !scrollState.canScrollBackward

    if (searchedMoviesList.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Empty",
            )
        }
        coroutineScope.launch {
            scrollState.scrollToItem(0)
        }
    } else {
        LogUtil.i_dev("현재 검색된 영화 수: ${searchedMoviesList.size}")
        for (item in searchedMoviesList) {
//            LogUtil.i_dev("영화: ${item}")
        }

        LazyColumn(
            state = scrollState
        ) {
            itemsIndexed(
                searchedMoviesList,
                key = { index, item ->
                    item.hashCode()
                }
            ) { index, item ->
//            LogUtil.d_dev("NavController: ${navController.currentDestination}\nindex: ${index} / item: ${item?.title}")
                searchMovieScreenViewModelPresenter.invoke(SearchMovieScreenViewModelPresenter.UpdateCurrentPosition(index))
//                searchMovieScreenViewModel.currentPosition.value = index
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
            if(!isLoading && currentPosition >= (searchedMoviesList.size - 5)) {
                LogUtil.d_dev("MYTAG Request new item ${searchedMoviesList.size}")
                searchMovieScreenViewModelPresenter.invoke(SearchMovieScreenViewModelPresenter.GetSearchMovies(searchMovieSearchText, false))
//                searchMovieScreenViewModel.getSearchMovies(searchMovieScreenViewModel.searchMovieSearchText.value, false)
            }
        }
    }
}