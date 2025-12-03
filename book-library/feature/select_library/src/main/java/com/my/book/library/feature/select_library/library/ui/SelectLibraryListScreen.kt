package com.my.book.library.feature.select_library.library.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.my.book.library.core.common.CommonMainViewModel
import com.my.book.library.core.common.component.CommonActionBar
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.core.resource.R
import com.my.book.library.feature.select_library.library.intent.SelectLibraryListViewModelEvent
import com.my.book.library.feature.select_library.library.state.SelectLibraryListUiState
import com.my.book.library.feature.select_library.library.viewmodel.SelectLibraryListViewModel

@Composable
fun SelectLibraryListScreen(
    commonMainViewModel: CommonMainViewModel,
    onMoveToLibraryDetail: () -> Unit, // TODO 도서관 정보 객체
    onBackPressed: () -> Unit,
    modifier: Modifier,
    detailRegion: LibraryData.DetailRegion
) {

    LogUtil.d_dev("받은 데이터: ${detailRegion}")
    val context = LocalContext.current

    val commonMainViewModel = commonMainViewModel
    val selectLibraryListViewModel = hiltViewModel<SelectLibraryListViewModel>()

    /**
     * 최초 실행 분기
     */
    var initExecute by rememberSaveable {
        mutableStateOf(true)
    }

    val selectLibraryListUiState = selectLibraryListViewModel.selectLibraryListUiState.collectAsStateWithLifecycle()
    val libraryListPaging = selectLibraryListUiState.value.libraryList?.collectAsLazyPagingItems()

    // DetailRegion 저장 - detailRegion이 변경될 때만 실행
    LaunchedEffect(detailRegion) {
        LogUtil.d_dev("LaunchedEffect 실행 - DetailRegion 저장: $detailRegion")
        selectLibraryListViewModel.intentAction(
            SelectLibraryListViewModelEvent.UpdateDetailRegion(detailRegion = detailRegion)
        )
    }

    LogUtil.d_dev("SelectLibraryListUiState: ${selectLibraryListUiState.value}")

    SelectLibraryContent(
        localContext = context,
        onMoveToLibraryDetail = onMoveToLibraryDetail,
        onBackPressed = {
            onBackPressed.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        selectLibraryListUiState = selectLibraryListUiState,
        libraryListPaging = libraryListPaging
    )

    LaunchedEffect(Unit) {
        if(initExecute) {
            initExecute = false
            selectLibraryListViewModel.intentAction(SelectLibraryListViewModelEvent.RequestLibraryList())
        }
    }
}

@Composable
fun SelectLibraryContent(
    localContext: Context,
    onMoveToLibraryDetail: () -> Unit, // TODO 도서관 정보 객체
    onBackPressed: () -> Unit,
    modifier: Modifier,
    selectLibraryListUiState: State<SelectLibraryListUiState>,
    libraryListPaging: LazyPagingItems<ResSearchBookLibrary.ResponseData.LibraryWrapper>?
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CommonActionBar(
                context = localContext,
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                actionBarTitle = localContext.getString(R.string.select_library_list),
                isShowBackButton = true,
                onBackClick = {
                    onBackPressed.invoke()
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    content = {
                        items(libraryListPaging?.itemCount?:0) {index ->
                            libraryListPaging?.get(index).let { library ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .noRippleClickable {
//                                    onMoveToDetailRegion.invoke(item)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 20.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = library?.lib?.libName?:""
                                    )
                                }
                            }
                        }

                        libraryListPaging?.loadState?.let { loadStatus ->
                            LogUtil.i_dev("MYTAG addLoadStateListener ${loadStatus}")

                            when (loadStatus.source.append) {
                                is LoadState.Error -> {
                                    LogUtil.e_dev("MYTAG append LoadState.Error")
//                                    isShowLoading.value = false
//                                    item {
//                                        RetryView(
//                                            localContext = localContext,
//                                            retry = {
//                                                movieListPaging.retry()
//                                            },
//                                            message = (loadStatus.source.append as LoadState.Error).error.localizedMessage
//                                                ?: ""
//                                        )
//                                    }
                                }

                                is LoadState.Loading -> {
                                    LogUtil.d_dev("MYTAG append LoadState.Loading")
//                                    isShowLoading.value = true
//                                    item {
//                                        ListLoadingView()
//                                    }
                                }

                                is LoadState.NotLoading -> {
                                    LogUtil.d_dev("MYTAG append LoadState.NotLoading")
//                                    isShowLoading.value = false
                                }
                            }

                            when (loadStatus.source.prepend) {
                                is LoadState.Error -> {
                                    LogUtil.e_dev("MYTAG prepend LoadState.Error")
//                                    item {
//                                        RetryView(
//                                            localContext = localContext,
//                                            retry = {
//                                                movieListPaging.retry()
//                                            },
//                                            message = (loadStatus.source.prepend as LoadState.Error).error.localizedMessage
//                                                ?: ""
//                                        )
//                                    }
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
//                                    item {
//                                        RetryView(
//                                            localContext = localContext,
//                                            retry = {
//                                                movieListPaging.retry()
//                                            },
//                                            message = (loadStatus.source.refresh as LoadState.Error).error.localizedMessage
//                                                ?: ""
//                                        )
//                                    }
//                                    isShowLoading.value = false
                                }

                                is LoadState.Loading -> {
                                    LogUtil.d_dev("MYTAG refresh LoadState.Loading")
//                                    isShowLoading.value = true
//                                    item {
//                                        ListLoadingView()
//                                    }
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