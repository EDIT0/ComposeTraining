package com.example.movieappdemo1.presentation.ui.screen.savedmovie

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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.presentation.ui.screen.home.moveToMovieInfo
import com.example.movieappdemo1.presentation.ui.screen.movielist.MovieItem
import com.example.movieappdemo1.ui.theme.LightGray
import com.example.movieappdemo1.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun PreviewSavedMovieScreen() {
    val bottomNavController = rememberNavController()
    val navController = rememberNavController()

    UiSavedMovieScreen(
        navController,
        emptyList(),
        "SearchText",
        "Keyword",
        false,
        savedMovieScreenViewModelPresenter = { }
    )
}

sealed class SavedMovieScreenViewModelPresenter {
    class UpdateSearchText(val text: String) : SavedMovieScreenViewModelPresenter()
    class UpdateKeyword(val keyword: String) : SavedMovieScreenViewModelPresenter()
    class UpdateCurrentPosition(val position: Int) : SavedMovieScreenViewModelPresenter()
    class DeleteSavedMovie(val movieModelResult: MovieModelResult) : SavedMovieScreenViewModelPresenter()
}

@Composable
fun SavedMovieScreen(
    bottomNavController: NavController,
    navController: NavController,
    savedMovieScreenViewModel: SavedMovieScreenViewModel
) {
//    val allSavedMoviesList = savedMovieScreenViewModel.allSavedMoviesList.collectAsState(initial = emptyList())
    val allSavedMoviesList = savedMovieScreenViewModel.getSearchSavedMoviesStateFlow.collectAsState(initial = emptyList())

    val savedMovieSearchText = savedMovieScreenViewModel.savedMovieSearchText.value
    val keyword = savedMovieScreenViewModel.keyword.value

    val isDelete: Boolean = savedMovieScreenViewModel.isDelete.value

    UiSavedMovieScreen(
        navController,
        allSavedMoviesList.value,
        savedMovieSearchText,
        keyword?:"",
        isDelete,
        savedMovieScreenViewModelPresenter = {
            val callback : SavedMovieScreenViewModelPresenter = it
            when(callback) {
                is SavedMovieScreenViewModelPresenter.UpdateCurrentPosition -> {
                    savedMovieScreenViewModel.currentPosition.value = callback.position
                }
                is SavedMovieScreenViewModelPresenter.UpdateKeyword -> {
                    savedMovieScreenViewModel.keyword.value = callback.keyword
                }
                is SavedMovieScreenViewModelPresenter.UpdateSearchText -> {
                    savedMovieScreenViewModel.savedMovieSearchText.value = callback.text
                }
                is SavedMovieScreenViewModelPresenter.DeleteSavedMovie -> {
                    savedMovieScreenViewModel.deleteSavedMovie(callback.movieModelResult)
                }
            }
        }
    )

}

@Composable
fun UiSavedMovieScreen(
    navController: NavController,
    allSavedMoviesList: List<MovieModelResult>,
    savedMovieSearchText: String,
    keyword: String,
    isDelete: Boolean,
    savedMovieScreenViewModelPresenter: (SavedMovieScreenViewModelPresenter) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        SavedMovieActionBar(
            savedMovieSearchText,
            keyword,
            savedMovieScreenViewModelPresenter
        )
        SavedMoviesList(
            navController,
            scrollState,
            allSavedMoviesList,
            savedMovieScreenViewModelPresenter
        )
    }

    scrollLogic(isDelete, coroutineScope, scrollState)
}

@Composable
fun SavedMovieActionBar(
    savedMovieSearchText: String,
    keyword: String,
    savedMovieScreenViewModelPresenter : (SavedMovieScreenViewModelPresenter) -> Unit
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
            value = savedMovieSearchText,
            onValueChange = {
                savedMovieScreenViewModelPresenter(SavedMovieScreenViewModelPresenter.UpdateSearchText(it))
                savedMovieScreenViewModelPresenter(SavedMovieScreenViewModelPresenter.UpdateKeyword(it))
//                savedMovieScreenViewModel.savedMovieSearchText.value = it
//                savedMovieScreenViewModel.keyword.value = it
                LogUtil.i_dev("MYTAG 저장된 영화 검색어: ${keyword}")
            },
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = LightGray
            ),
            placeholder = {
                Text(text = "저장된 영화 검색")
            }
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SavedMoviesList(
    navController: NavController,
    scrollState: LazyListState,
    list: List<MovieModelResult>,
    savedMovieScreenViewModelPresenter: (SavedMovieScreenViewModelPresenter) -> Unit
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

        LazyColumn(
            state = scrollState
        ) {
            itemsIndexed(
                list,
                key = { index, item ->
                    item.hashCode()
                }
            ) {index, item ->
//            LogUtil.d_dev("NavController: ${navController.currentDestination}\nindex: ${index} / item: ${item?.title}")
                savedMovieScreenViewModelPresenter(SavedMovieScreenViewModelPresenter.UpdateCurrentPosition(index))
//                savedMovieScreenViewModel.currentPosition.value = index
                MovieItem(index, item,
                    onItemClick = {
                        moveToMovieInfo(navController, it)
                    },
                    onItemLongClick = {
                        savedMovieScreenViewModelPresenter(SavedMovieScreenViewModelPresenter.DeleteSavedMovie(it))
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

private fun scrollLogic(
    isDelete: Boolean,
    coroutineScope: CoroutineScope,
    scrollState: LazyListState
) {
    if(isDelete) {

    } else {
        scrollToTop(coroutineScope, scrollState)
    }
}

fun scrollToTop(coroutineScope: CoroutineScope, scrollState: LazyListState) {
    LogUtil.i_dev("MYTAG Scroll to top")
    coroutineScope.launch {
        scrollState.scrollToItem(0)
    }
}