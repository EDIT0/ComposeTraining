package com.my.composebottomsheetdemo1.screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.my.composebottomsheetdemo1.KakaoMapUtil
import com.my.composebottomsheetdemo1.LogUtil
import com.my.composebottomsheetdemo1.ViewSizeUtil
import com.my.composebottomsheetdemo1.navigation.NavigationScreenName
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.FlexibleSheetValue
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import kotlinx.coroutines.CoroutineScope

@Composable
fun ThirdMapBottomSheetScreen(
    navController: NavController
) {
    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()
    val kakaoMap: MutableState<KakaoMap?> = remember {
        mutableStateOf(null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White
            )
            .padding(
                top = if (true) {
                    WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding()
                } else {
                    0.dp
                },
                bottom = if (true) {
                    WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding()
                } else {
                    0.dp
                },
            ),
        content = {
            ThirdMapBottomSheetScreenUI(
                localContext = localContext,
                scope = scope,
                kakaoMap = kakaoMap,
                moveToFirst = {
                    navController.navigate(route = NavigationScreenName.SecondBottomSheetScreen.name)
                }
            )
        }
    )

//    BottomSheet()
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdMapBottomSheetScreenUI(
    localContext: Context,
    scope: CoroutineScope,
    kakaoMap: MutableState<KakaoMap?>,
    moveToFirst: () -> Unit
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp // 스크린 높이
    val screenWidth = configuration.screenWidthDp // 스크린 너비

    val contentScrollState = rememberScrollState()

    val sheetContentHandleHeight = rememberSaveable { // 바텀시트 핸들 높이
        mutableStateOf(0)
    }

    val newCenter : MutableState<LatLng?> = remember {
        mutableStateOf(null)
    }

    val mapView = KakaoMapUtil.setKakaoMap(
        localContext,
        NavigationScreenName.ThirdMapBottomSheetScreen.name,
        getKakaoMap = {
            kakaoMap.value = it
        },
        getMapViewInfo = {

        }
    )

    val flexibleBottomSheetState = rememberFlexibleBottomSheetState(
        isModal = false,
        skipHiddenState = true,
        skipSlightlyExpanded = false,
        skipIntermediatelyExpanded = false,
        flexibleSheetSize = FlexibleSheetSize(
            fullyExpanded = 1f,
            intermediatelyExpanded = 0.4f,
            slightlyExpanded = 0.1f,
        ),
        allowNestedScroll = false,
        containSystemBars = false
    )

    val realMovingHeight = rememberSaveable {
        mutableStateOf(0f)
    }

    val realOffsetY = rememberSaveable {
        mutableStateOf(0f)
    }

    val viewMode = remember {
        mutableStateOf(BsViewMode.One)
    }


    // 스크롤 방향을 기억하는 상태
    var isScrollingUp by remember { mutableStateOf(false) }

    // 이전 스크롤 위치를 저장할 상태
    var previousScrollOffset by remember { mutableStateOf(0f) }

    try {
        // 이전 위치와 현재 위치를 비교하여 스크롤 방향 결정
        isScrollingUp = flexibleBottomSheetState.swipeableState.requireOffset() < previousScrollOffset
        previousScrollOffset = flexibleBottomSheetState.swipeableState.requireOffset()
    } catch (e: Exception) {

    }

    var expand by rememberSaveable {
        mutableStateOf(0f)
    }
    var half by rememberSaveable {
        mutableStateOf(0f)
    }
    var collapse by rememberSaveable {
        mutableStateOf(0f)
    }


    var onePixelDistance by rememberSaveable {
        mutableStateOf(0.0)
    }
    var slideOffset by rememberSaveable {
        mutableStateOf(0f)
    }
    var currentCollapseLat by rememberSaveable {
        mutableStateOf(0.0)
    }
    var currentHalfLat by rememberSaveable {
        mutableStateOf(0.0)
    }
    var currentColHalfDistanceLat by rememberSaveable {
        mutableStateOf(0.0)
    }

    try {
        slideOffset = calculateSlideOffset(flexibleBottomSheetState.requireOffset(), ViewSizeUtil.dpToPx(localContext, screenHeight.toFloat()).toFloat())
//        LogUtil.i_dev("MYTAG SlideOffset: ${slideOffset} Offset: ${flexibleBottomSheetState.requireOffset()} ScreenHeight: ${ViewSizeUtil.dpToPx(localContext, screenHeight.toFloat()).toFloat()}")

//        LogUtil.d_dev("flexibleSheetSize: ${flexibleBottomSheetState.flexibleSheetSize}")
//        LogUtil.d_dev("currentValue: ${flexibleBottomSheetState.currentValue}")
        LogUtil.d_dev("progress: ${flexibleBottomSheetState.swipeableState.progress}")
//        LogUtil.d_dev("anchor: ${flexibleBottomSheetState.swipeableState.anchors}")

        val offset = flexibleBottomSheetState.requireOffset()
        expand = flexibleBottomSheetState.swipeableState.anchors[FlexibleSheetValue.FullyExpanded]?:0f
        half = flexibleBottomSheetState.swipeableState.anchors[FlexibleSheetValue.IntermediatelyExpanded]?:0f
        collapse = flexibleBottomSheetState.swipeableState.anchors[FlexibleSheetValue.SlightlyExpanded]?:0f

//        LogUtil.d_dev("MYTAG expand: ${expand} / half: ${half} / collapse: ${collapse}")

        val movingTotalHeight = collapse - half
        realMovingHeight.value = movingTotalHeight
        val offsetY = half - offset
        if(half < offset && collapse > offset) {
//            LogUtil.d_dev("MYTAG 이때만 지도가 움직여야합니다. offsetY: ${offsetY} / offset: ${offset}")
            realOffsetY.value = offsetY

//            val y = -(ViewSizeUtil.pxToDp(localContext, realMovingHeight.value) + ViewSizeUtil.pxToDp(localContext, realOffsetY.value))
//            LogUtil.d_dev("MYTAG y: ${y}")

//            val newCenter : Point = kakaoMap.value?.toScreenPoint(kakaoMap.value?.cameraPosition?.position!!)!!
//            val center = kakaoMap.value?.fromScreenPoint(newCenter.value!!.x, newCenter.value!!.y)
//
//            val newPoint : Point = Point(0, 0)
//            if(isScrollingUp) {
//                newPoint.set(newCenter.value!!.x, newCenter.value!!.y + fraction.toInt())
//            } else {
//                newPoint.set(newCenter.value!!.x, newCenter.value!!.y - fraction.toInt())
//            }

//            LogUtil.d_dev("MYTAG cX: ${newCenter.value!!.x} cY: ${newCenter.value!!.y} / ${kakaoMap.value?.fromScreenPoint(newCenter.value!!.x, newCenter.value!!.y)}")
//            LogUtil.d_dev("MYTAG x: ${newPoint.x} y: ${newPoint.y}")

//            val changed = kakaoMap.value?.fromScreenPoint(newPoint.x, newPoint.y)

//            kakaoMap.value?.moveCamera(
//                CameraUpdateFactory.newCenterPosition(
//                    LatLng.from(changed!!.latitude, changed!!.longitude), 16)
//            )
        }
        LogUtil.d_dev("${ViewSizeUtil.pxToDp(localContext, realOffsetY.value).dp} / ${ViewSizeUtil.pxToDp(localContext, realMovingHeight.value).dp}")
        LogUtil.d_dev("Move 1: ${-ViewSizeUtil.pxToDp(localContext, realOffsetY.value).dp - ViewSizeUtil.pxToDp(localContext, realMovingHeight.value).dp}")
        LogUtil.d_dev("Move 2: ${ViewSizeUtil.pxToDp(localContext, realMovingHeight.value).dp + ViewSizeUtil.pxToDp(localContext, realOffsetY.value).dp}")
    } catch (e: Exception) {

    }

    var heightChange by rememberSaveable {
        mutableStateOf(0.0)
    }

    LaunchedEffect(key1 = slideOffset) {
        if(slideOffset > 0.1 && slideOffset < 0.4) {
            val normalizedOffset = (slideOffset - 0.1) / (0.4 - 0.1)
            heightChange = normalizedOffset * realMovingHeight.value
//            if(isScrollingUp) {
////                val newLat = kakaoMap.value?.cameraPosition?.position?.latitude!! - heightChange * onePixelDistance
//                val newLat = normalizedOffset * currentColHalfDistanceLat + currentCollapseLat
//                kakaoMap.value?.moveCamera(
//                    CameraUpdateFactory.newCenterPosition(LatLng.from(newLat, kakaoMap.value?.cameraPosition?.position?.longitude!!))
//                )
//            } else {
////                val newLat = kakaoMap.value?.cameraPosition?.position?.latitude!! + heightChange * onePixelDistance
//                val newLat = currentHalfLat - normalizedOffset * currentColHalfDistanceLat
//                kakaoMap.value?.moveCamera(
//                    CameraUpdateFactory.newCenterPosition(LatLng.from(newLat, kakaoMap.value?.cameraPosition?.position?.longitude!!))
//                )
//            }

            LogUtil.i_dev("MYTAG heightChange: ${heightChange}\nonePixelDistance: ${onePixelDistance}\ncurrentCollapseLat: ${currentCollapseLat}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = -ViewSizeUtil.pxToDp(localContext, heightChange.toFloat()).dp / 2),
//                    .offset(
//                        y = -(ViewSizeUtil.pxToDp(
//                            localContext,
//                            realMovingHeight.value
//                        ).dp + ViewSizeUtil.pxToDp(localContext, realOffsetY.value).dp)
//                    ),
                factory = { context ->
                    mapView!!
                }
            )

            Row {
                Button(onClick = { moveToFirst.invoke() }) {
                    Text(text = "다른 화면으로")
                }

                Button(onClick = {
                    viewMode.value = BsViewMode.One
                }) {
                    Text(text = "단계별")
                }

                Button(onClick = {
                    viewMode.value = BsViewMode.Two
                }) {
                    Text(text = "마커정보")
                }

                Button(onClick = {
//                    val newCenterX = kakaoMap.value?.viewport!!.width() / 2
//                    val newCenterY = (expand + half) / 2
//                    val latLng = kakaoMap.value?.fromScreenPoint(newCenterX, newCenterY.toInt())
//
//                    LogUtil.d_dev("MYTAG 음? ${half} / ${latLng!!.latitude},${latLng.longitude}")
//
//                    kakaoMap.value?.moveCamera(
//                        CameraUpdateFactory.newCenterPosition(LatLng.from(latLng!!.latitude, latLng.longitude))
//                    )
                    LogUtil.d_dev("MYTAG 현재 센터: ${kakaoMap.value?.cameraPosition?.position}")
                }) {

                }
            }

        }

        if(viewMode.value == BsViewMode.One) {
            FlexibleBottomSheet(
                onDismissRequest = {

                },
                sheetState = flexibleBottomSheetState,
                dragHandle = {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .onGloballyPositioned { coordinates ->
                                sheetContentHandleHeight.value =
                                    with(density) { coordinates.size.height }
                            }
                    ) {
                        Spacer(
                            modifier = Modifier
                                .width(50.dp)
                                .height(5.dp)
                                .background(
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(5.dp)
                                )
                        )
                    }
                },
                modifier = Modifier
                    .width(screenWidth.dp)
            ) {
                Column(
                    modifier = Modifier
                        .width(screenWidth.dp)
//                    .fillMaxHeight(sizeRatio)
                        .verticalScroll(contentScrollState)
                ) {
                    Text(text = "1", style = TextStyle(fontSize = 60.sp))
                    Text(text = "2", style = TextStyle(fontSize = 60.sp))
                    Text(text = "3", style = TextStyle(fontSize = 60.sp))
                    Text(text = "4", style = TextStyle(fontSize = 60.sp))
                    Text(text = "5", style = TextStyle(fontSize = 60.sp))
                    Text(text = "6", style = TextStyle(fontSize = 60.sp))
                    Text(text = "7", style = TextStyle(fontSize = 60.sp))
                    Text(text = "8", style = TextStyle(fontSize = 60.sp))
                    Text(text = "9", style = TextStyle(fontSize = 60.sp))
                    Text(text = "1", style = TextStyle(fontSize = 60.sp))
                    Text(text = "2", style = TextStyle(fontSize = 60.sp))
                    Text(text = "3", style = TextStyle(fontSize = 60.sp))
                    Text(text = "4", style = TextStyle(fontSize = 60.sp))
                    Text(text = "5", style = TextStyle(fontSize = 60.sp))
                    Text(text = "6", style = TextStyle(fontSize = 60.sp))
                    Text(text = "7", style = TextStyle(fontSize = 60.sp))
                    Text(text = "8", style = TextStyle(fontSize = 60.sp))
                    Text(text = "9", style = TextStyle(fontSize = 60.sp))
                }
            }
        } else if(viewMode.value == BsViewMode.Two) {
            FlexibleBottomSheet(
                onDismissRequest = {

                },
                sheetState = rememberFlexibleBottomSheetState(
                    isModal = false,
                    skipHiddenState = true,
                    skipSlightlyExpanded = true,
                    skipIntermediatelyExpanded = false,
                    flexibleSheetSize = FlexibleSheetSize(
//                        fullyExpanded = 1f,
                        intermediatelyExpanded = 0.4f,
//                        slightlyExpanded = 0.1f,
                    ),
                    allowNestedScroll = true,
                    containSystemBars = false,
                    confirmValueChange = { flexibleSheetValue ->
                        if(flexibleSheetValue == FlexibleSheetValue.FullyExpanded) {

                        }
                        true
                    }
                ),
                dragHandle = {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .onGloballyPositioned { coordinates ->
                                sheetContentHandleHeight.value =
                                    with(density) { coordinates.size.height }
                            }
                    ) {
                        Spacer(
                            modifier = Modifier
                                .width(50.dp)
                                .height(5.dp)
                                .background(
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(5.dp)
                                )
                        )
                    }
                },
                modifier = Modifier
                    .width(screenWidth.dp)
            ) {
                Column(
                    modifier = Modifier
                        .width(screenWidth.dp)
//                    .fillMaxHeight(sizeRatio)
                        .verticalScroll(contentScrollState)
                ) {
                    Text(text = "1", style = TextStyle(fontSize = 60.sp))
                }
            }
        }
    }

    val betweenCenterAndHalf : MutableState<Double> = rememberSaveable {
        mutableStateOf(0.0)
    }
    val betweenCenterAndCollapse : MutableState<Double> = rememberSaveable {
        mutableStateOf(0.0)
    }
    val calLatLngHalf: MutableState<LatLng?> = rememberSaveable {
        mutableStateOf(null)
    }
    kakaoMap.value?.setOnCameraMoveEndListener { kakaoMap, cameraPosition, gestureType ->
        val viewport: Rect = kakaoMap.viewport

        LogUtil.d_dev("현재 보이는 화면 - width: ${viewport.width()} height: ${viewport.height()}")

        val x = viewport.width() / 2
        val y = viewport.height() / 2


        // fromScreenPoint() 를 이용하여 스크린 좌표를 지리적 좌표로 변환할 수 있습니다.
        val position: LatLng = kakaoMap.fromScreenPoint(x, y)!!
        LogUtil.d_dev("현재 보이는 화면 - x: ${x}, y: ${y}")
        LogUtil.d_dev("현재 보이는 화면 - Center: ${cameraPosition.position.latitude} / ${cameraPosition.position.longitude}")
        LogUtil.d_dev("현재 보이는 화면 - position latlng: ${position.latitude} / ${position.longitude}")

        val top = viewport.top
        val bottom = viewport.bottom
        val left = viewport.left
        val right = viewport.right
        LogUtil.d_dev("현재 보이는 화면 - ${viewport.top} ${viewport.bottom} ${viewport.left} ${viewport.right}")

        val topLeft: LatLng = kakaoMap.fromScreenPoint(top, left)!!
        val topRight: LatLng = kakaoMap.fromScreenPoint(right, left)!!
        val bottomLeft: LatLng = kakaoMap.fromScreenPoint(top, bottom)!!
        val bottomRight: LatLng = kakaoMap.fromScreenPoint(right, bottom)!!

        LogUtil.i_dev("현재 보이는 화면 - TopLeft: ${topLeft.latitude}, ${topLeft.longitude}")
        LogUtil.i_dev("현재 보이는 화면 - TopRight: ${topRight.latitude}, ${topRight.longitude}")
        LogUtil.i_dev("현재 보이는 화면 - BottomLeft: ${bottomLeft.latitude}, ${bottomLeft.longitude}")
        LogUtil.i_dev("현재 보이는 화면 - BottomRight: ${bottomRight.latitude}, ${bottomRight.longitude}")

        val aHalf = right / 2
        val bHalf = bottom
        val abHalf = kakaoMap.fromScreenPoint(aHalf, bHalf)

        try {
            betweenCenterAndHalf.value = cameraPosition.position.latitude - kakaoMap.fromScreenPoint(aHalf, half.toInt())!!.latitude
            LogUtil.d_dev("betweenCenterAndHalf: ${betweenCenterAndHalf.value}")
            betweenCenterAndCollapse.value = cameraPosition.position.latitude - kakaoMap.fromScreenPoint(aHalf, collapse.toInt())!!.latitude
        } catch (e: Exception) {
            LogUtil.e_dev("betweenCenterAndHalf ${e}")
        }


        val cHalf = right / 2
        val dHalf = bottom / 4
        val eHalf = dHalf * 3
        val ceHalf = kakaoMap.fromScreenPoint(cHalf, eHalf)

        val latHalf = ceHalf!!.latitude - abHalf!!.latitude - betweenCenterAndHalf.value
        val lngHalf = ceHalf.longitude - abHalf.longitude
        calLatLngHalf.value = LatLng.from(latHalf, lngHalf)
        LogUtil.d_dev("Half 계산 ${latHalf}, ${lngHalf}")


        val a = kakaoMap.fromScreenPoint(right, bottom)
        val b = kakaoMap.fromScreenPoint(right, top)
        val c = b!!.latitude - a!!.latitude
//        val c = 0.01
        val d = c / ViewSizeUtil.dpToPx(localContext, screenHeight.toFloat())
        onePixelDistance = d
        LogUtil.i_dev("MYTAG d: ${d}\nc: ${c}\nb: ${b}\na: ${a}")

        currentCollapseLat = kakaoMap.fromScreenPoint(aHalf, collapse.toInt())!!.latitude
        currentHalfLat = kakaoMap.fromScreenPoint(aHalf, half.toInt())!!.latitude
        currentColHalfDistanceLat = currentHalfLat - currentCollapseLat
        LogUtil.d_dev("MYTAG currentCollapseLat: ${currentCollapseLat} currentHalfLat: ${currentHalfLat} currentColHalfDistanceLat: ${currentColHalfDistanceLat}")
    }

    kakaoMap.value?.setOnMapClickListener { kakaoMap, latLng, pointF, poi ->
        val calLat : Double = calLatLngHalf.value?.latitude?:0.0
        // 중앙과 높이 1/4지점 찍기
        // 중앙에서 미리 계산해둔 latitude만큼 빼기
        kakaoMap.moveCamera(
            CameraUpdateFactory.newCenterPosition(LatLng.from(latLng.latitude - calLat, latLng.longitude))
        )
    }

