package com.my.book.library.feature.main.ui.rank_detail

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.feature.main.intent.rank_detail.BookRankDetailViewModelEvent
import com.my.book.library.feature.main.ui.common.BookCoverImage
import com.my.book.library.feature.main.viewmodel.rank_detail.BookRankDetailViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.my.book.library.core.common.component.CommonActionBar
import com.my.book.library.core.common.component.CommonButton
import com.my.book.library.core.common.component.ListLoadingView
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.model.res.ResBookDetail
import com.my.book.library.core.model.res.ResRecommendList
import com.my.book.library.core.model.res.ResSearchBook
import com.my.book.library.core.resource.NotoSansKR
import com.my.book.library.core.resource.R

/**
 * 책소개는 없으면 해당 영역 GONE
 * 이 책과 함께 보면 좋은 책은 없으면 해당 영역 GONE
 * */
@Composable
fun BookRankDetailScreen(
    isbn13: String,
    commonViewModel: CommonViewModel,
    onBackPressed: () -> Unit,
    onMoveToBookRankDetail: (String) -> Unit,
    onMoveToLibraryMap: (ResSearchBook.ResponseData.BookWrapper) -> Unit,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current

    val bookRankDetailViewModel = hiltViewModel<BookRankDetailViewModel>()
    val isLoading = bookRankDetailViewModel.bookRankDetailUiState.collectAsState().value.isLoading
    val bookWrapper = bookRankDetailViewModel.bookRankDetailUiState.collectAsState().value.bookWrapper
    val recommendList = bookRankDetailViewModel.bookRankDetailUiState.collectAsState().value.recommendList

    LaunchedEffect(key1 = Unit) {
        bookRankDetailViewModel.sideEffectEvent.collect {
            when(it) {
                is BookRankDetailViewModel.SideEffectEvent.ShowToast -> {

                }
            }
        }
    }
    LaunchedEffect(isbn13) {
        bookRankDetailViewModel.intentAction(BookRankDetailViewModelEvent.LoadBookDetailAndRecommendList(isbn13 = isbn13))
    }

    BookRankDetailContent(
        localContext = localContext,
        onBackPressed = onBackPressed,
        onMoveToBookRankDetail = onMoveToBookRankDetail,
        onMoveToLibraryMap = onMoveToLibraryMap,
        onRetryClick = {
            bookRankDetailViewModel.intentAction(BookRankDetailViewModelEvent.LoadBookDetailAndRecommendList(isbn13 = isbn13))
        },
        modifier = modifier,
        isLoading = isLoading,
        bookWrapper = bookWrapper,
        recommendList = recommendList
    )
}

