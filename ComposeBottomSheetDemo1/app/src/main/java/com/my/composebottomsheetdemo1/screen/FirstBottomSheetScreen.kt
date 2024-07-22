package com.my.composebottomsheetdemo1.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.my.composebottomsheetdemo1.LogUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun FirstBottomSheetScreen(
    navController: NavController
) {

    FirstBottomSheetScreenUI()

}

@Composable
fun FirstBottomSheetScreenUI(

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val state = remember {
            mutableStateOf(false)
        }
        Button(onClick = { state.value = !state.value}) {
            Text(text = "버튼")
        }
        if(state.value) {
            PersistentBottomSheet()
        } else {
            Persistent2BottomSheet()
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersistentBottomSheet(

) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp.dp
    LogUtil.d_dev("Screen Width: ${screenWidth} Height: ${screenHeight}")

    val scrollState = rememberScrollState()
    val bottomSheetContentScrollState = rememberScrollState()

    val scope = rememberCoroutineScope()
//    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
//    val bottomSheetState = bottomSheetScaffoldState.bottomSheetState

    var sizeRatio by remember{mutableStateOf(0.9f)}

//    val direction: SheetValue = bottomSheetState.direction
//    val currentValue: SheetValue = bottomSheetState.currentValue
//    val targetValue: SheetValue = bottomSheetState.targetValue
//    val overflow = bottomSheetState.overflow.value
//    val offset = bottomSheetState.requireOffset()

//    val progress = bottomSheetState.progress
//    val fraction = progress.fraction
//    val from = progress.from.name
//    val to = progress.to.name

    val bottomSheetSt = rememberStandardBottomSheetState(
        skipHiddenState = true,
        initialValue = SheetValue.PartiallyExpanded
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetSt)

    val sheetContentHeight = remember {
        mutableStateOf(0.dp)
    }

    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize(),
        sheetContent = {
            Column(
                modifier = Modifier
                    .width(screenWidth)
                    .fillMaxHeight(sizeRatio)
                    .verticalScroll(bottomSheetContentScrollState)
            ) {
                Text("Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet", style = TextStyle(fontSize = 50.sp),
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            sheetContentHeight.value = with(density) { coordinates.size.height.toDp() + bottomPadding }
                        }
                )

//                Text("Swipe up to expand sheet\n" +
//                        "Swipe up to expand sheet\n" +
//                        "Swipe up to expand sheet\n" +
//                        "Swipe up to expand sheet\n" +
//                        "Swipe up to expand sheet\n" +
//                        "Swipe up to expand sheet\n" +
//                        "Swipe up to expand sheet\n" +
//                        "Swipe up to expand sheet\n" +
//                        "Swipe up to expand sheet\n" +
//                        "Swipe up to expand sheet", style = TextStyle(fontSize = 100.sp))
            }

//            Text(modifier = Modifier
//                .fillMaxWidth()
//                .height(
//                    WindowInsets.navigationBars
//                        .asPaddingValues()
//                        .calculateBottomPadding()
//                )
//                .background(Color.Red),
//                text = "a"
//            )
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetContentHeight.value, // 컨텐츠 높이 만큼 지정하려면 sheetContentHeight.value,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetContainerColor = Color.White,
        sheetShadowElevation = 10.dp,
        sheetSwipeEnabled = true,
        sheetDragHandle = {
            Row(
                modifier = Modifier
                    .padding(5.dp)
            ) {
                Icon(
                    imageVector = if(bottomSheetSt.currentValue == SheetValue.PartiallyExpanded) {
                        Icons.Filled.KeyboardArrowUp
                    } else {
                        Icons.Filled.KeyboardArrowDown
                    },
                    tint = Color.Black,
                    contentDescription = ""
                )
            }
        },
        sheetMaxWidth = screenWidth,
        sheetContentColor = Color.Gray,
        content = {
            // BottomSheet 외의 화면
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan)
                    .verticalScroll(scrollState)
            ) {
                Text("This is main ui")
                Text(
                    text = "CurrentValue: ${bottomSheetSt.currentValue}\n" +
                            "TargetValue: ${bottomSheetSt.targetValue}\n",
                    style = TextStyle(fontSize = 30.sp)
                )
                Button(
                    onClick = {
                        scope.launch { bottomSheetSt.partialExpand() }
                    }
                ) {
                    Text("collapse sheet")
                }

                Button(
                    onClick = {
                        scope.launch { bottomSheetSt.expand() }
                    }
                ) {
                    Text("expand sheet")
                }
            }
        }

