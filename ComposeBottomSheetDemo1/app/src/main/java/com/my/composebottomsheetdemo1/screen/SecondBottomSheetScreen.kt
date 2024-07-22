package com.my.composebottomsheetdemo1.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.navigation.NavController
import com.my.composebottomsheetdemo1.LogUtil
import kotlinx.coroutines.launch

@Composable
fun SecondBottomSheetScreen(
    navController: NavController
) {
    SecondBottomSheetScreenUI()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondBottomSheetScreenUI() {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    val scope = rememberCoroutineScope()

    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { sheetValue ->
            LogUtil.d_dev("confirmValueChange: ${sheetValue}")
            true // false 일 경우, BottomSheet 상태 변경이 되지 않음
        }
    )

    val isShowBottomSheet = rememberSaveable {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState()

    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        if(isShowBottomSheet.value) {
            ModalBottomSheet(
                modifier = Modifier
                    // 전체 높이 설정
                    .fillMaxHeight(fraction = 0.45f),
                onDismissRequest = {
                    isShowBottomSheet.value = false
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                    LogUtil.d_dev("바텀시트 종료")
                },
                sheetState = modalBottomSheetState,
                shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp),
                sheetMaxWidth = screenWidth.dp,
                containerColor = Color.Blue,
                contentColor = Color.Cyan,
                tonalElevation = 10.dp,
//                scrimColor = Color.Red,
                dragHandle = {
                    // 직접 View 만들어도 된다.
                    BottomSheetDefaults.DragHandle(
                        color = Color.Yellow,
                        height = 20.dp,
                        width = 100.dp
                    )
                },
                properties = ModalBottomSheetProperties(
                    isFocusable = true,
                    securePolicy = SecureFlagPolicy.SecureOn,
                    shouldDismissOnBackPress = false
                ),
//            windowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
            ) {
                // Content
                val bottomSheetContentScrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(screenHeight * 0.3f)
                        .padding(start = 20.dp, end = 20.dp)
                        .verticalScroll(bottomSheetContentScrollState)
                ) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                scope.launch {
                                    modalBottomSheetState.hide()
                                }
                                isShowBottomSheet.value = false
                            },
                        text = "1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1",
                        style = TextStyle(fontSize = 50.sp)
                    )
                }
            }
        } else {

        }

        Button(
            onClick = {
                LogUtil.d_dev("클릭")
                scope.launch {
                    modalBottomSheetState.show()
                }
                isShowBottomSheet.value = true
            }
        ) {
            Text(text = "Open")
        }

        Text(text = "아아아아아아아아", style = TextStyle(fontSize = 100.sp))
        Text(text = "아아아아아아아아", style = TextStyle(fontSize = 100.sp))
        Text(text = "아아아아아아아아", style = TextStyle(fontSize = 100.sp))
        Text(text = "아아아아아아아아", style = TextStyle(fontSize = 100.sp))
        Text(text = "아아아아아아아아", style = TextStyle(fontSize = 100.sp))
        Text(text = "아아아아아아아아", style = TextStyle(fontSize = 100.sp))
        Text(text = "아아아아아아아아", style = TextStyle(fontSize = 100.sp))
        Text(text = "아아아아아아아아", style = TextStyle(fontSize = 100.sp))
        Text(text = "아아아아아아아아", style = TextStyle(fontSize = 100.sp))
    }

    LogUtil.d_dev("1. isShowBottomSheet: ${isShowBottomSheet.value}")
    LogUtil.d_dev(
        "2. currentValue: ${modalBottomSheetState.currentValue}\n" +
                "3. targetValue: ${modalBottomSheetState.targetValue}\n" +
                "4. hasExpandedState: ${modalBottomSheetState.hasExpandedState}\n" +
                "5. hasPartiallyExpandedState: ${modalBottomSheetState.hasPartiallyExpandedState}\n" +
                "6. isVisible: ${modalBottomSheetState.isVisible}"
    )
}

@Preview(showBackground = true)
@Composable
fun SecondBottomSheetScreen() {
    SecondBottomSheetScreenUI()
}