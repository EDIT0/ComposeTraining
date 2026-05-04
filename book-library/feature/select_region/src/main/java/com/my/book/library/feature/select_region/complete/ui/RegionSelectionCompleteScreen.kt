package com.my.book.library.feature.select_region.complete.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.CommonActionBar
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.resource.Black
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.core.resource.R
import com.my.book.library.feature.select_region.complete.intent.RegionSelectionCompleteViewModelEvent
import com.my.book.library.feature.select_region.complete.state.RegionSelectionCompleteUiState
import com.my.book.library.feature.select_region.complete.viewmodel.RegionSelectionCompleteViewModel

@Composable
fun RegionSelectionCompleteScreen(
    commonViewModel: CommonViewModel,
    onBackPressed: () -> Unit,
    onMoveToMain: () -> Unit,
    modifier: Modifier,
    detailRegion: LibraryData.DetailRegion
) {

    LogUtil.d_dev("받은 데이터: ${detailRegion}")
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val commonViewModel = commonViewModel
    val regionSelectionCompleteViewModel = hiltViewModel<RegionSelectionCompleteViewModel>()

    val regionSelectionCompleteUiState = regionSelectionCompleteViewModel.regionSelectionCompleteUiState.collectAsStateWithLifecycle()

    // DetailRegion 저장 - detailRegion 가 변경될 때만 실행
    LaunchedEffect(detailRegion) {
        LogUtil.d_dev("LaunchedEffect 실행 - DetailRegion 저장: $detailRegion")
        regionSelectionCompleteViewModel.intentAction(
            RegionSelectionCompleteViewModelEvent.SetDetailRegion(detailRegion = detailRegion)
        )
    }

    LaunchedEffect(key1 = true) {
        regionSelectionCompleteViewModel.sideEffectEvent.collect {
            when(it) {
                is RegionSelectionCompleteViewModel.SideEffectEvent.ShowToast -> {
                    Toast.makeText(localContext, it.message, Toast.LENGTH_SHORT).show()
                }
                is RegionSelectionCompleteViewModel.SideEffectEvent.OnSuccessRegistration -> {
                    onMoveToMain.invoke()
                }
            }
        }
    }

    RegionSelectionCompleteContent(
        localContext = localContext,
        onMoveToMain = onMoveToMain,
        onBackPressed = {
            onBackPressed.invoke()
        },
        modifier = Modifier,
        regionSelectionCompleteViewModelEvent = {
            regionSelectionCompleteViewModel.intentAction(regionSelectionCompleteViewModelEvent = it)
        },
        regionSelectionCompleteUiState = regionSelectionCompleteUiState
    )

}

@Composable
fun RegionSelectionCompleteContent(
    localContext: Context,
    onMoveToMain: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    regionSelectionCompleteViewModelEvent: (RegionSelectionCompleteViewModelEvent) -> Unit,
    regionSelectionCompleteUiState: State<RegionSelectionCompleteUiState>
) {

    SystemBarController.Setup(
        config = SystemBarConfig(
            statusBarColor = Black,
            statusBarDarkIcons = false,
            useStatusBarSpace = true,
            navigationBarColor = Black,
            navigationBarDarkIcons = false,
            useNavigationBarSpace = true
        )
    ) { state ->
        Scaffold(
            modifier = Modifier
                .padding(top = state.statusBarHeight, bottom = state.navigationBarHeight)
                .consumeWindowInsets(WindowInsets.statusBars)
                .consumeWindowInsets(WindowInsets.navigationBars)
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CommonActionBar(
                        context = localContext,
                        modifier = Modifier,
                        actionBarTitle = localContext.getString(R.string.select_library_region_title),
                        isShowBackButton = false,
                        onBackClick = {
                            onBackPressed.invoke()
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        val region = if(regionSelectionCompleteUiState.value.detailRegion?.regionNameRes != null) {
                            stringResource(regionSelectionCompleteUiState.value.detailRegion!!.regionNameRes)
                        } else {
                            ""
                        }
                        val detailRegion = if(regionSelectionCompleteUiState.value.detailRegion?.districtNameRes != null) {
                            stringResource(regionSelectionCompleteUiState.value.detailRegion!!.districtNameRes)
                        } else {
                            ""
                        }

                        Text("${region} ${detailRegion}")
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .noRippleClickable {
                                regionSelectionCompleteViewModelEvent.invoke(
                                    RegionSelectionCompleteViewModelEvent.RegisterRegionAndLibrary
                                )
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "책 찾으러 가기",
                            modifier = Modifier
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .noRippleClickable {
                                onBackPressed.invoke()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "다른 지역 선택",
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun RegionSelectionCompleteUIPreview() {
    RegionSelectionCompleteContent(
        localContext = LocalContext.current,
        onMoveToMain = {},
        onBackPressed = {},
        modifier = Modifier,
        regionSelectionCompleteViewModelEvent = {},
        regionSelectionCompleteUiState = remember {
            mutableStateOf(RegionSelectionCompleteUiState())
        }
    )
}