//    LogUtil.d_dev("MYTAG ${kakaoMap.value?.viewport?.width()} / ${kakaoMap.value?.viewport?.height()} / ${kakaoMap.value?.viewport?.width()?.dp} ${kakaoMap.value?.viewport?.height()?.dp}")
    if(kakaoMap.value?.viewport?.width() != null && kakaoMap.value?.viewport?.height() != null) {
        val centerX = kakaoMap.value?.viewport?.width()!! / 2
        val centerY = kakaoMap.value?.viewport?.height()!! / 2
        kakaoMap.value?.fromScreenPoint(centerX, centerY)

//        LogUtil.d_dev("MYTAG ${kakaoMap.value?.viewport?.width()?.dp!! / 2} ${kakaoMap.value?.viewport?.height()?.dp!! / 2}")
//        LogUtil.d_dev("MYTAG ${realOffsetY.value}")

//        val newY = kakaoMap.value?.viewport?.height()!! / 2 - realOffsetY.value
//
//        kakaoMap.value?.moveCamera(
//            CameraUpdateFactory.newCenterPosition(
//                LatLng.from((kakaoMap.value?.viewport?.width()!! / 2).toDouble(), newY.toDouble()), 16
//            )
//        )

    }

/**
 * 좀 더 봐야함
 * */
