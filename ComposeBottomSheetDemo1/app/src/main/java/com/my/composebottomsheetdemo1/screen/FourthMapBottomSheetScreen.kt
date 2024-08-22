package com.my.composebottomsheetdemo1.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
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
import kotlinx.coroutines.CoroutineScope

@Composable
fun FourthMapBottomSheetScreen(
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
            FourthMapBottomSheetScreenUI(
                localContext = localContext,
                scope = scope,
                kakaoMap = kakaoMap,
                moveToFirst = {
                    navController.navigate(route = NavigationScreenName.SecondBottomSheetScreen.name)
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FourthMapBottomSheetScreenUI(
    localContext: Context,
    scope: CoroutineScope,
    kakaoMap: MutableState<KakaoMap?>,
    moveToFirst: () -> Unit
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp // 스크린 높이
    val screenWidth = configuration.screenWidthDp // 스크린 너비

    val mapView = KakaoMapUtil.setKakaoMap(
        localContext,
        NavigationScreenName.FourthMapBottomSheetScreen.name,
        getKakaoMap = {
            kakaoMap.value = it
        },
        getMapViewInfo = {

        }
    )

    // 바텀시트 초기화
    val standardBottomSheetState = rememberStandardBottomSheetState(
        skipHiddenState = false,
        initialValue = SheetValue.PartiallyExpanded,
        confirmValueChange = { sheetValue ->
            LogUtil.d_dev("SheetValue: ${sheetValue}")
            true
        }
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        standardBottomSheetState
//        rememberStandardBottomSheetStateFix()
    )

    val isShowBottomSheet = rememberSaveable {
        mutableStateOf(false)
    }

    val sizeRatio by remember{mutableStateOf(1f)} // 바텀시트 최대 사이즈 (0.0 ~ 1.0)

    var handleHeight by rememberSaveable {
        mutableStateOf(0)
    }
    val contentHeight = rememberSaveable {
        mutableStateOf(0)
    }
    val sheetBottomHeight : Int = if(isShowBottomSheet.value) {
        ViewSizeUtil.pxToDp(localContext, (contentHeight.value + handleHeight + 20).toFloat()).toInt()
    } else {
        0
    }

    var screenHeightOffset by rememberSaveable {
        mutableStateOf(0f)
    }
    var movingRangeOffset by rememberSaveable {
        mutableStateOf(0f)
    }

    try {
        screenHeightOffset = 1.0f - (bottomSheetScaffoldState.bottomSheetState.requireOffset() / ViewSizeUtil.dpToPx(localContext, screenHeight.toFloat()).toFloat())
    } catch (e: Exception) {

    }

    LaunchedEffect(key1 = screenHeightOffset) {
        if (screenHeightOffset > 0.1 && screenHeightOffset < 0.4) {
            val normalizedOffset = (screenHeightOffset - 0.1) / (0.4 - 0.1)
            movingRangeOffset = (normalizedOffset * contentHeight.value).toFloat()
        }
    }

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize(),
        sheetContent = {
            Column(
                modifier = Modifier
                    .width(screenWidth.dp)
                    .fillMaxHeight(sizeRatio)
            ) {
                Content(
                    density = density,
                    screenWidth = screenWidth,
                    contentHeight = contentHeight
                )
            }
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = sheetBottomHeight.dp,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetContainerColor = Color.White,
        sheetShadowElevation = 10.dp,
        sheetSwipeEnabled = true,
        sheetDragHandle = {
            Row(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .onGloballyPositioned { coordinates ->
                        handleHeight = with(density) { coordinates.size.height }
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
        sheetMaxWidth = screenWidth.dp,
        sheetContentColor = Color.Gray,
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = -ViewSizeUtil.pxToDp(localContext, movingRangeOffset.toFloat()).dp),
                    factory = { context ->
                        mapView!!
                    }
                )

                Row {
                    Button(onClick = { moveToFirst.invoke() }) {
                        Text(text = "Button")
                    }
                    Button(onClick = { isShowBottomSheet.value = !isShowBottomSheet.value }) {
                        Text(text = "BS Toggle")
                    }
                }

            }
        }
    )
}

@Composable
fun Content(
    density: Density,
    screenWidth: Int,
    contentHeight: MutableState<Int>
) {
    Column(
        modifier = Modifier
            .width(screenWidth.dp)
            .onGloballyPositioned { coordinates ->
                contentHeight.value = with(density) { coordinates.size.height }
            }
    ) {
        Text(text = "1", style = TextStyle(fontSize = 60.sp))
        Text(text = "2", style = TextStyle(fontSize = 60.sp))
        Text(text = "3", style = TextStyle(fontSize = 60.sp))
    }
}