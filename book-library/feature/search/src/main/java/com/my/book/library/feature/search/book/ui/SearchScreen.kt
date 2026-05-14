package com.my.book.library.feature.search.book.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.core.common.component.ListLoadingView
import com.my.book.library.core.common.component.RetryView
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.model.res.ResSearchBook
import com.my.book.library.core.resource.Black
import com.my.book.library.core.resource.R
import com.my.book.library.feature.search.book.intent.SearchViewModelEvent
import com.my.book.library.feature.search.book.state.SearchUiState
import com.my.book.library.feature.search.book.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    commonViewModel: CommonViewModel,
    onBackPressed: () -> Unit,
    modifier: Modifier
) {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val commonViewModel = commonViewModel
    val searchViewModel = hiltViewModel<SearchViewModel>()

    val searchUiState = searchViewModel.searchUiState.collectAsStateWithLifecycle()
    val bookListPaging = searchUiState.value.bookList?.collectAsLazyPagingItems()

    val lifecycleResult = remember {
        object : LifecycleResult {
            override fun onEnter() {}
            override fun onStart() {}
            override fun onResume() {}
            override fun onPause() {}
            override fun onStop() {}
            override fun onDispose() {}
        }
    }

    // TODO 임시 검색어 삭제
    LaunchedEffect(true) {
        searchViewModel.intentAction(SearchViewModelEvent.RequestSearchBook(keyword = "편의점"))
    }

    SearchContent(
        localContext = localContext,
        onBackPressed = onBackPressed,
        modifier = Modifier,
        searchViewModelEvent = {
            searchViewModel.intentAction(it)
        },
        searchUiState = searchUiState,
        bookListPaging = bookListPaging,
    )

    LifecycleListener(
        lifecycleOwner = lifecycleOwner,
        screenName = object {}.javaClass.enclosingClass?.simpleName ?: "SearchScreen",
        lifecycleResult = lifecycleResult
    )
}

@Composable
fun SearchContent(
    localContext: Context,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    searchViewModelEvent: (SearchViewModelEvent) -> Unit,
    searchUiState: State<SearchUiState>,
    bookListPaging: LazyPagingItems<ResSearchBook.ResponseData.BookWrapper>?
) {

    SystemBarController.Setup(
        config = SystemBarConfig(
            statusBarColor = Black,
            statusBarDarkIcons = false,
            useStatusBarSpace = true,
            navigationBarColor = Black,
            navigationBarDarkIcons = false,
            useNavigationBarSpace = true
        )
    ) { state ->
        Scaffold(
            modifier = Modifier
                .padding(top = state.statusBarHeight, bottom = state.navigationBarHeight)
                .consumeWindowInsets(WindowInsets.statusBars)
                .consumeWindowInsets(WindowInsets.navigationBars)
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        // 로딩이 완료되고 데이터가 없을 때만 "데이터가 없습니다" 표시
                        val isLoadingComplete = bookListPaging?.loadState?.refresh is LoadState.NotLoading
                        val hasNoData = bookListPaging?.itemCount == 0

                        if (isLoadingComplete && hasNoData) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = localContext.getString(R.string.common_component_no_item))
                            }
                        }

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f),
                            content = {
                                items(bookListPaging?.itemCount ?: 0) { index ->
                                    bookListPaging?.get(index).let { book ->
//                                        LibraryItemView(
//                                            context = localContext,
//                                            libraryInfo = library,
//                                            onMoveToLibraryDetail = onMoveToLibraryDetail,
//                                            selectLibraryListUiState = selectLibraryListUiState
//                                        )
                                        // TODO 책 아이템 UI 필요
                                        Text(
                                            text = "${book.toString()}"
                                        )
                                    }
                                }

                                bookListPaging?.loadState?.let { loadStatus ->
                                    LogUtil.i_dev("MYTAG addLoadStateListener ${loadStatus}")

                                    when (loadStatus.source.append) {
                                        is LoadState.Error -> {
                                            LogUtil.e_dev("MYTAG append LoadState.Error")
                                            item {
                                                RetryView(
                                                    localContext = localContext,
                                                    retry = {
                                                        bookListPaging.retry()
                                                    },
                                                    message = (loadStatus.source.refresh as LoadState.Error).error.localizedMessage
                                                )
                                            }
                                        }

                                        is LoadState.Loading -> {
                                            LogUtil.d_dev("MYTAG append LoadState.Loading")
                                            item {
                                                ListLoadingView()
                                            }
                                        }

                                        is LoadState.NotLoading -> {
                                            LogUtil.d_dev("MYTAG append LoadState.NotLoading")
                                        }
                                    }

                                    when (loadStatus.source.prepend) {
                                        is LoadState.Error -> {
                                            LogUtil.e_dev("MYTAG prepend LoadState.Error")
                                            item {
                                                RetryView(
                                                    localContext = localContext,
                                                    retry = {
                                                        bookListPaging.retry()
                                                    },
                                                    message = (loadStatus.source.refresh as LoadState.Error).error.localizedMessage
                                                )
                                            }
                                        }

                                        is LoadState.Loading -> {
                                            LogUtil.d_dev("MYTAG prepend LoadState.Loading")
                                        }

                                        is LoadState.NotLoading -> {
                                            LogUtil.d_dev("MYTAG prepend LoadState.NotLoading")
                                        }
                                    }

                                    when (loadStatus.source.refresh) {
                                        is LoadState.Error -> {
                                            LogUtil.e_dev("MYTAG refresh LoadState.Error")
                                            item {
                                                RetryView(
                                                    localContext = localContext,
                                                    retry = {
                                                        bookListPaging.retry()
                                                    },
                                                    message = (loadStatus.source.refresh as LoadState.Error).error.localizedMessage
                                                )
                                            }
                                        }

                                        is LoadState.Loading -> {
                                            LogUtil.d_dev("MYTAG refresh LoadState.Loading")
                                            item {
                                                ListLoadingView()
                                            }
                                        }

                                        is LoadState.NotLoading -> {
                                            LogUtil.d_dev("MYTAG refresh LoadState.NotLoading")
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchContentUiPreview() {

}