//    val mapHeight = rememberSaveable {
//        mutableStateOf(0)
//    }
//
//    var bsContentHeight : MutableState<Dp> = remember {
//        mutableStateOf(screenHeight.dp / 2)
//    }
//
//    val sizeRatio by remember{mutableStateOf(1f)} // 바텀시트 최대 사이즈 (0.0 ~ 1.0)
//
//    val currentSheetValue = remember {
//        mutableStateOf(SheetValue.Hidden)
//    }
//
//    // 바텀시트 초기화
//    val standardBottomSheetState = rememberStandardBottomSheetState(
//        skipHiddenState = false,
//        initialValue = SheetValue.PartiallyExpanded,
//        confirmValueChange = { sheetValue ->
//            currentSheetValue.value = sheetValue
//            LogUtil.d_dev("SheetValue: ${sheetValue}")
//            true
//        }
//    )
//
//    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
//        standardBottomSheetState
////        rememberStandardBottomSheetStateFix()
//    )
//
//    var value = rememberSaveable {
//        mutableStateOf((ViewSizeUtil.dpToPx(localContext, screenHeight.toFloat()) / 1.5).toFloat())
//    }
//    try {
//        value.value = bottomSheetScaffoldState.bottomSheetState.requireOffset()
////        LogUtil.d_dev("${bottomSheetScaffoldState.bottomSheetState.requireOffset()} / ${ViewSizeUtil.dpToPx(localContext, screenHeight.toFloat())} / ${(screenHeight.dp / 6)} / ${(screenHeight.dp / 3)}")
//    } catch (e: Exception) {
//
//    }
//
//    val newHeight = when {
//        value.value < (ViewSizeUtil.dpToPx(localContext, screenHeight.toFloat()) / 4) -> {
//            screenHeight.dp
//        }
//        value.value < (ViewSizeUtil.dpToPx(localContext, screenHeight.toFloat()) / 1.5) -> {
//            screenHeight.dp / 3
//        }
//        else -> {
//            screenHeight.dp / 6
//        }
//    }
//
//    if (bsContentHeight.value != newHeight) {
//        bsContentHeight.value = newHeight
//    }
//
////    LaunchedEffect(key1 = currentSheetValue.value) {
//        if(currentSheetValue.value == SheetValue.Hidden) {
//            scope.launch {
//                bottomSheetScaffoldState.bottomSheetState.show()
//                bottomSheetScaffoldState.bottomSheetState.partialExpand()
//            }
//        }
////    }
//
//    scope.launch {
//        bottomSheetScaffoldState.bottomSheetState.partialExpand()
//    }
//
//    BottomSheetScaffold(
//        modifier = Modifier
//            .fillMaxSize()
//            .onGloballyPositioned { coordinates ->
//                mapHeight.value = with(density) { coordinates.size.height }
//            },
//        sheetContent = {
//            Column(
//                modifier = Modifier
//                    .width(screenWidth.dp)
//                    .fillMaxHeight(sizeRatio)
//                    .verticalScroll(contentScrollState)
//            ) {
//                Text(text = "1", style = TextStyle(fontSize = 60.sp))
//                Text(text = "2", style = TextStyle(fontSize = 60.sp))
//                Text(text = "3", style = TextStyle(fontSize = 60.sp))
//                Text(text = "4", style = TextStyle(fontSize = 60.sp))
//                Text(text = "5", style = TextStyle(fontSize = 60.sp))
//                Text(text = "6", style = TextStyle(fontSize = 60.sp))
//                Text(text = "7", style = TextStyle(fontSize = 60.sp))
//                Text(text = "8", style = TextStyle(fontSize = 60.sp))
//                Text(text = "9", style = TextStyle(fontSize = 60.sp))
//                Text(text = "1", style = TextStyle(fontSize = 60.sp))
//                Text(text = "2", style = TextStyle(fontSize = 60.sp))
//                Text(text = "3", style = TextStyle(fontSize = 60.sp))
//                Text(text = "4", style = TextStyle(fontSize = 60.sp))
//                Text(text = "5", style = TextStyle(fontSize = 60.sp))
//                Text(text = "6", style = TextStyle(fontSize = 60.sp))
//                Text(text = "7", style = TextStyle(fontSize = 60.sp))
//                Text(text = "8", style = TextStyle(fontSize = 60.sp))
//                Text(text = "9", style = TextStyle(fontSize = 60.sp))
//            }
//        },
//        scaffoldState = bottomSheetScaffoldState,
//        sheetPeekHeight = bsContentHeight.value,
//        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
//        sheetContainerColor = Color.White,
//        sheetShadowElevation = 10.dp,
//        sheetSwipeEnabled = true,
//        sheetDragHandle = {
//            Row(
//                modifier = Modifier
//                    .padding(top = 10.dp, bottom = 10.dp)
//                    .onGloballyPositioned { coordinates ->
//                        sheetContentHandleHeight.value = with(density) { coordinates.size.height }
//                    }
//            ) {
//                Spacer(
//                    modifier = Modifier
//                        .width(50.dp)
//                        .height(5.dp)
//                        .background(
//                            color = Color.Gray,
//                            shape = RoundedCornerShape(5.dp)
//                        )
//                )
//            }
//        },
//        sheetMaxWidth = screenWidth.dp,
//        sheetContentColor = Color.Gray,
//        content = {
//            Box(
//                modifier = Modifier.fillMaxSize()
//            ) {
//
//                AndroidView(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    factory = { context ->
//                        mapView!!
//                    }
//                )
//
//                Button(onClick = { moveToFirst.invoke() }) {
//                    Text(text = "Button")
//                }
//            }
//        }
//    )
}

