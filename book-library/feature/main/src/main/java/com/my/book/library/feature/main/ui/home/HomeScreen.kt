package com.my.book.library.feature.main.ui.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.CommonSearchBar
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.resource.NotoSansKR
import com.my.book.library.core.resource.R
import com.my.book.library.feature.main.state.home.HomeUiState
import com.my.book.library.feature.main.viewmodel.MainViewModel
import com.my.book.library.feature.main.viewmodel.home.HomeViewModel

@Composable
fun HomeScreen(
    onMoveToSearchLibrary: () -> Unit,
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
            override fun onResume() {}
            override fun onPause() {}
            override fun onStop() {}
            override fun onDispose() {}
        }
    }

    HomeContent(
        localContext = localContext,
        onMoveToSearchLibrary = onMoveToSearchLibrary,
        homeUiState = homeUiState,
    )

    LifecycleListener(
        lifecycleOwner = lifecycleOwner,
        screenName = object {}.javaClass.enclosingClass?.simpleName ?: "HomeScreen",
        lifecycleResult = lifecycleResult
    )
}

@Composable
fun HomeContent(
    localContext: Context,
    onMoveToSearchLibrary: () -> Unit,
    homeUiState: State<HomeUiState>
) {
    val scrollState = rememberScrollState()

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
                modifier = Modifier
                    .padding(innerPadding)
                    .background(color = colorResource(R.color.color_FFFFFFFF))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp)
                            .noRippleClickable {
                                // TODO 위치 변경으로 이동
                                LogUtil.d_dev("내 위치 클릭")
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_point_marker_blue_16x20),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = homeUiState.value.myLibraryInfo?.detailRegion?.let {
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

                    CommonSearchBar(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                            .fillMaxWidth(),
                        isFakeSearchBar = true,
                        backgroundColorRes = R.color.color_F2F4F6,
                        cornerRadius = 8.dp,
                        hint = stringResource(R.string.main_home_fake_search_hint),
                        hintColorRes = R.color.color_8B95A1,
                        textSize = 16.dp,
                        query = "",
                        focusRequester = remember { FocusRequester() },
                        showKeyboardOnStart = true,
                        onQueryChange = {},
                        onSearchClick = {},
                        onCancelClick = {},
                        onFakeBarClick = {
                            onMoveToSearchLibrary.invoke()
                        }
                    )

//            Column() {
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
//                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)), modifier = Modifier.clickable {
//                    if(testState.value == "TEST") {
//                        changeTestState.invoke("zzz")
//                    } else {
//                        changeTestState.invoke("TEST")
//                    }
//                })
//            }
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun HomeUIPreview() {
    HomeContent(
        localContext = LocalContext.current,
        onMoveToSearchLibrary = {},
        homeUiState = remember { mutableStateOf(HomeUiState()) },
    )
}