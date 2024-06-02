package com.example.movieappdemo1.presentation.ui.screen.savedmovie

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.movieappdemo1.ui.theme.LightGray
import com.example.movieappdemo1.ui.theme.White

@Preview
@Composable
fun SavedMovieScreenPreview() {
    val bottomNavController = rememberNavController()
    val navController = rememberNavController()
    SavedMovieScreen(bottomNavController, navController, hiltViewModel())
}

@Composable
fun SavedMovieScreen(
    bottomNavController: NavController,
    navController: NavController,
    savedMovieScreenViewModel: SavedMovieScreenViewModel
) {
//    val allSavedMoviesList = savedMovieScreenViewModel.allSavedMoviesList.collectAsState(initial = emptyList())
    val allSavedMoviesList = savedMovieScreenViewModel.getSearchSavedMoviesStateFlow.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        SavedMovieActionBar(savedMovieScreenViewModel)
        SavedMoviesList(navController, savedMovieScreenViewModel, allSavedMoviesList.value)
    }
}

@Composable
fun SavedMovieActionBar(
    savedMovieScreenViewModel: SavedMovieScreenViewModel
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
            value = savedMovieScreenViewModel.savedMovieSearchText.value,
            onValueChange = {
                savedMovieScreenViewModel.savedMovieSearchText.value = it
                savedMovieScreenViewModel.keyword.value = it
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SavedMoviesList(
    navController: NavController,
    savedMovieScreenViewModel: SavedMovieScreenViewModel,
    list: List<MovieModelResult>
) {
    if(list.isEmpty()) {
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
    } else {
        LogUtil.i_dev("현재 저장된 영화 수: ${list.size}")
        for(item in list) {
            LogUtil.i_dev("영화: ${item}")
        }

        LazyColumn {
            itemsIndexed(
                list,
                key = { index, item ->
                    item.hashCode()
                }
            ) {index, item ->
//            LogUtil.d_dev("NavController: ${navController.currentDestination}\nindex: ${index} / item: ${item?.title}")
                savedMovieScreenViewModel.currentPosition.value = index
                MovieItem(index, item,
                    onItemClick = {
                        moveToMovieInfo(navController, it)
                    },
                    onItemLongClick = {
                        savedMovieScreenViewModel.deleteSavedMovie(it)
                    }
                )

//                val currentItem = rememberUpdatedState(item)
//                val dismissState = androidx.compose.material.rememberDismissState(
//                    confirmStateChange = {
//                        savedMovieScreenViewModel.deleteSavedMovie(currentItem.value)
//                        true
//                    }
//                )
//                SwipeToDismiss(
//                    state = dismissState,
//                    background = {
//                        dismissState.dismissDirection ?: return@SwipeToDismiss
//                        Box(modifier = Modifier.fillMaxSize().background(Red))
//                    },
//                    dismissContent = {
//                        MovieItem(index, item) {
//                            moveToMovieInfo(navController, it)
//                        }
//                    }
//                )

//                LazyColumn(modifier = Modifier.fillMaxHeight()) {
//                    items(
//                        items = myListState.value,
//                        key = { todoItem -> todoItem.id }
//                    ) { item ->
//                        val dismissState = rememberDismissState(
//                            confirmStateChange = {
//                                viewModel.removeItem(item)
//                                true
//                            }
//                        )
//
//                        SwipeToDismiss(
//                            state = dismissState,
//                            background = {
//                                dismissState.dismissDirection ?: return@SwipeToDismiss
//                                Box(modifier = Modifier.fillMaxSize().background(Color.Red))
//                            },
//                            dismissContent = {
//                                // The row view of each item
//                            }
//                        )
//                    }
//                }

//                val dismissState = androidx.compose.material.rememberDismissState()
//
//                if (dismissState.isDismissed(androidx.compose.material.DismissDirection.EndToStart)) {
//                    savedMovieScreenViewModel.deleteSavedMovie(item)
//                }
//                SwipeToDismiss(
//                    state = dismissState,
//                    modifier = Modifier
//                        .padding(vertical = Dp(1f)),
//                    directions = setOf(
//                        androidx.compose.material.DismissDirection.EndToStart
//                    ),
//                    dismissThresholds = { direction ->
//                        FractionalThreshold(if (direction == androidx.compose.material.DismissDirection.EndToStart) 0.1f else 0.05f)
//                    },
//                    background = {
//                        val color = animateColorAsState(
//                            when (dismissState.targetValue) {
//                                DismissValue.Default -> White
//                                else -> LightGray
//                            }
//                        )
//                        val alignment = Alignment.CenterEnd
//                        val icon = Icons.Default.Delete
//
//                        val scale = animateFloatAsState(
//                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
//                        )
//
//                        Box(
//                            Modifier
//                                .fillMaxSize()
//                                .background(color.value)
//                                .padding(horizontal = Dp(20f)),
//                            contentAlignment = alignment
//                        ) {
//                            Icon(
//                                icon,
//                                contentDescription = "Delete Icon",
//                                modifier = Modifier.scale(scale.value)
//                            )
//                        }
//                    },
//                    dismissContent = {
//                        Card(
//                            elevation = animateDpAsState(
//                                if (dismissState.dismissDirection != null) 4.dp else 0.dp
//                            ).value,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .wrapContentHeight()
//                                .align(alignment = Alignment.CenterVertically)
//                        ) {
//                            MovieItem(index, item) {
//                                moveToMovieInfo(navController, it)
//                            }
//                        }
//                    }
//                )
            }
        }
    }
}