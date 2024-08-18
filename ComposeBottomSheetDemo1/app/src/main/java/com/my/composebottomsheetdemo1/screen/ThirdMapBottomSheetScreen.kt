package com.my.composebottomsheetdemo1.screen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.kakao.vectormap.KakaoMap
import com.my.composebottomsheetdemo1.KakaoMapUtil
import com.my.composebottomsheetdemo1.LogUtil
import com.my.composebottomsheetdemo1.ViewSizeUtil
import com.my.composebottomsheetdemo1.navigation.NavigationScreenName
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.FlexibleSheetValue
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

    try {
//        LogUtil.d_dev("flexibleSheetSize: ${flexibleBottomSheetState.flexibleSheetSize}")
//        LogUtil.d_dev("currentValue: ${flexibleBottomSheetState.currentValue}")
        LogUtil.d_dev("progress: ${flexibleBottomSheetState.swipeableState.progress}")
//        LogUtil.d_dev("anchor: ${flexibleBottomSheetState.swipeableState.anchors}")

        val offset = flexibleBottomSheetState.requireOffset()
        val half = flexibleBottomSheetState.swipeableState.anchors[FlexibleSheetValue.IntermediatelyExpanded]!!
        val collapse = flexibleBottomSheetState.swipeableState.anchors[FlexibleSheetValue.SlightlyExpanded]!!
        val movingTotalHeight = collapse - half
        realMovingHeight.value = movingTotalHeight
        val offsetY = half - offset
        if(half <= offset && collapse >= offset) {
            LogUtil.d_dev("이때만 지도가 움직여야합니다. offsetY: ${offsetY}")
            realOffsetY.value = offsetY
        }
        LogUtil.d_dev("${ViewSizeUtil.pxToDp(localContext, realOffsetY.value).dp} / ${ViewSizeUtil.pxToDp(localContext, realMovingHeight.value).dp}")
        LogUtil.d_dev("Move 1: ${-ViewSizeUtil.pxToDp(localContext, realOffsetY.value).dp - ViewSizeUtil.pxToDp(localContext, realMovingHeight.value).dp}")
        LogUtil.d_dev("Move 2: ${ViewSizeUtil.pxToDp(localContext, realMovingHeight.value).dp + ViewSizeUtil.pxToDp(localContext, realOffsetY.value).dp}")
    } catch (e: Exception) {

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
                    .offset(y = -(ViewSizeUtil.pxToDp(localContext, realMovingHeight.value).dp + ViewSizeUtil.pxToDp(localContext, realOffsetY.value).dp)),
                factory = { context ->
                    mapView!!
                }
            )

            Button(onClick = { moveToFirst.invoke() }) {
                Text(text = "Button")
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

    kakaoMap.value?.setOnMapClickListener { kakaoMap, latLng, pointF, poi ->
        if(viewMode.value == BsViewMode.One) {
            viewMode.value = BsViewMode.Two
        } else if(viewMode.value == BsViewMode.Two) {
            viewMode.value = BsViewMode.One
        }
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