package com.my.book.library.feature.search.library.ui

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.model.res.ResSearchBook
import com.my.book.library.core.model.res.ResSearchBookHoldingLibrary
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
    val holdingLibraryListPaging = libraryMapUiState.value.holdingLibraryList?.collectAsLazyPagingItems()

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
        libraryMapViewModel.intentAction(LibraryMapViewModelEvent.RequestInit(
            isbn = book.doc.isbn13 ?: ""
        ))
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
        holdingLibraryListPaging = holdingLibraryListPaging,
        userLatitude = libraryMapUiState.value.userLatitude,
        userLongitude = libraryMapUiState.value.userLongitude,
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
    holdingLibraryListPaging: LazyPagingItems<ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper>?,
    previewHoldingLibraryItems: List<ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper> = emptyList(),
    userLatitude: Double? = null,
    userLongitude: Double? = null
) {
    val isPreview = LocalInspectionMode.current

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
        val density = LocalDensity.current

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
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val screenHeightPx = constraints.maxHeight.toFloat()
                    val initialOffsetPx = remember(screenHeightPx) {
                        screenHeightPx - with(density) { screenHeightPx / 4 }
                    }
                    val minOffsetPx = 0f
                    val maxOffsetPx = remember(screenHeightPx) {
                        screenHeightPx - with(density) { screenHeightPx / 4 }
                    }

                    // configuration change 후 시트 위치 복원을 위해 비율(0~1)로 저장
                    var savedOffsetRatio by rememberSaveable { mutableFloatStateOf(-1f) }
                    val sheetOffsetState = remember(screenHeightPx) {
                        val initial = if (savedOffsetRatio >= 0f) {
                            (savedOffsetRatio * screenHeightPx).coerceIn(minOffsetPx, maxOffsetPx)
                        } else {
                            initialOffsetPx
                        }
                        mutableFloatStateOf(initial)
                    }
                    var sheetOffsetY by sheetOffsetState

                    LaunchedEffect(sheetOffsetState) {
                        snapshotFlow { sheetOffsetState.floatValue }
                            .collect { savedOffsetRatio = it / screenHeightPx }
                    }

                    val nestedScrollConnection = remember(minOffsetPx, maxOffsetPx) {
                        object : NestedScrollConnection {
                            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                                val delta = available.y
                                if (delta < 0 && sheetOffsetState.floatValue > minOffsetPx) {
                                    val newOffset = (sheetOffsetState.floatValue + delta).coerceIn(minOffsetPx, maxOffsetPx)
                                    val consumed = newOffset - sheetOffsetState.floatValue
                                    sheetOffsetState.floatValue = newOffset
                                    return Offset(0f, consumed)
                                }
                                return Offset.Zero
                            }
                            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                                val delta = available.y
                                if (delta > 0 && sheetOffsetState.floatValue < maxOffsetPx) {
                                    val newOffset = (sheetOffsetState.floatValue + delta).coerceIn(minOffsetPx, maxOffsetPx)
                                    val consumed2 = newOffset - sheetOffsetState.floatValue
                                    sheetOffsetState.floatValue = newOffset
                                    return Offset(0f, consumed2)
                                }
                                return Offset.Zero
                            }
                        }
                    }

                    // 지도
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

                    // 자유 드래그 바텀시트
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .offset { IntOffset(x = 0, y = sheetOffsetY.roundToInt()) }
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .background(colorResource(R.color.color_FFFFFFFF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .nestedScroll(nestedScrollConnection)
                        ) {
                            // 드래그 핸들
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .draggable(
                                        orientation = Orientation.Vertical,
                                        state = rememberDraggableState { delta ->
                                            sheetOffsetY = (sheetOffsetY + delta)
                                                .coerceIn(minOffsetPx, maxOffsetPx)
                                        }
                                    )
                                    .padding(top = 12.dp, bottom = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(4.dp)
                                        .background(
                                            color = colorResource(R.color.color_E5E8EB),
                                            shape = RoundedCornerShape(2.dp)
                                        )
                                )
                            }

                            // 리스트
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                if (isPreview) {
                                    items(count = previewHoldingLibraryItems.size) { index ->
                                        LibraryListItem(
                                            item = previewHoldingLibraryItems[index],
                                            userLatitude = userLatitude,
                                            userLongitude = userLongitude
                                        )
                                    }
                                } else {
                                    holdingLibraryListPaging?.let { pagingItems ->
                                        items(count = pagingItems.itemCount) { index ->
                                            val item = pagingItems[index] ?: return@items
                                            LibraryListItem(
                                                item = item,
                                                userLatitude = userLatitude,
                                                userLongitude = userLongitude
                                            )
                                        }
                                    }
                                }
                                item {
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryListItem(
    item: ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper,
    userLatitude: Double?,
    userLongitude: Double?
) {
    val context = LocalContext.current

    val distanceText = remember(userLatitude, userLongitude, item.lib.latitude, item.lib.longitude) {
        val libLat = item.lib.latitude?.toDoubleOrNull()
        val libLon = item.lib.longitude?.toDoubleOrNull()
        if (userLatitude != null && userLongitude != null && libLat != null && libLon != null) {
            val results = FloatArray(1)
            Location.distanceBetween(userLatitude, userLongitude, libLat, libLon, results)
            "%.1f km".format(results[0] / 1000f)
        } else null
    }

    val times = item.lib.operatingTime?.split("~")
    val openTime = times?.getOrNull(0)?.trim()
    val closeTime = times?.getOrNull(1)?.trim()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // 제목 + 직선거리
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.lib.libName ?: "",
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    color = colorResource(R.color.color_191F28),
                    fontSize = dpToSp(16.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Medium
                )
            )
            distanceText?.let {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = it,
                    style = TextStyle(
                        color = colorResource(R.color.color_6B7684),
                        fontSize = dpToSp(13.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        // 주소
        Text(
            text = item.lib.address ?: "",
            style = TextStyle(
                color = colorResource(R.color.color_6B7684),
                fontSize = dpToSp(13.dp),
                fontFamily = NotoSansKR,
                fontWeight = FontWeight.Normal
            )
        )
        if (!openTime.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = openTime,
                style = TextStyle(
                    color = colorResource(R.color.color_6B7684),
                    fontSize = dpToSp(13.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Normal
                )
            )
        }
        if (!closeTime.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = closeTime,
                style = TextStyle(
                    color = colorResource(R.color.color_6B7684),
                    fontSize = dpToSp(13.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Normal
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // 전화 버튼 + 홈페이지 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.lib.tel}"))
                    context.startActivity(intent)
                }
            ) {
                Text(text = "전화")
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    val uri = Uri.parse(item.lib.homepage)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                }
            ) {
                Text(text = "홈페이지 바로가기")
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(1.dp)
            .background(colorResource(R.color.color_F2F4F6))
    )
}

@Preview(showBackground = true)
@Composable
fun SearchContentUiPreview() {
    val mockData = listOf(
        ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper(
            lib = ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper.LibraryInfo(
                libCode = "111017",
                libName = "종로도서관",
                address = "서울특별시 종로구 송월길 48",
                tel = "02-2148-1830",
                fax = "02-2148-1839",
                latitude = "37.572552",
                longitude = "126.966574",
                homepage = "http://jongno.lib.seoul.kr",
                closed = "매주 월요일",
                operatingTime = "09:00~22:00"
            )
        ),
        ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper(
            lib = ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper.LibraryInfo(
                libCode = "111018",
                libName = "정독도서관",
                address = "서울특별시 종로구 북촌로5길 48",
                tel = "02-2011-5799",
                fax = "02-2011-5780",
                latitude = "37.582837",
                longitude = "126.981837",
                homepage = "http://jd.lib.seoul.kr",
                closed = "매주 월요일",
                operatingTime = "09:00~21:00"
            )
        ),
        ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper(
            lib = ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper.LibraryInfo(
                libCode = "111019",
                libName = "마포구립서강도서관",
                address = "서울특별시 마포구 백범로 35",
                tel = "02-716-4567",
                fax = "02-716-4568",
                latitude = "37.547821",
                longitude = "126.939456",
                homepage = "http://mapo.lib.seoul.kr",
                closed = "매주 월요일",
                operatingTime = "09:00~21:00"
            )
        )
    )

    LibraryMapContent(
        localContext = LocalContext.current,
        onBackPressed = {},
        modifier = Modifier,
        libraryMapViewModelEvent = {},
        libraryMapUiState = remember { mutableStateOf(LibraryMapUiState()) },
        holdingLibraryListPaging = null,
        previewHoldingLibraryItems = mockData,
        userLatitude = 37.5665,
        userLongitude = 126.9780
    )
}