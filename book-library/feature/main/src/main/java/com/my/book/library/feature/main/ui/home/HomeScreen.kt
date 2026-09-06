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
import com.my.book.library.core.resource.NotoSansKR
import com.my.book.library.core.resource.R
import com.my.book.library.feature.main.intent.home.HomeViewModelEvent
import com.my.book.library.feature.main.state.home.HomeUiState
import com.my.book.library.feature.main.viewmodel.MainViewModel
import com.my.book.library.feature.main.viewmodel.home.HomeViewModel

/**
 * 홈 화면 진입점. ViewModel을 붙이고, 화면 재진입(onResume)마다 내 도서관 정보/핫트랜드를 새로 요청한다.
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
 * 위에서부터 순서대로 [HomeLocationRow](내 지역) → [HomeSearchBar](검색 진입) → [HotTrendSection](핫트랜드) 3개 영역으로 구성된다.
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

        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(color = colorResource(R.color.color_EEEFF2))
        )
    }
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
            var contentScale by remember {
                mutableStateOf<ContentScale>(ContentScale.Inside)
            }

            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                model = book.bookImageURL,
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
                    LogUtil.d_dev("book image success, url: ${book.bookImageURL}")
                }
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

@Preview(showBackground = true)
@Composable
fun HomeUIPreview() {
    HomeContent(
        localContext = LocalContext.current,
        onMoveToSearchLibrary = {},
        onMoveToSelectLibraryRegion = {},
        homeUiState = remember {
            mutableStateOf(HomeUiState(hotTrendBooks = previewHotTrendBooks))
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
