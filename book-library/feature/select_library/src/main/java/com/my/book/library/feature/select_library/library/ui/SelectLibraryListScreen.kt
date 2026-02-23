package com.my.book.library.feature.select_library.library.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.CommonActionBar
import com.my.book.library.core.common.component.ListLoadingView
import com.my.book.library.core.common.component.RetryView
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.highlightKeywords
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.Gray50
import com.my.book.library.core.resource.Gray500
import com.my.book.library.core.resource.Gray600
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.core.resource.R
import com.my.book.library.feature.select_library.library.intent.SelectLibraryListViewModelEvent
import com.my.book.library.feature.select_library.library.state.SelectLibraryListUiState
import com.my.book.library.feature.select_library.library.viewmodel.SelectLibraryListViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun SelectLibraryListScreen(
    commonViewModel: CommonViewModel,
    onMoveToLibraryDetail: (ResSearchBookLibrary.ResponseData.LibraryWrapper, LibraryData.DetailRegion) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    detailRegion: LibraryData.DetailRegion
) {

    LogUtil.d_dev("받은 데이터: ${detailRegion}")
    val context = LocalContext.current

    val commonViewModel = commonViewModel
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

    SelectLibraryListContent(
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
            // 도서관 리스트 호출
            selectLibraryListViewModel.intentAction(SelectLibraryListViewModelEvent.RequestLibraryList())
        }
    }
}

@Composable
fun SelectLibraryListContent(
    localContext: Context,
    onMoveToLibraryDetail: (ResSearchBookLibrary.ResponseData.LibraryWrapper, LibraryData.DetailRegion) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    selectLibraryListUiState: State<SelectLibraryListUiState>,
    libraryListPaging: LazyPagingItems<ResSearchBookLibrary.ResponseData.LibraryWrapper>?
) {

    Scaffold() { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
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
                    // 로딩이 완료되고 데이터가 없을 때만 "데이터가 없습니다" 표시
                    val isLoadingComplete = libraryListPaging?.loadState?.refresh is LoadState.NotLoading
                    val hasNoData = libraryListPaging?.itemCount == 0

                    if(isLoadingComplete && hasNoData) {
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
                            items(libraryListPaging?.itemCount?:0) {index ->
                                libraryListPaging?.get(index).let { library ->
                                    LibraryItemView(
                                        context = localContext,
                                        libraryInfo = library,
                                        onMoveToLibraryDetail = onMoveToLibraryDetail,
                                        selectLibraryListUiState = selectLibraryListUiState
                                    )
                                }
                            }

                            libraryListPaging?.loadState?.let { loadStatus ->
                                LogUtil.i_dev("MYTAG addLoadStateListener ${loadStatus}")

                                when (loadStatus.source.append) {
                                    is LoadState.Error -> {
                                        LogUtil.e_dev("MYTAG append LoadState.Error")
                                        item {
                                            RetryView(
                                                localContext = localContext,
                                                retry = {
                                                    libraryListPaging.retry()
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
                                                    libraryListPaging.retry()
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
                                                    libraryListPaging.retry()
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

/**
 * 도서관 아이템 뷰
 *
 * @param libraryInfo
 */
@Composable
fun LibraryItemView(
    context: Context,
    libraryInfo: ResSearchBookLibrary.ResponseData.LibraryWrapper?,
    onMoveToLibraryDetail: (ResSearchBookLibrary.ResponseData.LibraryWrapper, LibraryData.DetailRegion) -> Unit,
    selectLibraryListUiState: State<SelectLibraryListUiState>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable {
                libraryInfo?.let {
                    onMoveToLibraryDetail.invoke(it, selectLibraryListUiState.value.detailRegion!!)
                }
            }
            .padding(horizontal = 20.dp, vertical = 7.dp)
            .border(
                width = 1.dp,
                color = Gray500,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = Gray50,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = libraryInfo?.lib?.libName?:"",
            style = TextStyle(fontSize = dpToSp(16.dp))
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = ("${context.getString(R.string.select_library_list_address)} " + libraryInfo?.lib?.address?:"-")
                .highlightKeywords(
                    keywords = listOf("${context.getString(R.string.select_library_list_address)} "),
                    highlightStyle = SpanStyle(
                        color = Gray600,
                        fontSize = dpToSp(13.dp),
                        fontWeight = FontWeight.Bold
                    )
                ),
            style = TextStyle(fontSize = dpToSp(13.dp))
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = ("${context.getString(R.string.select_library_list_connect_number)} " + libraryInfo?.lib?.tel?:"-")
                .highlightKeywords(
                    keywords = listOf("${context.getString(R.string.select_library_list_connect_number)} "),
                    highlightStyle = SpanStyle(
                        color = Gray600,
                        fontSize = dpToSp(13.dp),
                        fontWeight = FontWeight.Bold
                    )
                ),
            style = TextStyle(fontSize = dpToSp(13.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectLibraryUiPreview() {
    val mockData = listOf(
        ResSearchBookLibrary.ResponseData.LibraryWrapper(
            lib =
                ResSearchBookLibrary.ResponseData.LibraryWrapper.LibraryInfo(
                    libCode = "123456",
                    libName = "서울시립도서관",
                    address = "서울시 강남구",
                    tel = "02-1234-5678",
                    fax = "02-1234-5679",
                    latitude = "37.5665",
                    longitude = "126.9780",
                    homepage = "https://library.seoul.kr",
                    closed = "월요일",
                    operatingTime = "09:00-18:00",
                    bookCount = "50000"
                )
        ),
        ResSearchBookLibrary.ResponseData.LibraryWrapper(
            lib =
                ResSearchBookLibrary.ResponseData.LibraryWrapper.LibraryInfo(
                    libCode = "234567",
                    libName = "국립중앙도서관",
                    address = "서울시 서초구",
                    tel = "02-2222-3333",
                    fax = "02-2222-3334",
                    latitude = "37.5026",
                    longitude = "127.0370",
                    homepage = "https://nl.go.kr",
                    closed = "월요일",
                    operatingTime = "09:00-21:00",
                    bookCount = "100000"
                )
        )
    )

    val pagingData = PagingData.from(mockData)
    val flow = flowOf(pagingData)

    SelectLibraryListContent(
        localContext = LocalContext.current,
        onMoveToLibraryDetail = {_, _ -> },
        onBackPressed = {},
        modifier = Modifier,
        selectLibraryListUiState = remember {
            mutableStateOf(
                SelectLibraryListUiState()
            )
        },
        libraryListPaging = flow.collectAsLazyPagingItems()
    )
}