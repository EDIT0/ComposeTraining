package com.my.book.library.feature.search.library.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.model.res.ResSearchBook
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.NotoSansKR
import com.my.book.library.core.resource.R
import com.my.book.library.feature.search.library.intent.LibraryMapViewModelEvent
import com.my.book.library.feature.search.library.state.LibraryMapUiState
import com.my.book.library.feature.search.library.viewmodel.LibraryMapViewModel
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap

@Composable
fun LibraryMapScreen(
    commonViewModel: CommonViewModel,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    book: ResSearchBook.ResponseData.BookWrapper
) {

    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val commonViewModel = commonViewModel
    val libraryMapViewModel = hiltViewModel<LibraryMapViewModel>()

    val libraryMapUiState = libraryMapViewModel.libraryMapUiState.collectAsStateWithLifecycle()
    val libraryListPaging = libraryMapUiState.value.libraryList?.collectAsLazyPagingItems()

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
        libraryMapViewModel.sideEffectEvent.collect {
            when(it) {
                is LibraryMapViewModel.SideEffectEvent.ShowToast -> {
                    Toast.makeText(localContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LibraryMapContent(
        localContext = localContext,
        onBackPressed = onBackPressed,
        modifier = Modifier,
        libraryMapViewModelEvent = {
            libraryMapViewModel.intentAction(it)
        },
        libraryMapUiState = libraryMapUiState,
        libraryListPaging = libraryListPaging,
    )

    LifecycleListener(
        lifecycleOwner = lifecycleOwner,
        screenName = object {}.javaClass.enclosingClass?.simpleName ?: "LibraryMapScreen",
        lifecycleResult = lifecycleResult
    )
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun LibraryMapContent(
    localContext: Context,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    libraryMapViewModelEvent: (LibraryMapViewModelEvent) -> Unit,
    libraryMapUiState: State<LibraryMapUiState>,
    libraryListPaging: LazyPagingItems<ResSearchBookLibrary.ResponseData.LibraryWrapper>?
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
                .padding(top = if(useStatusBarSpace) {state.statusBarHeight} else {0.dp}, bottom = if(useNavigationBarSpace) {state.navigationBarHeight} else {0.dp})
                .consumeWindowInsets(WindowInsets.statusBars)
                .consumeWindowInsets(WindowInsets.navigationBars)
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .padding(innerPadding)
                    .background(color = colorResource(R.color.color_FFFFFFFF))
            ) {
                val isPreview = LocalInspectionMode.current
                if (isPreview) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(R.color.color_E5E8EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "지도 영역",
                            style = TextStyle(
                                color = colorResource(R.color.color_6B7684),
                                fontSize = dpToSp(16.dp),
                                fontFamily = NotoSansKR,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                } else {
                    NaverMap(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchContentUiPreview() {
    val mockData = listOf(
        ResSearchBookLibrary.ResponseData.LibraryWrapper(
            lib = ResSearchBookLibrary.ResponseData.LibraryWrapper.LibraryInfo(
                libCode = "111017",
                libName = "종로도서관",
                address = "서울특별시 종로구 송월길 48",
                tel = "02-2148-1830",
                fax = "02-2148-1839",
                latitude = "37.572552",
                longitude = "126.966574",
                homepage = "http://jongno.lib.seoul.kr",
                closed = "매주 월요일",
                operatingTime = "09:00~22:00",
                bookCount = "120000"
            )
        ),
        ResSearchBookLibrary.ResponseData.LibraryWrapper(
            lib = ResSearchBookLibrary.ResponseData.LibraryWrapper.LibraryInfo(
                libCode = "111018",
                libName = "정독도서관",
                address = "서울특별시 종로구 북촌로5길 48",
                tel = "02-2011-5799",
                fax = "02-2011-5780",
                latitude = "37.582837",
                longitude = "126.981837",
                homepage = "http://jd.lib.seoul.kr",
                closed = "매주 월요일",
                operatingTime = "09:00~21:00",
                bookCount = "250000"
            )
        )
    )

    val pagingData = PagingData.from(mockData)
    val flow = flowOf(pagingData)

    LibraryMapContent(
        localContext = LocalContext.current,
        onBackPressed = {},
        modifier = Modifier,
        libraryMapViewModelEvent = {},
        libraryMapUiState = remember { mutableStateOf(LibraryMapUiState()) },
        libraryListPaging = flow.collectAsLazyPagingItems()
    )
}