//    sheetTonalElevation: Dp = BottomSheetDefaults.Elevation,
//    topBar: @Composable() (() -> Unit)? = null,
//    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
//    containerColor: Color = MaterialTheme.colorScheme. surface,
//    contentColor: Color = contentColorFor(containerColor),
    )

    LogUtil.d_dev("BottomSheet State: ${bottomSheetSt.currentValue}")
}


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Persistent2BottomSheet(

) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp.dp
    LogUtil.d_dev("Screen Width: ${screenWidth} Height: ${screenHeight}")

    val scrollState = rememberScrollState()
    val bottomSheetContentScrollState = rememberScrollState()

    val scope = rememberCoroutineScope()
//    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
//    val bottomSheetState = bottomSheetScaffoldState.bottomSheetState

    var sizeRatio by remember{mutableStateOf(0.9f)}

//    val direction: SheetValue = bottomSheetState.direction
//    val currentValue: SheetValue = bottomSheetState.currentValue
//    val targetValue: SheetValue = bottomSheetState.targetValue
//    val overflow = bottomSheetState.overflow.value
//    val offset = bottomSheetState.requireOffset()

//    val progress = bottomSheetState.progress
//    val fraction = progress.fraction
//    val from = progress.from.name
//    val to = progress.to.name

    val bottomSheetSt = rememberStandardBottomSheetState(
        skipHiddenState = true,
        initialValue = SheetValue.PartiallyExpanded
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetSt)

    val sheetContentHeight = remember {
        mutableStateOf(0.dp)
    }

    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize(),
        sheetContent = {
            Column(
                modifier = Modifier
                    .width(screenWidth)
                    .fillMaxHeight(sizeRatio)
                    .verticalScroll(bottomSheetContentScrollState)
            ) {
                Text("Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet\n" +
                        "Swipe up to expand sheet", style = TextStyle(fontSize = 50.sp))
            }

//            Text(modifier = Modifier
//                .fillMaxWidth()
//                .height(
//                    WindowInsets.navigationBars
//                        .asPaddingValues()
//                        .calculateBottomPadding()
//                )
//                .background(Color.Red),
//                text = "a"
//            )
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 50.dp, // 컨텐츠 높이 만큼 지정하려면 sheetContentHeight.value,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetContainerColor = Color.White,
        sheetShadowElevation = 10.dp,
        sheetSwipeEnabled = true,
        sheetDragHandle = {
            Row(
                modifier = Modifier
                    .padding(5.dp)
            ) {
                Icon(
                    imageVector = if(bottomSheetSt.currentValue == SheetValue.PartiallyExpanded) {
                        Icons.Filled.KeyboardArrowUp
                    } else {
                        Icons.Filled.KeyboardArrowDown
                    },
                    tint = Color.Black,
                    contentDescription = ""
                )
            }
        },
        sheetMaxWidth = screenWidth,
        sheetContentColor = Color.Gray,
        content = {
            // BottomSheet 외의 화면
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan)
                    .verticalScroll(scrollState)
            ) {
                Text("This is main ui")
                Text(
                    text = "CurrentValue: ${bottomSheetSt.currentValue}\n" +
                            "TargetValue: ${bottomSheetSt.targetValue}\n",
                    style = TextStyle(fontSize = 30.sp)
                )
                Button(
                    onClick = {
                        scope.launch { bottomSheetSt.partialExpand() }
                    }
                ) {
                    Text("collapse sheet")
                }

                Button(
                    onClick = {
                        scope.launch { bottomSheetSt.expand() }
                    }
                ) {
                    Text("expand sheet")
                }
            }
        }

//    sheetTonalElevation: Dp = BottomSheetDefaults.Elevation,
//    topBar: @Composable() (() -> Unit)? = null,
//    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
//    containerColor: Color = MaterialTheme.colorScheme. surface,
//    contentColor: Color = contentColorFor(containerColor),
    )

    LogUtil.d_dev("BottomSheet State: ${bottomSheetSt.currentValue}")
}


@Preview(showBackground = true)
@Composable
fun PreviewFirstBottomSheetScreen() {
    FirstBottomSheetScreenUI(

    )
}