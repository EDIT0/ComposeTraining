package com.my.book.library.feature.main.ui.home

import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.CommonSearchBar
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.res.ResHotTrend
import com.my.book.library.core.model.res.ResLoanItemSrchByLib
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.core.resource.NotoSansKR
import com.my.book.library.core.resource.R
import com.my.book.library.feature.main.intent.home.HomeViewModelEvent
import com.my.book.library.feature.main.state.home.HomeUiState
import com.my.book.library.feature.main.viewmodel.MainViewModel
import com.my.book.library.feature.main.viewmodel.home.HomeViewModel

/**
 * 홈 화면 진입점. ViewModel을 붙이고, 화면 재진입(onResume)마다 내 도서관 정보/핫트랜드/인기대출도서를 새로 요청한다.
 * 실제 UI 그리기는 상태를 받아 그리기만 하는 [HomeContent]에 위임한다.
 */
@Composable
fun HomeScreen(
    onMoveToSearchLibrary: () -> Unit,
    onMoveToSelectLibraryRegion: () -> Unit,
    commonViewModel: CommonViewModel,
    mainViewModel: MainViewModel
) {

    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val commonViewModel = commonViewModel
    val mainViewModel = mainViewModel
    val homeViewModel = hiltViewModel<HomeViewModel>()

    val homeUiState = homeViewModel.homeUiState.collectAsStateWithLifecycle()

    val lifecycleResult = remember {
        object : LifecycleResult {
            override fun onEnter() {}
            override fun onStart() {}
            override fun onResume() {
                homeViewModel.intentAction(HomeViewModelEvent.GetMyLibraryInfo)
                homeViewModel.intentAction(HomeViewModelEvent.GetHotTrend)
                homeViewModel.intentAction(HomeViewModelEvent.GetPopularLoanBooks)
            }
            override fun onPause() {}
            override fun onStop() {}
            override fun onDispose() {}
        }
    }

    HomeContent(
        localContext = localContext,
        onMoveToSearchLibrary = onMoveToSearchLibrary,
        onMoveToSelectLibraryRegion = onMoveToSelectLibraryRegion,
        homeUiState = homeUiState
    )

    LifecycleListener(
        lifecycleOwner = lifecycleOwner,
        screenName = object {}.javaClass.enclosingClass?.simpleName ?: "HomeScreen",
        lifecycleResult = lifecycleResult
    )
}

/**
 * 홈 화면의 실제 레이아웃. 상태(State)만 받아 그리므로 Preview에서도 그대로 사용한다.
 * 위에서부터 순서대로 [HomeLocationRow](내 지역) → [HomeSearchBar](검색 진입) → [HotTrendSection](핫트랜드) →
 * [PopularLoanBooksSection](우리 동네 인기 대출도서) 4개 영역으로 구성된다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    localContext: Context,
    onMoveToSearchLibrary: () -> Unit,
    onMoveToSelectLibraryRegion: () -> Unit,
    homeUiState: State<HomeUiState>
) {
    val scrollState = rememberScrollState()

    val useStatusBarSpace = true
    val useNavigationBarSpace = false

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
                .padding(top = if(useStatusBarSpace) {state.statusBarHeight} else {0.dp}, bottom = if(useNavigationBarSpace) {state.navigationBarHeight} else {0.dp})
                .consumeWindowInsets(WindowInsets.statusBars)
                .consumeWindowInsets(WindowInsets.navigationBars)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(color = colorResource(R.color.color_FFFFFFFF))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    HomeLocationRow(
                        myLibraryInfo = homeUiState.value.myLibraryInfo,
                        onClick = onMoveToSelectLibraryRegion
                    )

                    HomeSearchBar(
                        onSearchBarClick = onMoveToSearchLibrary
                    )

                    HotTrendSection(
                        hotTrendBooks = homeUiState.value.hotTrendBooks
                    )

                    PopularLoanBooksSection(
                        popularLoanBooks = homeUiState.value.popularLoanBooks,
                        districtName = homeUiState.value.myLibraryInfo?.detailRegion?.let {
                            stringResource(it.districtNameRes)
                        }
                    )
                }
            }
        }
    }
}

/**
 * 상단 위치 영역 — 사용자가 선택해둔 지역(시군구)명을 보여주고, 탭하면 지역 선택 화면으로 이동한다.
 */
