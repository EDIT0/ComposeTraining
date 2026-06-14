package com.my.book.library.feature.search.library.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import coil3.compose.AsyncImage
import com.my.book.library.core.model.res.ResBookDetail
import com.my.book.library.core.model.res.ResSearchBook
import com.my.book.library.core.model.res.ResSearchBookHoldingLibrary
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.model.res.ResCheckBookAvailability
import com.my.book.library.core.model.res.ResLibraryBookData
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.core.resource.NotoSansKR
import com.my.book.library.core.resource.R
import com.my.book.library.feature.search.library.intent.LibraryMapViewModelEvent
import com.my.book.library.feature.search.library.state.LibraryMapUiState
import com.my.book.library.feature.search.library.viewmodel.LibraryMapViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberUpdatedMarkerState
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun LibraryMapScreen(
    commonViewModel: CommonViewModel,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    book: ResSearchBook.ResponseData.BookWrapper
) {

    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val libraryMapViewModel = hiltViewModel<LibraryMapViewModel>()

    val libraryMapUiState = libraryMapViewModel.libraryMapUiState.collectAsStateWithLifecycle()
    val holdingLibraryListPaging = libraryMapUiState.value.holdingLibraryList?.collectAsLazyPagingItems()
    val cameraPositionState = rememberCameraPositionState()
    val resCheckBookAvailability: ResCheckBookAvailability? = libraryMapUiState.value.resCheckBookAvailability

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        if (granted) {
            libraryMapViewModel.intentAction(LibraryMapViewModelEvent.StartLocationUpdates)
        }
    }

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
                is LibraryMapViewModel.SideEffectEvent.MoveToMyLocation -> {
                    val lat = libraryMapUiState.value.userLatitude
                    val lon = libraryMapUiState.value.userLongitude
                    if (lat != null && lon != null) {
                        cameraPositionState.animate(
                            update = CameraUpdate.scrollTo(LatLng(lat, lon)),
                            durationMs = 500
                        )
                    }
                }
                is LibraryMapViewModel.SideEffectEvent.RequestLocationPermission -> {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
        }
    }

    LibraryMapContent(
        localContext = localContext,
        onBackPressed = onBackPressed,
        modifier = Modifier,
        bookTitle = book.doc.bookName ?: "",
        book = book,
        libraryMapViewModelEvent = {
            libraryMapViewModel.intentAction(it)
        },
        libraryMapUiState = libraryMapUiState,
        holdingLibraryListPaging = holdingLibraryListPaging,
        userLatitude = libraryMapUiState.value.userLatitude,
        userLongitude = libraryMapUiState.value.userLongitude,
        cameraPositionState = cameraPositionState,
        resCheckBookAvailability = resCheckBookAvailability
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
    bookTitle: String = "",
    book: ResSearchBook.ResponseData.BookWrapper? = null,
    libraryMapViewModelEvent: (LibraryMapViewModelEvent) -> Unit,
    libraryMapUiState: State<LibraryMapUiState>,
    holdingLibraryListPaging: LazyPagingItems<ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper>?,
    previewHoldingLibraryItems: List<ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper> = emptyList(),
    userLatitude: Double? = null,
    userLongitude: Double? = null,
    cameraPositionState: CameraPositionState? = null,
    resCheckBookAvailability: ResCheckBookAvailability? = null
) {
    val isPreview = LocalInspectionMode.current

    // 지역 선택 바텀시트 상태 (저장 없이 임시 조회용)
    var showRegionSheet by rememberSaveable { mutableStateOf(false) }

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
                .padding(
                    top = if (useStatusBarSpace) {
                        state.statusBarHeight
                    } else {
                        0.dp
                    }, bottom = if (useNavigationBarSpace) {
                        state.navigationBarHeight
                    } else {
                        0.dp
                    }
                )
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
                    val coroutineScope = rememberCoroutineScope()
                    val screenHeightPx = constraints.maxHeight.toFloat()
                    var titleBarTopPx by remember { mutableFloatStateOf(0f) }
                    var titleBarBottomPx by remember { mutableFloatStateOf(0f) }
                    val initialOffsetPx = remember(screenHeightPx) {
                        screenHeightPx - with(density) { screenHeightPx / 3 }
                    }
                    val minOffsetPx = (titleBarTopPx + titleBarBottomPx) / 2f
                    val maxOffsetPx = remember(screenHeightPx) {
                        screenHeightPx - with(density) { screenHeightPx / 3 }
                    }

                    val savedOffsetRatio = libraryMapUiState.value.sheetOffsetRatio
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
                            .collect { libraryMapViewModelEvent(LibraryMapViewModelEvent.UpdateSheetOffsetRatio(it / screenHeightPx)) }
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

                    // Detail sheet 스냅 포인트
                    val detailExpandOffsetPx = (titleBarTopPx + titleBarBottomPx) / 2f
                    val detailCollapsedOffsetPx = remember(screenHeightPx) {
                        screenHeightPx - with(density) { screenHeightPx / 3 }
                    }
                    val detailHalfOffsetPx = detailExpandOffsetPx + (screenHeightPx - detailExpandOffsetPx) * 0.5f

                    val detailSheetAnim = remember(screenHeightPx) {
                        Animatable(screenHeightPx)
                    }

                    val selectedLibCode = libraryMapUiState.value.selectedLibCode

                    val handleBack = {
                        if (selectedLibCode != null) {
                            libraryMapViewModelEvent(LibraryMapViewModelEvent.SelectMarker(null))
                        } else {
                            onBackPressed()
                        }
                    }

                    BackHandler {
                        handleBack()
                    }

                    LaunchedEffect(selectedLibCode) {
                        if (selectedLibCode != null) {
                            detailSheetAnim.snapTo(screenHeightPx)
                            detailSheetAnim.animateTo(
                                targetValue = detailCollapsedOffsetPx,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
                            )
                        } else {
                            detailSheetAnim.animateTo(
                                targetValue = screenHeightPx,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)
                            )
                        }
                    }

                    val detailNestedScrollConnection = remember(detailExpandOffsetPx, detailCollapsedOffsetPx, screenHeightPx) {
                        object : NestedScrollConnection {
                            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                                val delta = available.y
                                if (delta < 0 && detailSheetAnim.value > detailExpandOffsetPx) {
                                    val newOffset = (detailSheetAnim.value + delta).coerceIn(detailExpandOffsetPx, screenHeightPx)
                                    val consumed = newOffset - detailSheetAnim.value
                                    coroutineScope.launch { detailSheetAnim.snapTo(newOffset) }
                                    return Offset(0f, consumed)
                                }
                                return Offset.Zero
                            }
                            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                                val delta = available.y
                                if (delta > 0 && detailSheetAnim.value < detailCollapsedOffsetPx) {
                                    val newOffset = (detailSheetAnim.value + delta).coerceIn(detailExpandOffsetPx, detailCollapsedOffsetPx)
                                    val consumed2 = newOffset - detailSheetAnim.value
                                    coroutineScope.launch { detailSheetAnim.snapTo(newOffset) }
                                    return Offset(0f, consumed2)
                                }
                                return Offset.Zero
                            }
                        }
                    }

                    // 지도
                    val markerItems = holdingLibraryListPaging?.itemSnapshotList?.items ?: emptyList()

                    // 지역이 바뀔 때(holdingLibraryListPaging 교체)만 카메라 재조정
                    // 동일 지역 내 페이징은 리스트만 추가되고 카메라는 유지
                    LaunchedEffect(holdingLibraryListPaging) {
                        if (isPreview || holdingLibraryListPaging == null) return@LaunchedEffect
                        // 첫 아이템이 도착할 때까지 대기 후 단 한 번 bounds 계산
                        snapshotFlow { holdingLibraryListPaging.itemSnapshotList.items }
                            .first { it.isNotEmpty() }
                            .let { items ->
                                val validCoords = items.mapNotNull { item ->
                                    val lat = item.lib.latitude?.toDoubleOrNull()
                                    val lon = item.lib.longitude?.toDoubleOrNull()
                                    if (lat != null && lon != null) LatLng(lat, lon) else null
                                }
                                if (validCoords.isNotEmpty()) {
                                    val bounds = LatLngBounds.Builder().apply {
                                        validCoords.forEach { include(it) }
                                    }.build()
                                    cameraPositionState?.animate(
                                        update = CameraUpdate.fitBounds(
                                            bounds,
                                            100,
                                            titleBarBottomPx.toInt() + 100,
                                            100,
                                            100
                                        ),
                                        durationMs = 500
                                    )
                                }
                            }
                    }

                    if (isPreview) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colorResource(R.color.color_E5E8EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.library_map_preview_placeholder),
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
                                .fillMaxWidth()
                                .height(with(density) { (initialOffsetPx + 24.dp.toPx()).toDp() }),
                            cameraPositionState = cameraPositionState!!
                        ) {
                            // 내 위치 마커
                            val userLat = userLatitude
                            val userLon = userLongitude
                            if (userLat != null && userLon != null) {
                                Marker(
                                    state = rememberUpdatedMarkerState(position = LatLng(userLat, userLon)),
                                    icon = OverlayImage.fromResource(R.drawable.ic_user_location_marker_20x20)
                                )
                            }
                            // 도서관 마커
                            markerItems.forEach { item ->
                                val lat = item.lib.latitude?.toDoubleOrNull()
                                val lon = item.lib.longitude?.toDoubleOrNull()
                                if (lat != null && lon != null) {
                                    val isSelected = selectedLibCode == item.lib.libCode
                                    Marker(
                                        state = rememberUpdatedMarkerState(
                                            position = LatLng(lat, lon)
                                        ),
                                        icon = OverlayImage.fromResource(
                                            if (isSelected) R.drawable.ic_point_marker_blue_40x50
                                            else R.drawable.ic_point_marker_blue_28x35
                                        ),
                                        captionText = item.lib.libName ?: "",
                                        onClick = {
                                            libraryMapViewModelEvent.invoke(LibraryMapViewModelEvent.SelectMarker(item.lib.libCode))
                                            true
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 내 위치 이동 버튼 (현재 활성 바텀시트 상단 15dp 위, 오른쪽 끝 15dp 여백)
                    val myLocBtnSizePx = with(density) { 48.dp.toPx() }
                    val myLocBtnMarginPx = with(density) { 15.dp.toPx() }
                    val activeSheetY = if (selectedLibCode == null) sheetOffsetY else detailSheetAnim.value
                    val myLocBtnY = (activeSheetY - myLocBtnMarginPx - myLocBtnSizePx)
                        .coerceAtLeast(titleBarBottomPx + myLocBtnMarginPx)
                    val myLocBtnX = constraints.maxWidth - myLocBtnMarginPx - myLocBtnSizePx

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .offset { IntOffset(myLocBtnX.roundToInt(), myLocBtnY.roundToInt()) }
                            .shadow(elevation = 4.dp, shape = CircleShape)
                            .background(colorResource(R.color.color_FFFFFFFF), CircleShape)
                            .clickable {
                                libraryMapViewModelEvent.invoke(LibraryMapViewModelEvent.RequestMoveToMyLocation)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_my_location_24x24),
                            contentDescription = ""
                        )
                    }

                    // 자유 드래그 바텀시트 (마커 선택 시 숨김)
                    LibraryListSheet(
                        modifier = Modifier.offset { IntOffset(x = 0, y = if (selectedLibCode == null) sheetOffsetY.roundToInt() else screenHeightPx.toInt()) },
                        isPreview = isPreview,
                        nestedScrollConnection = nestedScrollConnection,
                        onDrag = { delta -> sheetOffsetY = (sheetOffsetY + delta).coerceIn(minOffsetPx, maxOffsetPx) },
                        navigationBarHeight = state.navigationBarHeight,
                        previewItems = previewHoldingLibraryItems,
                        userLatitude = userLatitude,
                        userLongitude = userLongitude,
                        currentDetailRegion = libraryMapUiState.value.currentDetailRegion,
                        holdingLibraryListPaging = holdingLibraryListPaging,
                        onRegionHeaderClick = { showRegionSheet = true },
                        onLibraryItemClick = { item ->
                            libraryMapViewModelEvent(LibraryMapViewModelEvent.SelectMarker(item.lib.libCode))
                            val lat = item.lib.latitude?.toDoubleOrNull()
                            val lon = item.lib.longitude?.toDoubleOrNull()
                            if (lat != null && lon != null) {
                                coroutineScope.launch {
                                    cameraPositionState?.animate(
                                        update = CameraUpdate.scrollTo(LatLng(lat, lon)),
                                        durationMs = 500
                                    )
                                }
                            }
                        }
                    )

                    // 마커 선택 시 detail 바텀시트
                    val selectedItem = holdingLibraryListPaging?.itemSnapshotList?.items
                        ?.firstOrNull { it.lib.libCode == selectedLibCode }
                    LibraryDetailSheet(
                        localContext = localContext,
                        modifier = Modifier.offset { IntOffset(x = 0, y = detailSheetAnim.value.roundToInt()) },
                        item = selectedItem,
                        book = book,
                        resBookDetail = libraryMapUiState.value.resBookDetail,
                        userLatitude = userLatitude,
                        userLongitude = userLongitude,
                        resCheckBookAvailability = resCheckBookAvailability,
                        resLibraryBookData = libraryMapUiState.value.resLibraryBookData,
                        isLibraryBookDataLoading = libraryMapUiState.value.isLibraryBookDataLoading,
                        nestedScrollConnection = detailNestedScrollConnection,
                        onDrag = { delta ->
                            val newOffset = (detailSheetAnim.value + delta)
                                .coerceIn(detailExpandOffsetPx, screenHeightPx)
                            coroutineScope.launch { detailSheetAnim.snapTo(newOffset) }
                        },
                        onDragStopped = { velocity ->
                            val snapPoints = listOf(detailExpandOffsetPx, detailHalfOffsetPx, detailCollapsedOffsetPx)
                            val current = detailSheetAnim.value
                            val target: Float = when {
                                velocity > 800f -> snapPoints.firstOrNull { it > current } ?: (detailCollapsedOffsetPx + 1f)
                                velocity < -800f -> snapPoints.lastOrNull { it < current } ?: detailExpandOffsetPx
                                else -> snapPoints.minByOrNull { kotlin.math.abs(it - current) } ?: detailCollapsedOffsetPx
                            }
                            if (target > detailCollapsedOffsetPx) {
                                detailSheetAnim.animateTo(screenHeightPx, spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium))
                                libraryMapViewModelEvent(LibraryMapViewModelEvent.SelectMarker(null))
                            } else {
                                detailSheetAnim.animateTo(target, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium))
                            }
                        },
                        navigationBarHeight = state.navigationBarHeight,
                        extraBottomPadding = with(density) { titleBarBottomPx.toDp() },
                    )

                    // 바텀시트가 타이틀 바에 닿을 때 하얀 배경 오버레이
                    // 마커 선택 시 메인 시트는 숨겨지므로 mainCoverAlpha를 0으로 처리.
                    // selectedLibCode가 null로 돌아오면 sheetOffsetY가 보존되어 있으므로 이전 오버레이 상태가 자동 복원됨.
                    val coverThresholdPx = with(density) { 60.dp.toPx() }
                    val mainCoverAlpha = if (selectedLibCode != null) 0f
                        else ((titleBarBottomPx + coverThresholdPx - sheetOffsetY) / coverThresholdPx).coerceIn(0f, 1f)
                    val detailCoverAlpha = ((titleBarBottomPx + coverThresholdPx - detailSheetAnim.value) / coverThresholdPx)
                        .coerceIn(0f, 1f)
                    val coverAlpha = maxOf(mainCoverAlpha, detailCoverAlpha)

                    if (coverAlpha > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(with(density) { titleBarBottomPx.toDp() })
                                .alpha(coverAlpha)
                                .background(colorResource(R.color.color_FFFFFFFF))
                        )
                    }

                    // 상단 타이틀 바
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 10.dp + state.statusBarHeight,
                                start = 10.dp,
                                end = 10.dp
                            )
                            .clip(RoundedCornerShape(999.dp))
                            .onGloballyPositioned { coords ->
                                titleBarTopPx = coords.boundsInParent().top
                                titleBarBottomPx = coords.boundsInParent().bottom
                            }
                            .background(colorResource(R.color.color_FFFFFFFF))
                            .padding(horizontal = 4.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_arrow_left_black_40x40),
                            contentDescription = "",
                            modifier = Modifier.clickable { handleBack() }
                        )
                        Text(
                            text = bookTitle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                color = colorResource(R.color.color_191F28),
                                fontSize = dpToSp(16.dp),
                                fontFamily = NotoSansKR,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }

        // 지역 임시 변경 바텀시트 (저장 없이 조회용)
        if (showRegionSheet) {
            RegionSelectBottomSheet(
                currentDetailRegion = libraryMapUiState.value.currentDetailRegion,
                onDismiss = { showRegionSheet = false },
                onRegionSelected = { detailRegion ->
                    showRegionSheet = false
                    libraryMapViewModelEvent(LibraryMapViewModelEvent.UpdateRegion(detailRegion))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegionSelectBottomSheet(
    currentDetailRegion: LibraryData.DetailRegion?,
    onDismiss: () -> Unit,
    onRegionSelected: (LibraryData.DetailRegion) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedRegion by remember {
        mutableStateOf(
            currentDetailRegion?.let { dr ->
                LibraryData.regionList.find { it.code == dr.regionCode }
            }
        )
    }
    var selectedDetailRegion by remember { mutableStateOf(currentDetailRegion) }

    // LazyColumn 잔여 스크롤이 ModalBottomSheet으로 전파되지 않도록 차단
    // (각 LazyColumn에 직접 적용해야 효과적)
    val blockOverscrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset = available.copy(x = 0f)
        }
    }

    val detailListState = androidx.compose.foundation.lazy.rememberLazyListState()
    LaunchedEffect(selectedRegion) {
        detailListState.scrollToItem(0)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colorResource(R.color.color_FFFFFFFF)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
        ) {
            // 헤더
            Text(
                text = stringResource(R.string.select_region_selection_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                style = TextStyle(
                    color = colorResource(R.color.color_191F28),
                    fontSize = dpToSp(16.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colorResource(R.color.color_E5E8EB))
            )

            // 지역 선택 (좌: 시/도, 우: 구/군)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // 시/도 목록
                LazyColumn(
                    modifier = Modifier
                        .width(128.dp)
                        .fillMaxHeight()
                        .background(colorResource(R.color.color_F9FAFB))
                        .nestedScroll(blockOverscrollConnection)
                ) {
                    items(count = LibraryData.regionList.size, key = { LibraryData.regionList[it].code }) { index ->
                        val region = LibraryData.regionList[index]
                        val isSelected = region == selectedRegion
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .noRippleClickable {
                                    selectedRegion = region
                                    selectedDetailRegion = null
                                }
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(region.nameRes),
                                style = TextStyle(
                                    color = colorResource(if (isSelected) R.color.color_3182F6 else R.color.color_6B7684),
                                    fontSize = dpToSp(16.dp),
                                    fontFamily = NotoSansKR,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }

                // 구분선
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(colorResource(R.color.color_E5E8EB))
                )

                // 구/군 목록
                val filteredDetails = LibraryData.allDetailRegions.filter { it.regionCode == selectedRegion?.code }
                if (selectedRegion == null) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.library_map_region_select_hint),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                color = colorResource(R.color.color_6B7684),
                                fontSize = dpToSp(14.dp),
                                fontFamily = NotoSansKR
                            )
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .nestedScroll(blockOverscrollConnection),
                        state = detailListState
                    ) {
                        items(count = filteredDetails.size, key = { filteredDetails[it].code }) { index ->
                            val detail = filteredDetails[index]
                            val isSelected = detail == selectedDetailRegion
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .noRippleClickable { selectedDetailRegion = detail }
                                    .padding(horizontal = 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(detail.districtNameRes),
                                    modifier = Modifier.weight(1f),
                                    style = TextStyle(
                                        color = colorResource(if (isSelected) R.color.color_3182F6 else R.color.color_6B7684),
                                        fontSize = dpToSp(16.dp),
                                        fontFamily = NotoSansKR,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                if (isSelected) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_check_blue_17x13),
                                        contentDescription = null
                                    )
                                }
                            }
                            if (index < filteredDetails.lastIndex) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(colorResource(R.color.color_F2F4F6))
                                )
                            }
                        }
                    }
                }
            }

            // 적용 버튼
            val isEnabled = selectedDetailRegion != null
            Button(
                onClick = { selectedDetailRegion?.let { onRegionSelected(it) } },
                enabled = isEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 24.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.library_map_apply),
                    style = TextStyle(
                        fontSize = dpToSp(16.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
private fun LibraryListSheet(
    modifier: Modifier = Modifier,
    isPreview: Boolean,
    nestedScrollConnection: NestedScrollConnection,
    onDrag: (Float) -> Unit,
    navigationBarHeight: Dp,
    previewItems: List<ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper>,
    userLatitude: Double?,
    userLongitude: Double?,
    currentDetailRegion: LibraryData.DetailRegion?,
    holdingLibraryListPaging: LazyPagingItems<ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper>?,
    onRegionHeaderClick: () -> Unit,
    onLibraryItemClick: (ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
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
                        state = rememberDraggableState { delta -> onDrag(delta) }
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
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = navigationBarHeight)
            ) {
                if (isPreview) {
                    items(
                        count = previewItems.size,
                        key = { index -> previewItems[index].lib.libCode ?: index }
                    ) { index ->
                        LibraryListItem(
                            item = previewItems[index],
                            userLatitude = userLatitude,
                            userLongitude = userLongitude
                        )
                    }
                } else {
                    item {
                        val districtName = currentDetailRegion?.districtNameRes
                            ?.let { stringResource(it) } ?: ""
                        val libraryCount = holdingLibraryListPaging?.itemCount ?: 0
                        val isRefreshDone = holdingLibraryListPaging?.loadState?.refresh is LoadState.NotLoading
                        val hasLibraries = libraryCount > 0

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onRegionHeaderClick() }
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_point_marker_black_15x19),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = districtName,
                                style = TextStyle(
                                    color = colorResource(R.color.color_191F28),
                                    fontSize = dpToSp(16.dp),
                                    fontFamily = NotoSansKR,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = dpToSp(24.dp)
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "•",
                                style = TextStyle(
                                    color = colorResource(R.color.color_191F28),
                                    fontSize = dpToSp(16.dp),
                                    fontFamily = NotoSansKR,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (!isRefreshDone) {
                                    ""
                                } else if (hasLibraries) {
                                    stringResource(R.string.library_map_library_count, libraryCount)
                                } else {
                                    stringResource(R.string.library_map_no_library)
                                },
                                style = TextStyle(
                                    color = colorResource(R.color.color_191F28),
                                    fontSize = dpToSp(16.dp),
                                    fontFamily = NotoSansKR,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = dpToSp(24.dp)
                                )
                            )
                        }
                    }
                    holdingLibraryListPaging?.let { pagingItems ->
                        val isRefreshDone = pagingItems.loadState.refresh is LoadState.NotLoading
                        if (isRefreshDone && pagingItems.itemCount == 0) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 48.dp, bottom = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_book_grey_57x64),
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = stringResource(R.string.library_map_no_library_main),
                                        style = TextStyle(
                                            color = colorResource(R.color.color_191F28),
                                            fontSize = dpToSp(16.dp),
                                            fontFamily = NotoSansKR,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = stringResource(R.string.library_map_no_library_sub),
                                        style = TextStyle(
                                            color = colorResource(R.color.color_6B7684),
                                            fontSize = dpToSp(14.dp),
                                            fontFamily = NotoSansKR,
                                            fontWeight = FontWeight.Normal
                                        )
                                    )
                                }
                            }
                        } else {
                            items(
                                count = pagingItems.itemCount,
                                key = { index -> pagingItems.peek(index)?.lib?.libCode ?: index }
                            ) { index ->
                                val item = pagingItems[index] ?: return@items
                                LibraryListItem(
                                    item = item,
                                    userLatitude = userLatitude,
                                    userLongitude = userLongitude,
                                    onClick = { onLibraryItemClick(item) }
                                )
                            }
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

@Composable
private fun LibraryDetailSheet(
    localContext: Context,
    modifier: Modifier = Modifier,
    item: ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper? = null,
    book: ResSearchBook.ResponseData.BookWrapper? = null,
    resBookDetail: ResBookDetail? = null,
    userLatitude: Double? = null,
    userLongitude: Double? = null,
    resCheckBookAvailability: ResCheckBookAvailability? = null,
    resLibraryBookData: ResLibraryBookData? = null,
    isLibraryBookDataLoading: Boolean = false,
    nestedScrollConnection: NestedScrollConnection,
    onDrag: (Float) -> Unit,
    onDragStopped: suspend (Float) -> Unit,
    navigationBarHeight: Dp,
    extraBottomPadding: Dp = 0.dp,
) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
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
                            onDrag(delta)
                        },
                        onDragStopped = { velocity ->
                            coroutineScope.launch { onDragStopped(velocity) }
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

            // Detail 콘텐츠
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = navigationBarHeight + extraBottomPadding)
            ) {
                // 도서관 기본 정보
                item {
                    LibraryInfoView(
                        localContext = localContext,
                        libraryInfo = item
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }

                // 도서 대출 현황
                item {
                    BookAvailabilityView(
                        resCheckBookAvailability = resCheckBookAvailability,
                        resLibraryBookData = resLibraryBookData,
                        isLibraryBookDataLoading = isLibraryBookDataLoading
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }

                // 검색한 도서 정보
                item {
                    BookInfoView(
                        book = book,
                        resBookDetail = resBookDetail
                    )
                }
            }
        }
    }
}

