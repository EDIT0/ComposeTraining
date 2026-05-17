package com.my.book.library.feature.search.book.ui

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.CommonSearchBar
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.core.common.component.ListLoadingView
import com.my.book.library.core.common.component.RetryView
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.model.res.ResSearchBook
import com.my.book.library.core.resource.NotoSansKR
import com.my.book.library.core.resource.R
import com.my.book.library.feature.search.book.intent.SearchViewModelEvent
import com.my.book.library.feature.search.book.state.SearchUiState
import com.my.book.library.feature.search.book.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.flowOf

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

    LaunchedEffect(key1 = Unit) {
        searchViewModel.sideEffectEvent.collect {
            when(it) {
                is SearchViewModel.SideEffectEvent.ShowToast -> {
                    Toast.makeText(localContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
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

    val useStatusBarSpace = false
    val useNavigationBarSpace = true

    SystemBarController.Setup(
        config = SystemBarConfig(
            statusBarColor = colorResource(R.color.color_FFFFFFFF),
            statusBarDarkIcons = true,
            useStatusBarSpace = useStatusBarSpace,
            navigationBarColor = colorResource(R.color.color_FFFFFFFF),
            navigationBarDarkIcons = true,
            useNavigationBarSpace = useNavigationBarSpace
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
                    .background(color = colorResource(R.color.color_FFFFFFFF))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .noRippleClickable {
                                    onBackPressed.invoke()
                                }
                                .padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_arrow_left_black_40x40),
                                contentDescription = null,
                            )
                        }

                        CommonSearchBar(
                            modifier = Modifier
                                .padding(top = 12.dp, start = 0.dp, end = 20.dp, bottom = 12.dp)
                                .fillMaxWidth(),
                            isFakeSearchBar = false,
                            backgroundColorRes = R.color.color_F2F4F6,
                            cornerRadius = 8.dp,
                            hint = stringResource(R.string.main_home_fake_search_hint),
                            hintColorRes = R.color.color_8B95A1,
                            textSize = 16.dp,
                            query = searchUiState.value.keyword,
                            focusRequester = remember { FocusRequester() },
                            showKeyboardOnStart = true,
                            onQueryChange = {
                                searchViewModelEvent.invoke(SearchViewModelEvent.RequestUpdateKeyword(keyword = it))
                            },
                            onSearchClick = {
                                searchViewModelEvent.invoke(SearchViewModelEvent.RequestSearchBook(keyword = searchUiState.value.keyword))
                            },
                            onCancelClick = {
                                searchViewModelEvent.invoke(SearchViewModelEvent.RequestUpdateKeyword(keyword = ""))
                            },
                            onFakeBarClick = {}
                        )
                    }

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
                                SearchStateView(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(horizontal = 20.dp),
                                    drawableRes = R.drawable.ic_search_fail_grey_27x26,
                                    title = "\"${searchUiState.value.searchInfo?.request?.keyword}\"에 대한\n" + stringResource(R.string.search_book_no_result_title),
                                    subtitle = stringResource(R.string.search_book_no_result_subtitle)
                                )
                            }
                        }

                        /**
                         * LazyColumn 안의 item에서는 fillMaxSize()나 fillMaxHeight()가 동작하지 않습니다.
                         * LazyColumn은 자식의 높이를 측정하지 않고 무한히 스크롤 가능한 구조이기 때문입니다.
                         * BoxWithConstraints를 사용하면 현재 레이아웃에서 사용 가능한 최대 높이(maxHeight)를 측정할 수 있고,
                         * 이 값을 item 내부에 .height(maxHeight)로 직접 전달해서 화면 전체 높이를 차지하게 만들 수 있습니다.
                         * 즉, fillMaxHeight() 대신 실제 픽셀 값으로 높이를 지정하기 위한 용도입니다.
                         */
                        BoxWithConstraints(
                            modifier = Modifier.weight(1f)
                        ) {
                            val maxHeight = this.maxHeight

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize(),
                                content = {

                                    if(searchUiState.value.searchInfo != null) {
                                        item {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(
                                                        color = colorResource(R.color.color_F9FAFB)
                                                    )
                                                    .padding(horizontal = 20.dp, vertical = 16.dp)
                                            ) {
                                                Text(
                                                    text = stringResource(R.string.search_book_result_count, searchUiState.value.searchInfo?.request?.keyword ?: "", searchUiState.value.searchInfo?.numFound ?: 0),
                                                    style = TextStyle(
                                                        color = colorResource(R.color.color_6B7684),
                                                        fontSize = dpToSp(13.dp),
                                                        lineHeight = dpToSp(20.dp),
                                                        fontFamily = NotoSansKR,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                )
                                            }
                                        }

                                        items(bookListPaging?.itemCount ?: 0) { index ->
                                            bookListPaging?.get(index).let { book ->
                                                BookItemView(
                                                    data = book!!,
                                                    onItemClick = {
                                                        LogUtil.d_dev("클릭된 책: ${it.doc.bookName}")
                                                    }
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
                                    } else {
                                        item {
                                            SearchStateView(
                                                modifier = Modifier.height(maxHeight),
                                                drawableRes = R.drawable.ic_search_grey_27x28,
                                                title = stringResource(R.string.search_book_empty_title),
                                                subtitle = stringResource(R.string.search_book_empty_subtitle)
                                            )
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
}

@Composable
fun BookItemView(
    data: ResSearchBook.ResponseData.BookWrapper,
    onItemClick: (ResSearchBook.ResponseData.BookWrapper) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable {
                onItemClick.invoke(data)
            }
            .background(
                color = colorResource(R.color.color_FFFFFFFF),
            )
            .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(56.dp)
                .height(80.dp)
                .background(
                    color = colorResource(R.color.color_FFFFFFFF),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            var contentScale by remember {
                mutableStateOf<ContentScale>(ContentScale.Inside)
            }

            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = data.doc.bookImageUrl,
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = contentScale,
                placeholder = painterResource(R.drawable.ic_book_grey_21x18),
                error = painterResource(R.drawable.ic_book_grey_21x18),
                onError = { error ->
                    contentScale = ContentScale.Inside
                    LogUtil.e_dev("book image load error: ${error.result.throwable}")
                },
                onSuccess = {
                    contentScale = ContentScale.Crop
                    LogUtil.d_dev("book image success, url: ${data.doc.bookImageUrl}")
                }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "${data.doc.bookName}",
                style = TextStyle(
                    color = colorResource(R.color.color_191F28),
                    fontSize = dpToSp(16.dp),
                    lineHeight = dpToSp(24.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.search_book_authors, data.doc.authors ?: ""),
                style = TextStyle(
                    color = colorResource(R.color.color_6B7684),
                    fontSize = dpToSp(13.dp),
                    lineHeight = dpToSp(20.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = stringResource(R.string.search_book_publisher_year, data.doc.publisher ?: "", data.doc.publicationYear ?: ""),
                style = TextStyle(
                    color = colorResource(R.color.color_6B7684),
                    fontSize = dpToSp(13.dp),
                    lineHeight = dpToSp(20.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Image(
            painter = painterResource(R.drawable.ic_arrow_right_grey_7x10),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun SearchStateView(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
    title: String,
    subtitle: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(drawableRes),
            contentDescription = null,
            modifier = Modifier
                .background(
                    color = colorResource(R.color.color_F9FAFB),
                    shape = RoundedCornerShape(9999.dp)
                )
                .padding(horizontal = 18.dp, vertical = 19.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = TextStyle(
                color = colorResource(R.color.color_191F28),
                fontSize = dpToSp(20.dp),
                lineHeight = dpToSp(28.dp),
                fontFamily = NotoSansKR,
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subtitle,
            style = TextStyle(
                color = colorResource(R.color.color_8B95A1),
                fontSize = dpToSp(13.dp),
                lineHeight = dpToSp(20.dp),
                fontFamily = NotoSansKR,
                fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookItemViewPreview() {
    BookItemView(
        data = ResSearchBook.ResponseData.BookWrapper(
            doc = ResSearchBook.ResponseData.BookWrapper.BookInfo(
                bookName = "코틀린 완벽 가이드",
                authors = "지은이: 조세핀 홍",
                publisher = "한빛미디어",
                publicationYear = "2023",
                isbn13 = "9791162245678",
                additionSymbol = null,
                vol = null,
                classNo = "005.133",
                classNm = "프로그래밍 언어",
                bookImageUrl = null,
                bookDtlUrl = null,
                loanCount = "120"
            )
        ),
        onItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SearchContentUiPreview() {
    val mockData = listOf(
        ResSearchBook.ResponseData.BookWrapper(
            doc = ResSearchBook.ResponseData.BookWrapper.BookInfo(
                bookName = "코틀린 완벽 가이드",
                authors = "지은이: 조세핀 홍",
                publisher = "한빛미디어",
                publicationYear = "2023",
                isbn13 = "9791162245678",
                additionSymbol = null,
                vol = null,
                classNo = "005.133",
                classNm = "프로그래밍 언어",
                bookImageUrl = null,
                bookDtlUrl = null,
                loanCount = "120"
            )
        ),
        ResSearchBook.ResponseData.BookWrapper(
            doc = ResSearchBook.ResponseData.BookWrapper.BookInfo(
                bookName = "Clean Architecture",
                authors = "지은이: 로버트 C. 마틴",
                publisher = "인사이트",
                publicationYear = "2019",
                isbn13 = "9788966262472",
                additionSymbol = null,
                vol = null,
                classNo = "005.1",
                classNm = "소프트웨어 공학",
                bookImageUrl = null,
                bookDtlUrl = null,
                loanCount = "95"
            )
        )
    )

    val pagingData = PagingData.from(mockData)
    val flow = flowOf(pagingData)

    SearchContent(
        localContext = LocalContext.current,
        onBackPressed = {},
        modifier = Modifier,
        searchViewModelEvent = {},
        searchUiState = remember { mutableStateOf(SearchUiState()) },
        bookListPaging = flow.collectAsLazyPagingItems()
    )
}