@Composable
private fun HomeLocationRow(
    myLibraryInfo: MyRegionAndLibrary?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp)
            .noRippleClickable {
                // 지역 변경으로 이동
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_point_marker_blue_16x20),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = myLibraryInfo?.detailRegion?.let {
                stringResource(it.districtNameRes)
            } ?: "",
            style = TextStyle(
                color = colorResource(R.color.color_191F28),
                fontSize = dpToSp(20.dp),
                lineHeight = dpToSp(25.dp),
                fontFamily = NotoSansKR,
                fontWeight = FontWeight.Medium
            )
        )

        Spacer(modifier = Modifier.width(4.dp))

        Image(
            painter = painterResource(R.drawable.ic_arrow_bottom_grey_10x7),
            contentDescription = null
        )
    }
}

/**
 * 실제 입력은 받지 않는 가짜 검색바 — 탭하면 도서 검색 화면으로 이동한다.
 */
@Composable
private fun HomeSearchBar(
    onSearchBarClick: () -> Unit
) {
    CommonSearchBar(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            .fillMaxWidth(),
        isFakeSearchBar = true,
        backgroundColorRes = R.color.color_F2F4F6,
        cornerRadius = 8.dp,
        hint = stringResource(R.string.main_home_fake_search_hint),
        hintColorRes = R.color.color_8B95A1,
        textSize = 16.dp,
        value = TextFieldValue(""),
        focusRequester = remember { FocusRequester() },
        showKeyboardOnStart = true,
        onValueChange = {},
        onSearchClick = {},
        onCancelClick = {},
        onFakeBarClick = {
            onSearchBarClick.invoke()
        }
    )
}

/**
 * 대출 급상승(핫트랜드) 도서 영역 — 제목/부제 + 가로 스크롤 도서 목록.
 * 목록이 비어있으면(아직 로딩 전이거나 조회 결과 없음) 영역 자체를 그리지 않는다.
 */