@Composable
fun LibraryInfoView(
    localContext: Context,
    libraryInfo: ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = libraryInfo?.lib?.libName?:"",
            style = TextStyle(
                color = colorResource(R.color.color_191F28),
                fontSize = dpToSp(20.dp),
                lineHeight = dpToSp(24.dp),
                fontFamily = NotoSansKR,
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(16.dp))

        libraryInfo?.lib?.address?.let { address ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_point_marker_grey_12x15),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 3.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = address,
                    style = TextStyle(
                        color = colorResource(R.color.color_6B7684),
                        fontSize = dpToSp(14.dp),
                        lineHeight = dpToSp(22.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        libraryInfo?.lib?.operatingTime?.let { operatingTime ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_clock_grey_15x15),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 3.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = operatingTime,
                    style = TextStyle(
                        color = colorResource(R.color.color_6B7684),
                        fontSize = dpToSp(14.dp),
                        lineHeight = dpToSp(22.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        libraryInfo?.lib?.closed?.let { closed ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_calendar_grey_14x15),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 3.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = closed,
                    style = TextStyle(
                        color = colorResource(R.color.color_6B7684),
                        fontSize = dpToSp(14.dp),
                        lineHeight = dpToSp(22.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 전화 버튼 + 홈페이지 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${libraryInfo?.lib?.tel}"))
                    localContext.startActivity(intent)
                }
            ) {
                Text(text = stringResource(R.string.library_map_call))
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    val uri = Uri.parse(libraryInfo?.lib?.homepage)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    localContext.startActivity(intent)
                }
            ) {
                Text(text = stringResource(R.string.library_map_homepage))
            }
        }
    }
}

@Composable
fun BookAvailabilityView(
    resCheckBookAvailability: ResCheckBookAvailability? = null,
    resLibraryBookData: ResLibraryBookData? = null,
    isLibraryBookDataLoading: Boolean = false,
) {
    val callNumberText = remember(resLibraryBookData) {
        resLibraryBookData?.response?.docs
            ?.mapNotNull {
                it.doc
            }
            ?.firstOrNull { doc ->
                !doc.classNo.isNullOrBlank() &&
                doc.callNumbers?.any { wrapper -> !wrapper.callNumber?.bookCode.isNullOrBlank() } == true
            }
            ?.let { doc ->
                val bookCode = doc.callNumbers
                    ?.firstOrNull { !it.callNumber?.bookCode.isNullOrBlank() }
                    ?.callNumber?.bookCode
                "${doc.classNo} - $bookCode"
            }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.library_map_book_availability_title),
            style = TextStyle(
                color = colorResource(R.color.color_191F28),
                fontSize = dpToSp(20.dp),
                lineHeight = dpToSp(24.dp),
                fontFamily = NotoSansKR,
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = colorResource(R.color.color_F2F4F6),
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = colorResource(R.color.color_FFFFFFFF),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledCircle(color = if(resCheckBookAvailability?.response?.result?.loanAvailable == "Y") {
                    colorResource(R.color.color_03B26C)
                } else if(resCheckBookAvailability?.response?.result?.loanAvailable == "N") {
                    colorResource(R.color.color_FF0000)
                } else {
                    colorResource(R.color.color_8B95A1)
                })
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if(resCheckBookAvailability?.response?.result?.loanAvailable == "Y") {
                        stringResource(R.string.library_map_book_availability_available)
                    } else if(resCheckBookAvailability?.response?.result?.loanAvailable == "N") {
                        stringResource(R.string.library_map_book_availability_on_loan)
                    } else {
                        stringResource(R.string.library_map_book_availability_check_homepage)
                    },
                    style = TextStyle(
                        color = colorResource(R.color.color_191F28),
                        fontSize = dpToSp(16.dp),
                        lineHeight = dpToSp(24.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.library_map_book_availability_notice),
                style = TextStyle(
                    color = colorResource(R.color.color_6B7684),
                    fontSize = dpToSp(12.dp),
                    lineHeight = dpToSp(18.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.library_map_book_availability_call_number_title),
                style = TextStyle(
                    color = colorResource(R.color.color_4E5968),
                    fontSize = dpToSp(13.dp),
                    lineHeight = dpToSp(20.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = colorResource(R.color.color_4DC1C6D6),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = colorResource(R.color.color_F2F3FD),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = if (isLibraryBookDataLoading) {
                        stringResource(R.string.library_map_book_availability_call_number_loading)
                    } else {
                        callNumberText ?: stringResource(R.string.library_map_book_availability_call_number_no_info)
                    },
                    style = TextStyle(
                        color = colorResource(R.color.color_191F28),
                        fontSize = dpToSp(14.dp),
                        lineHeight = dpToSp(22.dp),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun BookInfoView(
    book: ResSearchBook.ResponseData.BookWrapper? = null,
    resBookDetail: ResBookDetail? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.library_map_book_info_title),
            style = TextStyle(
                color = colorResource(R.color.color_191F28),
                fontSize = dpToSp(20.dp),
                lineHeight = dpToSp(24.dp),
                fontFamily = NotoSansKR,
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = colorResource(R.color.color_F2F4F6),
                    shape = RoundedCornerShape(24.dp)
                )
                .background(
                    color = colorResource(R.color.color_0D000000),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 이미지
            var bookImageContentScale by remember { mutableStateOf<ContentScale>(ContentScale.Crop) }
            AsyncImage(
                model = book?.doc?.bookImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(width = 128.dp, height = 192.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = bookImageContentScale,
                placeholder = painterResource(R.drawable.ic_book_grey_21x18),
                error = painterResource(R.drawable.ic_book_grey_21x18),
                onError = {
                    bookImageContentScale = ContentScale.Inside
                },
                onSuccess = {
                    bookImageContentScale = ContentScale.Crop
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 제목
            Text(
                text = book?.doc?.bookName ?: "",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    color = colorResource(R.color.color_191F28),
                    fontSize = dpToSp(16.dp),
                    lineHeight = dpToSp(36.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 지은이 · 출판사
            Text(
                text = listOfNotNull(
                    book?.doc?.authors?.replace(Regex("지은이\\s*:\\s*"), "")?.takeIf { it.isNotBlank() },
                    book?.doc?.publisher?.takeIf { it.isNotBlank() }
                ).joinToString(" · "),
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    color = colorResource(R.color.color_6B7684),
                    fontSize = dpToSp(16.dp),
                    lineHeight = dpToSp(24.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(20.dp))

            // 디바이더
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colorResource(R.color.color_E5E8EB))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 줄거리
            Text(
                text = resBookDetail?.response?.detail?.firstOrNull()?.book?.description ?: "",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    color = colorResource(R.color.color_4E5968),
                    fontSize = dpToSp(14.dp),
                    lineHeight = dpToSp(25.dp),
                    fontFamily = NotoSansKR,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun FilledCircle(color: Color) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .background(color = color, shape = CircleShape)
    )
}

@Composable
private fun LibraryListItem(
    item: ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper,
    userLatitude: Double?,
    userLongitude: Double?,
    onClick: () -> Unit = {}
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
            .clickable { onClick() }
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
                Text(text = stringResource(R.string.library_map_call))
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    val uri = Uri.parse(item.lib.homepage)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                }
            ) {
                Text(text = stringResource(R.string.library_map_homepage))
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
private fun LibraryListSheetPreview() {
    val mockItems = listOf(
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
        )
    )
    LibraryListSheet(
        isPreview = true,
        nestedScrollConnection = object : NestedScrollConnection {},
        onDrag = {},
        navigationBarHeight = 0.dp,
        previewItems = mockItems,
        userLatitude = 37.5665,
        userLongitude = 126.9780,
        currentDetailRegion = null,
        holdingLibraryListPaging = null,
        onRegionHeaderClick = {},
        onLibraryItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun LibraryDetailSheetPreview() {
    val mockItem = ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper(
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
    )
    val mockBook = ResSearchBook.ResponseData.BookWrapper(
        doc = ResSearchBook.ResponseData.BookWrapper.BookInfo(
            bookName = "미움받을 용기 - 자유롭고 행복한 삶을 위한 아들러의 가르침",
            authors = "기시미 이치로, 고가 후미타케 지음",
            publisher = "인플루엔셜",
            publicationYear = "2014",
            isbn13 = "9788996991342",
            additionSymbol = null,
            vol = null,
            classNo = "189",
            classNm = "철학 > 심리학 > 응용심리학 일반",
            bookImageUrl = null,
            bookDtlUrl = null,
            loanCount = null
        )
    )
    LibraryDetailSheet(
        localContext = LocalContext.current,
        item = mockItem,
        book = mockBook,
        userLatitude = 37.5665,
        userLongitude = 126.9780,
        nestedScrollConnection = object : NestedScrollConnection {},
        onDrag = {},
        onDragStopped = {},
        navigationBarHeight = 0.dp
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
        bookTitle = "미움받을 용기 - 자유롭고 행복한 삶을 위한 아들러의 가르침",
        libraryMapViewModelEvent = {},
        libraryMapUiState = remember { mutableStateOf(LibraryMapUiState()) },
        holdingLibraryListPaging = null,
        previewHoldingLibraryItems = mockData,
        userLatitude = 37.5665,
        userLongitude = 126.9780,
        resCheckBookAvailability = null
    )
}