@Composable
fun BookRankDetailContent(
    localContext: Context,
    onBackPressed: () -> Unit,
    onMoveToBookRankDetail: (String) -> Unit,
    onMoveToLibraryMap: (ResSearchBook.ResponseData.BookWrapper) -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    bookWrapper: ResBookDetail.ResponseData.BookWrapper?,
    recommendList: List<ResRecommendList.ResponseData.BookWrapper>?
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
                    top = if (useStatusBarSpace) {
                        state.statusBarHeight
                    } else {
                        0.dp
                    },
                    bottom = if (useNavigationBarSpace) {
                        state.navigationBarHeight
                    } else {
                        0.dp
                    }
                )
                .consumeWindowInsets(WindowInsets.statusBars)
                .consumeWindowInsets(WindowInsets.navigationBars),
            topBar = {
                CommonActionBar(
                    context = localContext,
                    actionBarTitle = bookWrapper?.book?.bookName ?: "",
                    titleTextAlign = TextAlign.Start,
                    isShowBackButton = true,
                    onBackClick = onBackPressed
                )
            }
        ) { innerPadding ->

            if(isLoading) {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(color = colorResource(R.color.color_FFFFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    ListLoadingView()
                }
            } else if(bookWrapper == null) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(color = colorResource(R.color.color_FFFFFFFF)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_warning_container),
                        contentDescription = ""
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.main_rank_detail_error_title),
                        style = TextStyle(
                            color = colorResource(R.color.color_191F28),
                            fontSize = dpToSp(20.dp),
                            lineHeight = dpToSp(25.dp),
                            fontFamily = NotoSansKR,
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.main_rank_detail_error_subtitle),
                        style = TextStyle(
                            color = colorResource(R.color.color_4E5968),
                            fontSize = dpToSp(13.dp),
                            lineHeight = dpToSp(22.dp),
                            fontFamily = NotoSansKR,
                            fontWeight = FontWeight.Normal
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CommonButton(
                        text = stringResource(R.string.main_rank_detail_retry_button),
                        textColorRes = R.color.color_FFFFFFFF,
                        backgroundColorRes =R.color.color_3182F6,
                        cornerRadius = 100.dp,
                        isBorderEnabled = false,
                        isEnabled = true,
                        textSize = 16.dp,
                        onClick = onRetryClick
                    )
                }
            } else {
                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(color = colorResource(R.color.color_FFFFFFFF))
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(state = scrollState),
                    ) {
                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(200.dp)
                                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
                                    .background(
                                        color = colorResource(R.color.color_FFFFFFFF),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                BookCoverImage(
                                    imageUrl = bookWrapper?.book?.bookImageUrl,
                                    modifier = Modifier.fillMaxSize(),
                                    cornerRadius = 12.dp
                                )
                            }

                            Spacer(modifier = Modifier.width(20.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = bookWrapper?.book?.bookName ?: "",
                                    style = TextStyle(
                                        color = colorResource(R.color.color_191F28),
                                        fontSize = dpToSp(18.dp),
                                        lineHeight = dpToSp(30.dp),
                                        fontFamily = NotoSansKR,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = listOfNotNull(
                                        bookWrapper?.book?.authors?.takeIf { it.isNotBlank() },
                                        bookWrapper?.book?.publisher?.takeIf { it.isNotBlank() },
                                        bookWrapper?.book?.publicationYear?.takeIf { it.isNotBlank() }
                                    ).joinToString("\n"),
                                    style = TextStyle(
                                        color = colorResource(R.color.color_4E5968),
                                        fontSize = dpToSp(14.dp),
                                        lineHeight = dpToSp(22.dp),
                                        fontFamily = NotoSansKR,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    maxLines = 4,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        if(!bookWrapper?.book?.description.isNullOrBlank()) {
                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .padding(horizontal = 20.dp)
                                .background(
                                    color = colorResource(R.color.color_E5E8EB)
                                )
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.main_rank_detail_description_title),
                                    style = TextStyle(
                                        color = colorResource(R.color.color_191F28),
                                        fontSize = dpToSp(18.dp),
                                        lineHeight = dpToSp(25.dp),
                                        fontFamily = NotoSansKR,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(7.dp))

                                Text(
                                    text = bookWrapper?.book?.description ?: "",
                                    style = TextStyle(
                                        color = colorResource(R.color.color_4E5968),
                                        fontSize = dpToSp(14.dp),
                                        lineHeight = dpToSp(22.dp),
                                        fontFamily = NotoSansKR,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    maxLines = 100,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.height(32.dp))
                        }

                        if (!recommendList.isNullOrEmpty()) {

                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .padding(horizontal = 20.dp)
                                .background(
                                    color = colorResource(R.color.color_E5E8EB)
                                )
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.main_rank_detail_recommend_title),
                                    style = TextStyle(
                                        color = colorResource(R.color.color_191F28),
                                        fontSize = dpToSp(18.dp),
                                        lineHeight = dpToSp(25.dp),
                                        fontFamily = NotoSansKR,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(3.dp))

                                Text(
                                    text = stringResource(R.string.main_rank_detail_recommend_subtitle),
                                    style = TextStyle(
                                        color = colorResource(R.color.color_4E5968),
                                        fontSize = dpToSp(12.dp),
                                        lineHeight = dpToSp(14.dp),
                                        fontFamily = NotoSansKR,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp)
                            ) {
                                items(
                                    items = recommendList,
                                    key = { it.book?.isbn13 ?: it.book?.bookname.orEmpty() }
                                ) { bookWrapper ->
                                    RecommendBookItem(
                                        book = bookWrapper.book,
                                        onClick = onMoveToBookRankDetail
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                    }

                    CommonButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        text = stringResource(R.string.main_rank_detail_find_library_button),
                        textColorRes = R.color.color_FFFFFFFF,
                        backgroundColorRes =R.color.color_3182F6,
                        cornerRadius = 8.dp,
                        isBorderEnabled = false,
                        isEnabled = true,
                        textSize = 16.dp,
                        onClick = {
                            bookWrapper?.book?.let { book ->
                                onMoveToLibraryMap(
                                    ResSearchBook.ResponseData.BookWrapper(
                                        doc = ResSearchBook.ResponseData.BookWrapper.BookInfo(
                                            bookName = book.bookName,
                                            authors = book.authors,
                                            publisher = book.publisher,
                                            publicationYear = book.publicationYear,
                                            isbn13 = book.isbn13,
                                            additionSymbol = book.additionSymbol,
                                            vol = book.vol,
                                            classNo = book.classNo,
                                            classNm = book.classNm,
                                            bookImageUrl = book.bookImageUrl,
                                            bookDtlUrl = null,
                                            loanCount = null
                                        )
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendBookItem(
    book: ResRecommendList.ResponseData.BookWrapper.BookData?,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .noRippleClickable {
                book?.isbn13?.let(onClick)
            }
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(160.dp)
                .background(
                    color = colorResource(R.color.color_F2F4F6),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            BookCoverImage(
                imageUrl = book?.bookImageURL,
                modifier = Modifier.fillMaxSize(),
                cornerRadius = 16.dp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = book?.bookname ?: "",
            style = TextStyle(
                color = colorResource(R.color.color_191F28),
                fontSize = dpToSp(14.dp),
                lineHeight = dpToSp(18.dp),
                fontFamily = NotoSansKR,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (!book?.authors.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = book?.authors ?: "",
                style = TextStyle(
                    color = colorResource(R.color.color_4E5968),
                    fontSize = dpToSp(12.dp),
                    lineHeight = dpToSp(15.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// BookRankDetail 프리뷰용 더미 데이터 — 실제 API 응답 대신 화면 모양 확인용
private val previewBookWrapper = ResBookDetail.ResponseData.BookWrapper(
    book = ResBookDetail.ResponseData.BookWrapper.BookInfo(
        no = 1,
        bookName = "아몬드",
        authors = "손원평 저",
        publisher = "창비",
        publicationDate = "2017-03-31",
        publicationYear = "2017",
        isbn = "9788936455473",
        isbn13 = "9788936455473",
        additionSymbol = null,
        vol = null,
        classNo = null,
        classNm = null,
        description = "감정을 느끼지 못하는 소년 윤재가 세상과 부딪히며 성장해가는 이야기.",
        bookImageUrl = null
    )
)

private val previewRecommendList = listOf(
    ResRecommendList.ResponseData.BookWrapper(
        book = ResRecommendList.ResponseData.BookWrapper.BookData(
            no = 1,
            bookname = "소년이 온다",
            authors = "한강 저",
            publisher = "창비",
            publicationYear = "2014",
            isbn13 = "9788936434120",
            additionSymbol = null,
            vol = null,
            classNo = null,
            classNm = null,
            bookImageURL = null
        )
    ),
    ResRecommendList.ResponseData.BookWrapper(
        book = ResRecommendList.ResponseData.BookWrapper.BookData(
            no = 2,
            bookname = "파친코",
            authors = "이민진 저",
            publisher = "문학사상",
            publicationYear = "2018",
            isbn13 = "9788970128172",
            additionSymbol = null,
            vol = null,
            classNo = null,
            classNm = null,
            bookImageURL = null
        )
    )
)

@Preview(showBackground = true)
@Composable
fun BookRankDetailContentPreview() {
    BookRankDetailContent(
        localContext = LocalContext.current,
        onBackPressed = {},
        onMoveToBookRankDetail = {},
        onMoveToLibraryMap = {},
        onRetryClick = {},
        isLoading = false,
        bookWrapper = previewBookWrapper,
        recommendList = previewRecommendList
    )
}

@Preview(showBackground = true, name = "Empty")
@Composable
fun BookRankDetailContentPreviewEmpty() {
    BookRankDetailContent(
        localContext = LocalContext.current,
        onBackPressed = {},
        onMoveToBookRankDetail = {},
        onMoveToLibraryMap = {},
        onRetryClick = {},
        isLoading = false,
        bookWrapper = null,
        recommendList = null
    )
}