@Composable
private fun HotTrendSection(
    hotTrendBooks: List<ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData>
) {
    if (hotTrendBooks.isEmpty()) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.main_home_hot_issue_books_title),
                    style = TextStyle(
                        color = colorResource(R.color.color_191F28),
                        fontSize = dpToSp(22.dp),
                        lineHeight = dpToSp(33.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(R.drawable.ic_arrow_right_grey_12x22),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.main_home_hot_issue_books_subtitle),
                style = TextStyle(
                    color = colorResource(R.color.color_4E5968),
                    fontSize = dpToSp(13.dp),
                    lineHeight = dpToSp(19.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(
                items = hotTrendBooks,
                key = { it.isbn13 ?: it.bookname.orEmpty() }
            ) { book ->
                HotTrendBookItem(book = book)
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(color = colorResource(R.color.color_EEEFF2))
        )
    }
}

/**
 * 도서 표지 비동기 이미지 — 로드 실패 시 Inside, 성공 시 Crop으로 표시 방식을 전환한다.
 * [HotTrendBookItem]/[PopularLoanBookRow]에서 공통으로 사용한다.
 */
@Composable
private fun BookCoverImage(
    imageUrl: String?,
    modifier: Modifier,
    cornerRadius: Dp
) {
    var contentScale by remember {
        mutableStateOf<ContentScale>(ContentScale.Inside)
    }

    AsyncImage(
        modifier = modifier.clip(RoundedCornerShape(cornerRadius)),
        model = imageUrl,
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = contentScale,
        placeholder = painterResource(R.drawable.ic_book_grey_57x64),
        error = painterResource(R.drawable.ic_book_grey_57x64),
        onError = { error ->
            contentScale = ContentScale.Inside
            LogUtil.e_dev("book image load error: ${error.result.throwable}")
        },
        onSuccess = {
            contentScale = ContentScale.Crop
            LogUtil.d_dev("book image success, url: $imageUrl")
        }
    )
}

/**
 * 핫트랜드 목록의 도서 1권 카드 — 표지 이미지 + 제목 + 저자.
 */
@Composable
private fun HotTrendBookItem(
    book: ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData
) {
    Column(
        modifier = Modifier
            .width(186.dp)
    ) {
        Box(
            modifier = Modifier
                .width(186.dp)
                .height(240.dp)
                .background(
                    color = colorResource(R.color.color_F2F4F6),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            BookCoverImage(
                imageUrl = book.bookImageURL,
                modifier = Modifier.fillMaxSize(),
                cornerRadius = 16.dp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = book.bookname ?: "",
            style = TextStyle(
                color = colorResource(R.color.color_191F28),
                fontSize = dpToSp(16.dp),
                lineHeight = dpToSp(24.dp),
                fontFamily = NotoSansKR,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        if(!book.authors.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = book.authors ?: "",
                style = TextStyle(
                    color = colorResource(R.color.color_191F28),
                    fontSize = dpToSp(13.dp),
                    lineHeight = dpToSp(19.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private const val POPULAR_LOAN_BOOKS_GRID_ROWS = 3
private val POPULAR_LOAN_BOOKS_ITEM_WIDTH = 280.dp
private val POPULAR_LOAN_BOOKS_COVER_WIDTH = 80.dp
private val POPULAR_LOAN_BOOKS_COVER_HEIGHT = 110.dp
private val POPULAR_LOAN_BOOKS_GRID_ROW_SPACING = 16.dp
private val POPULAR_LOAN_BOOKS_GRID_VERTICAL_PADDING = 4.dp

// Row 안쪽 여백(margin 역할) — 표지 위아래로 이 값만큼 여백이 생기므로
// 행 높이는 표지 높이 + 여백*2로 계산해야 그리드 위/아래가 잘리지 않는다.
private val POPULAR_LOAN_BOOKS_ROW_PADDING = 8.dp
private val POPULAR_LOAN_BOOKS_ROW_HEIGHT = POPULAR_LOAN_BOOKS_COVER_HEIGHT + POPULAR_LOAN_BOOKS_ROW_PADDING * 2
private val POPULAR_LOAN_BOOKS_GRID_HEIGHT = POPULAR_LOAN_BOOKS_ROW_HEIGHT * POPULAR_LOAN_BOOKS_GRID_ROWS + POPULAR_LOAN_BOOKS_GRID_ROW_SPACING * (POPULAR_LOAN_BOOKS_GRID_ROWS - 1)

/**
 * 우리 동네 인기 대출도서 영역 — 제목/부제(내 지역명 포함) + 가로 스크롤 도서 목록(최대 12권, 순위 뱃지 표시).
 * 목록이 비어있으면(지역 미선택 또는 조회 결과 없음) 영역 자체를 그리지 않는다.
 */
@Composable
private fun PopularLoanBooksSection(
    popularLoanBooks: List<ResLoanItemSrchByLib.ResponseData.DocWrapper.DocData>,
    districtName: String?
) {
    if (popularLoanBooks.isEmpty()) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.main_home_popular_loan_books_title),
                    style = TextStyle(
                        color = colorResource(R.color.color_191F28),
                        fontSize = dpToSp(22.dp),
                        lineHeight = dpToSp(33.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(R.drawable.ic_arrow_right_grey_12x22),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }

            if (!districtName.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.main_home_popular_loan_books_subtitle, districtName),
                    style = TextStyle(
                        color = colorResource(R.color.color_4E5968),
                        fontSize = dpToSp(13.dp),
                        lineHeight = dpToSp(19.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyHorizontalGrid(
            rows = GridCells.Fixed(POPULAR_LOAN_BOOKS_GRID_ROWS),
            modifier = Modifier
                .fillMaxWidth()
                .height(POPULAR_LOAN_BOOKS_GRID_HEIGHT),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(POPULAR_LOAN_BOOKS_GRID_ROW_SPACING),
            contentPadding = PaddingValues(
                horizontal = 20.dp,
                vertical = POPULAR_LOAN_BOOKS_GRID_VERTICAL_PADDING
            )
        ) {
            itemsIndexed(
                items = popularLoanBooks,
                key = { _, book -> book.isbn13 ?: book.bookname.orEmpty() }
            ) { index, book ->
                PopularLoanBookRow(
                    rank = index + 1,
                    book = book,
                    modifier = Modifier
                        .width(POPULAR_LOAN_BOOKS_ITEM_WIDTH)
                        .height(POPULAR_LOAN_BOOKS_ROW_HEIGHT)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * 인기 대출도서 목록의 도서 1권 행 — 순위 숫자 + 표지(80x110) + 제목/저자.
 * 3행 x 4열 그리드 한 칸을 차지하며, 그리드가 가로로 스크롤되어 열 단위로 다음 도서들을 보여준다.
 */
@Composable
private fun PopularLoanBookRow(
    rank: Int,
    book: ResLoanItemSrchByLib.ResponseData.DocWrapper.DocData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(POPULAR_LOAN_BOOKS_ROW_PADDING),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rank.toString(),
            style = TextStyle(
                color = colorResource(R.color.color_3182F6),
                fontSize = dpToSp(18.dp),
                lineHeight = dpToSp(27.dp),
                fontFamily = NotoSansKR,
                fontWeight = FontWeight.Bold
            ),
        )

        Spacer(modifier = Modifier.width(17.dp))

        Box(
            modifier = Modifier
                .width(POPULAR_LOAN_BOOKS_COVER_WIDTH)
                .height(POPULAR_LOAN_BOOKS_COVER_HEIGHT)
                .background(
                    color = colorResource(R.color.color_F2F4F6),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            BookCoverImage(
                imageUrl = book.bookImageURL,
                modifier = Modifier.fillMaxSize(),
                cornerRadius = 8.dp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = book.bookname ?: "",
                style = TextStyle(
                    color = colorResource(R.color.color_191F28),
                    fontSize = dpToSp(14.dp),
                    lineHeight = dpToSp(20.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            if (!book.authors.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = book.authors ?: "",
                    style = TextStyle(
                        color = colorResource(R.color.color_4E5968),
                        fontSize = dpToSp(12.dp),
                        lineHeight = dpToSp(18.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// 핫트랜드 프리뷰용 더미 데이터 — 실제 API 응답 대신 화면 모양 확인용
private val previewHotTrendBooks = listOf(
    ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData(
        no = 1,
        difference = 3,
        baseWeekRank = 1,
        pastWeekRank = 4,
        bookname = "달러구트 꿈 백화점",
        authors = "이미예",
        publisher = "팩토리나인",
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
        bookname = "불편한 편의점",
        authors = "김호연",
        publisher = "나무옆의자",
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
        bookname = "미드나잇 라이브러리",
        authors = "매트 헤이그",
        publisher = "인플루엔셜",
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

// 우리 동네 인기 대출도서 프리뷰용 더미 데이터 — 실제 API 응답 대신 화면 모양 확인용
private val previewPopularLoanBooks = listOf(
    ResLoanItemSrchByLib.ResponseData.DocWrapper.DocData(
        no = 1,
        ranking = "1",
        bookname = "채식주의자",
        authors = "한강",
        publisher = "창비",
        publicationYear = "2007",
        isbn13 = "9788936434120",
        additionSymbol = null,
        vol = null,
        classNo = null,
        classNm = null,
        bookImageURL = null,
        bookDtlUrl = null,
        loanCount = "132"
    ),
    ResLoanItemSrchByLib.ResponseData.DocWrapper.DocData(
        no = 2,
        ranking = "2",
        bookname = "아몬드",
        authors = "손원평",
        publisher = "창비",
        publicationYear = "2017",
        isbn13 = "9788936455473",
        additionSymbol = null,
        vol = null,
        classNo = null,
        classNm = null,
        bookImageURL = null,
        bookDtlUrl = null,
        loanCount = "121"
    ),
    ResLoanItemSrchByLib.ResponseData.DocWrapper.DocData(
        no = 3,
        ranking = "3",
        bookname = "파친코",
        authors = "이민진",
        publisher = "문학사상",
        publicationYear = "2018",
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

@Preview(showBackground = true)
@Composable
fun HomeUIPreview() {
    HomeContent(
        localContext = LocalContext.current,
        onMoveToSearchLibrary = {},
        onMoveToSelectLibraryRegion = {},
        homeUiState = remember {
            mutableStateOf(
                HomeUiState(
                    myLibraryInfo = MyRegionAndLibrary(
                        detailRegion = LibraryData.allDetailRegions.first(),
                        library = null
                    ),
                    hotTrendBooks = previewHotTrendBooks,
                    popularLoanBooks = previewPopularLoanBooks
                )
            )
        },
    )
}

@Preview(showBackground = true, name = "HotTrend Empty")
@Composable
fun HomeUIPreviewEmptyHotTrend() {
    HomeContent(
        localContext = LocalContext.current,
        onMoveToSearchLibrary = {},
        onMoveToSelectLibraryRegion = {},
        homeUiState = remember { mutableStateOf(HomeUiState()) },
    )
}

@Preview(showBackground = true, name = "PopularLoanBooks Empty")
@Composable
fun HomeUIPreviewEmptyPopularLoanBooks() {
    HomeContent(
        localContext = LocalContext.current,
        onMoveToSearchLibrary = {},
        onMoveToSelectLibraryRegion = {},
        homeUiState = remember {
            mutableStateOf(HomeUiState(hotTrendBooks = previewHotTrendBooks))
        },
    )
}
