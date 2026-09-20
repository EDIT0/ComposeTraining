package com.my.book.library.feature.main.ui.rank

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.model.local.BookRankType
import com.my.book.library.core.model.res.ResHotTrend
import com.my.book.library.core.model.res.ResLoanItemSrchByLib
import com.my.book.library.core.resource.NotoSansKR
import com.my.book.library.core.resource.R
import com.my.book.library.feature.main.intent.rank.BookRankViewModelEvent
import com.my.book.library.feature.main.state.rank.BookRankUiState
import com.my.book.library.feature.main.ui.common.BookCoverImage
import com.my.book.library.feature.main.viewmodel.rank.BookRankViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun BookRankScreen(
    bookRankType: BookRankType,
    commonViewModel: CommonViewModel,
    onBackPressed: () -> Unit,
    onMoveToBookRankDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current

    val commonViewModel = commonViewModel
    val bookRankViewModel = hiltViewModel<BookRankViewModel>()

    val bookRankUiState = bookRankViewModel.bookRankUiState.collectAsStateWithLifecycle()
    val popularLoanBooksPaging = bookRankUiState.value.popularLoanBooks?.collectAsLazyPagingItems()

    LaunchedEffect(bookRankType) {
        bookRankViewModel.intentAction(BookRankViewModelEvent.LoadBookRank(bookRankType))
    }

    BookRankContent(
        localContext = localContext,
        bookRankType = bookRankType,
        bookRankUiState = bookRankUiState,
        popularLoanBooksPaging = popularLoanBooksPaging,
        onBackPressed = onBackPressed,
        onMoveToBookRankDetail = onMoveToBookRankDetail,
        modifier = modifier
    )
}

