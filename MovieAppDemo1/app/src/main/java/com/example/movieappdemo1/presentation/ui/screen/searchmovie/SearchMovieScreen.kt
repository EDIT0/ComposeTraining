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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.presentation.ui.screen.home.moveToMovieInfo
import com.example.movieappdemo1.presentation.ui.screen.movielist.MovieItem
import com.example.movieappdemo1.presentation.ui.screen.savedmovie.SavedMovieActionBar
import com.example.movieappdemo1.presentation.ui.screen.savedmovie.SavedMovieScreenViewModel
import com.example.movieappdemo1.presentation.ui.screen.savedmovie.SavedMoviesList
import com.example.movieappdemo1.ui.theme.LightGray
import com.example.movieappdemo1.ui.theme.White
import kotlinx.coroutines.launch

@Preview
@Composable
fun SearchMovieScreenPreview() {
    val bottomNavController = rememberNavController()
    val navController = rememberNavController()
    SearchMovieScreen(bottomNavController, navController, hiltViewModel())
}

@Composable
fun SearchMovieScreen(
    bottomNavController: NavController,
    navController: NavController,
    searchMovieScreenViewModel: SearchMovieScreenViewModel
) {
    val searchedMoviesList = searchMovieScreenViewModel.searchedMovies

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        SearchMovieActionBar(searchMovieScreenViewModel)
        SearchMoviesList(navController, searchMovieScreenViewModel, searchedMoviesList)
    }
}

@Composable
fun SearchMovieActionBar(
    searchMovieScreenViewModel: SearchMovieScreenViewModel
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
            value = searchMovieScreenViewModel.searchMovieSearchText.value,
            onValueChange = {
                searchMovieScreenViewModel.searchMovieSearchText.value = it
                searchMovieScreenViewModel.getSearchMovies(it, true)
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
    searchMovieScreenViewModel: SearchMovieScreenViewModel,
    list: List<MovieModelResult>
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val cantScrollForward = !scrollState.canScrollForward
    val cantScrollBackward = !scrollState.canScrollBackward

    if (list.isEmpty()) {
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
        LogUtil.i_dev("현재 검색된 영화 수: ${list.size}")
        for (item in list) {
//            LogUtil.i_dev("영화: ${item}")
        }

        LazyColumn(
            state = scrollState
        ) {
            itemsIndexed(
                list,
                key = { index, item ->
                    item.hashCode()
                }
            ) { index, item ->
//            LogUtil.d_dev("NavController: ${navController.currentDestination}\nindex: ${index} / item: ${item?.title}")
                searchMovieScreenViewModel.currentPosition.value = index
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
            if(
                !searchMovieScreenViewModel.isLoading.value
                && searchMovieScreenViewModel.currentPosition.value >= (searchMovieScreenViewModel.searchedMovies.size - 5)
            ) {
                LogUtil.d_dev("MYTAG Request new item ${searchMovieScreenViewModel.searchedMovies.size}")
                searchMovieScreenViewModel.getSearchMovies(searchMovieScreenViewModel.searchMovieSearchText.value, false)
            }
        }
    }
}