fun calculateSlideOffset(currentY: Float, maxHeight: Float): Float {
    return 1.0f - (currentY / maxHeight)
}

enum class BsViewMode {
    One, Two
}

@Preview(showBackground = true)
@Composable
fun ThirdMapBottomSheetScreen() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberStandardBottomSheetStateFix(
    initialValue: SheetValue = SheetValue.PartiallyExpanded,
    confirmValueChange: (SheetValue) -> Boolean = { true },
    skipHiddenState: Boolean = true
): SheetState {
    val skipPartiallyExpanded = false
    return rememberSaveable(
        skipPartiallyExpanded,
        confirmValueChange,
        saver = sheetStateSaver(
            skipPartiallyExpanded = skipPartiallyExpanded,
            confirmValueChange = confirmValueChange,
            skipHiddenState = skipHiddenState
        )
    ) {
        SheetState(skipPartiallyExpanded, initialValue, confirmValueChange, skipHiddenState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun sheetStateSaver(
    skipPartiallyExpanded: Boolean,
    confirmValueChange: (SheetValue) -> Boolean,
    skipHiddenState: Boolean = false
) = Saver<SheetState, SheetValue>(
    save = { it.currentValue },
    restore = { savedValue ->
        SheetState(skipPartiallyExpanded, savedValue, confirmValueChange, skipHiddenState)
    }
)


enum class ExpandedType {
    HALF, FULL, COLLAPSED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    var expandedType by remember {
        mutableStateOf(ExpandedType.COLLAPSED)
    }
    val height by animateIntAsState(
        when (expandedType) {
            ExpandedType.HALF -> screenHeight / 2
            ExpandedType.FULL -> screenHeight
            ExpandedType.COLLAPSED -> 70
        }
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(),
//        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
//        sheetElevation = 8.dp,
        sheetShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        sheetContent = {
            var isUpdated = false
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(height.dp)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                if (!isUpdated) {
                                    expandedType = when {
                                        dragAmount < 0 && expandedType == ExpandedType.COLLAPSED -> {
                                            ExpandedType.HALF
                                        }

                                        dragAmount < 0 && expandedType == ExpandedType.HALF -> {
                                            ExpandedType.FULL
                                        }

                                        dragAmount > 0 && expandedType == ExpandedType.FULL -> {
                                            ExpandedType.HALF
                                        }

                                        dragAmount > 0 && expandedType == ExpandedType.HALF -> {
                                            ExpandedType.COLLAPSED
                                        }

                                        else -> {
                                            ExpandedType.FULL
                                        }
                                    }
                                    isUpdated = true
                                }
                            },
                            onDragEnd = {
                                isUpdated = false
                            }
                        )
                    }
                    .background(Color.Red)
            )
        },
        sheetPeekHeight = height.dp,
        sheetDragHandle = {

        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        )
    }
}