@Composable
private fun BookRankContent(
    localContext: Context,
    bookRankType: BookRankType,
    bookRankUiState: State<BookRankUiState>,
    popularLoanBooksPaging: LazyPagingItems<ResLoanItemSrchByLib.ResponseData.DocWrapper>?,
    onBackPressed: () -> Unit,
    onMoveToBookRankDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val useStatusBarSpace = true
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
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = if (useStatusBarSpace) { state.statusBarHeight } else { 0.dp },
                    bottom = if (useNavigationBarSpace) { state.navigationBarHeight } else { 0.dp }
                )
                .consumeWindowInsets(WindowInsets.statusBars)
                .consumeWindowInsets(WindowInsets.navigationBars),
            topBar = {
                CommonActionBar(
                    context = localContext,
                    actionBarTitle = stringResource(
                        if (bookRankType == BookRankType.HOT_TREND) {
                            R.string.main_home_hot_issue_books_title
                        } else {
                            R.string.main_home_popular_loan_books_title
                        }
                    ),
                    isShowBackButton = true,
                    onBackClick = onBackPressed
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(color = colorResource(R.color.color_FFFFFFFF)),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                when (bookRankType) {
                    BookRankType.HOT_TREND -> {
                        val hotTrendBooks = bookRankUiState.value.hotTrendBooks

                        itemsIndexed(
                            items = hotTrendBooks,
                            key = { _, book -> book.isbn13 ?: book.bookname.orEmpty() }
                        ) { index, book ->
                            BookRankRow(
                                rank = index + 1,
                                bookname = book.bookname,
                                authors = book.authors,
                                publisher = book.publisher,
                                bookImageURL = book.bookImageURL,
                                onClick = {
                                    book.isbn13?.let(onMoveToBookRankDetail)
                                }
                            )
                        }

                        if (hotTrendBooks.isEmpty()) {
                            item { BookRankEmptyView() }
                        }
                    }

                    BookRankType.POPULAR_LOAN -> {
                        if (popularLoanBooksPaging != null) {
                            items(count = popularLoanBooksPaging.itemCount) { index ->
                                popularLoanBooksPaging[index]?.doc?.let { book ->
                                    BookRankRow(
                                        rank = index + 1,
                                        bookname = book.bookname,
                                        authors = book.authors,
                                        publisher = book.publisher,
                                        bookImageURL = book.bookImageURL,
                                        onClick = {
                                            book.isbn13?.let(onMoveToBookRankDetail)
                                        }
                                    )
                                }
                            }

                            val isLoadingComplete = popularLoanBooksPaging.loadState.source.refresh is LoadState.NotLoading
                            val hasNoData = popularLoanBooksPaging.itemCount == 0
                            if (isLoadingComplete && hasNoData) {
                                item { BookRankEmptyView() }
                            }

                            when (val appendState = popularLoanBooksPaging.loadState.source.append) {
                                is LoadState.Error -> {
                                    item {
                                        RetryView(
                                            localContext = localContext,
                                            retry = { popularLoanBooksPaging.retry() },
                                            message = appendState.error.localizedMessage ?: ""
                                        )
                                    }
                                }
                                is LoadState.Loading -> {
                                    item { ListLoadingView() }
                                }
                                is LoadState.NotLoading -> {}
                            }

                            when (val refreshState = popularLoanBooksPaging.loadState.source.refresh) {
                                is LoadState.Error -> {
                                    item {
                                        RetryView(
                                            localContext = localContext,
                                            retry = { popularLoanBooksPaging.retry() },
                                            message = refreshState.error.localizedMessage ?: ""
                                        )
                                    }
                                }
                                is LoadState.Loading -> {
                                    item { ListLoadingView() }
                                }
                                is LoadState.NotLoading -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * BookRank 전체보기 목록의 도서 1권 카드 — 표지 + (순위/제목/저자/출판사).
 * 핫트랜드/인기대출 두 API의 DocData 타입이 서로 달라 공용 도메인 모델이 없으므로,
 * 원시 파라미터만 받아 두 화면에서 공통으로 사용한다.
 */
@Composable
private fun BookRankRow(
    rank: Int,
    bookname: String?,
    authors: String?,
    publisher: String?,
    bookImageURL: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .noRippleClickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(96.dp)
                .height(132.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
                .background(
                    color = colorResource(R.color.color_FFFFFFFF),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            BookCoverImage(
                imageUrl = bookImageURL,
                modifier = Modifier.fillMaxSize(),
                cornerRadius = 12.dp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = rank.toString().padStart(2, '0'),
                style = TextStyle(
                    color = colorResource(R.color.color_6B7684),
                    fontSize = dpToSp(12.dp),
                    lineHeight = dpToSp(20.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = bookname ?: "",
                style = TextStyle(
                    color = colorResource(R.color.color_191F28),
                    fontSize = dpToSp(16.dp),
                    lineHeight = dpToSp(24.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (!authors.isNullOrBlank()) {
                Text(
                    text = authors,
                    style = TextStyle(
                        color = colorResource(R.color.color_4E5968),
                        fontSize = dpToSp(13.dp),
                        lineHeight = dpToSp(18.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (!publisher.isNullOrBlank()) {
                Text(
                    text = publisher,
                    style = TextStyle(
                        color = colorResource(R.color.color_6B7684),
                        fontSize = dpToSp(11.dp),
                        lineHeight = dpToSp(13.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun BookRankEmptyView() {
    Text(
        text = stringResource(R.string.common_component_no_item),
        style = TextStyle(
            color = colorResource(R.color.color_4E5968),
            fontSize = dpToSp(14.dp),
            lineHeight = dpToSp(20.dp),
            fontFamily = NotoSansKR,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    )
}

// 핫트랜드 프리뷰용 더미 데이터 — 실제 API 응답 대신 화면 모양 확인용
private val previewHotTrendBooks = listOf(
    ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData(
        no = 1,
        difference = 3,
        baseWeekRank = 1,
        pastWeekRank = 4,
        bookname = "아침의 피아노",
        authors = "김진영 저",
        publisher = "문학과지성사",
        publicationYear = "2020",
        isbn13 = "9791165341909",
        additionSymbol = null,
        vol = null,
        classNo = null,
        classNm = null,
        bookImageURL = null,
        bookDtlUrl = null
    ),
    ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData(
        no = 2,
        difference = 1,
        baseWeekRank = 2,
        pastWeekRank = 3,
        bookname = "물고기는 존재하지 않는다",
        authors = "룰루 밀러 저",
        publisher = "곰출판",
        publicationYear = "2021",
        isbn13 = "9791161571188",
        additionSymbol = null,
        vol = null,
        classNo = null,
        classNm = null,
        bookImageURL = null,
        bookDtlUrl = null
    ),
    ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData(
        no = 3,
        difference = -1,
        baseWeekRank = 3,
        pastWeekRank = 2,
        bookname = "긴긴밤",
        authors = "루리 저",
        publisher = "문학동네",
        publicationYear = "2021",
        isbn13 = "9791191056029",
        additionSymbol = null,
        vol = null,
        classNo = null,
        classNm = null,
        bookImageURL = null,
        bookDtlUrl = null
    )
)

// 인기대출도서 프리뷰용 더미 데이터 — 실제 API 응답 대신 화면 모양 확인용
private val previewPopularLoanBooks = listOf(
    ResLoanItemSrchByLib.ResponseData.DocWrapper(
        doc = ResLoanItemSrchByLib.ResponseData.DocWrapper.DocData(
            no = 1,
            ranking = "1",
            bookname = "사피엔스",
            authors = "유발 하라리 저",
            publisher = "김영사",
            publicationYear = "2015",
            isbn13 = "9788934972464",
            additionSymbol = null,
            vol = null,
            classNo = null,
            classNm = null,
            bookImageURL = null,
            bookDtlUrl = null,
            loanCount = "132"
        )
    ),
    ResLoanItemSrchByLib.ResponseData.DocWrapper(
        doc = ResLoanItemSrchByLib.ResponseData.DocWrapper.DocData(
            no = 2,
            ranking = "2",
            bookname = "소년이 온다",
            authors = "한강 저",
            publisher = "창비",
            publicationYear = "2014",
            isbn13 = "9788936434120",
            additionSymbol = null,
            vol = null,
            classNo = null,
            classNm = null,
            bookImageURL = null,
            bookDtlUrl = null,
            loanCount = "121"
        )
    ),
    ResLoanItemSrchByLib.ResponseData.DocWrapper(
        doc = ResLoanItemSrchByLib.ResponseData.DocWrapper.DocData(
            no = 3,
            ranking = "3",
            bookname = "불편한 편의점",
            authors = "김호연 저",
            publisher = "나무옆의자",
            publicationYear = "2021",
            isbn13 = "9788970128172",
            additionSymbol = null,
            vol = null,
            classNo = null,
            classNm = null,
            bookImageURL = null,
            bookDtlUrl = null,
            loanCount = "108"
        )
    )
)

@Preview(showBackground = true, name = "HotTrend")
@Composable
fun BookRankContentPreviewHotTrend() {
    BookRankContent(
        localContext = LocalContext.current,
        bookRankType = BookRankType.HOT_TREND,
        bookRankUiState = remember {
            mutableStateOf(BookRankUiState(hotTrendBooks = previewHotTrendBooks))
        },
        popularLoanBooksPaging = null,
        onBackPressed = {},
        onMoveToBookRankDetail = {}
    )
}

@Preview(showBackground = true, name = "PopularLoan")
@Composable
fun BookRankContentPreviewPopularLoan() {
    val pagingData = PagingData.from(previewPopularLoanBooks)
    val flow = flowOf(pagingData)

    BookRankContent(
        localContext = LocalContext.current,
        bookRankType = BookRankType.POPULAR_LOAN,
        bookRankUiState = remember { mutableStateOf(BookRankUiState()) },
        popularLoanBooksPaging = flow.collectAsLazyPagingItems(),
        onBackPressed = {},
        onMoveToBookRankDetail = {}
    )
}

@Preview(showBackground = true, name = "Empty")
@Composable
fun BookRankContentPreviewEmpty() {
    BookRankContent(
        localContext = LocalContext.current,
        bookRankType = BookRankType.HOT_TREND,
        bookRankUiState = remember { mutableStateOf(BookRankUiState()) },
        popularLoanBooksPaging = null,
        onBackPressed = {},
        onMoveToBookRankDetail = {}
    )
}
