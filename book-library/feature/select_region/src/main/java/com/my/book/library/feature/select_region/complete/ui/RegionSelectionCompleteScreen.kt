package com.my.book.library.feature.select_region.complete.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.CommonActionBar
import com.my.book.library.core.common.component.CommonButton
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.resource.Black
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.core.resource.NotoSansKR
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

    LaunchedEffect(key1 = Unit) {
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

    val useStatusBarSpace = true
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
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val region = regionSelectionCompleteUiState.value.detailRegion?.let { region ->
                            stringResource(region.regionNameRes)
                        } ?: ""

                        val detailRegion = regionSelectionCompleteUiState.value.detailRegion?.let { region ->
                            stringResource(region.districtNameRes)
                        } ?: ""

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        shape = RoundedCornerShape(9999.dp),
                                        color = colorResource(R.color.color_E8F3FF)
                                    )
                                    .align(Alignment.BottomCenter)
                            ) { }
                            Image(
                                painter = painterResource(R.drawable.ic_check_blue_28x21),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(R.string.select_region_complete_set_region_title),
                            style = TextStyle(
                                color = colorResource(R.color.color_191F28),
                                fontSize = dpToSp(22.dp),
                                lineHeight = dpToSp(30.dp),
                                fontFamily = NotoSansKR,
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .background(
                                    shape = RoundedCornerShape(9999.dp),
                                    color = colorResource(R.color.color_F2F4F6)
                                )
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_point_marker_grey_11x14),
                                contentDescription = null
                            )
                            
                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "${region} ${detailRegion}",
                                style = TextStyle(
                                    color = colorResource(R.color.color_191F28),
                                    fontSize = dpToSp(14.dp),
                                    lineHeight = dpToSp(21.dp),
                                    fontFamily = NotoSansKR,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(R.string.select_region_complete_set_region_notice),
                            style = TextStyle(
                                color = colorResource(R.color.color_6B7684),
                                fontSize = dpToSp(16.dp),
                                lineHeight = dpToSp(24.dp),
                                fontFamily = NotoSansKR,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                    ) {
                        CommonButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 24.dp),
                            text = stringResource(R.string.select_region_complete_go_to_main_title),
                            textColorRes = R.color.color_FFFFFFFF,
                            backgroundColorRes =R.color.color_3182F6,
                            cornerRadius = 12.dp,
                            isBorderEnabled = false,
                            isEnabled = true,
                            textSize = 16.dp,
                            onClick = {
                                regionSelectionCompleteViewModelEvent.invoke(
                                    RegionSelectionCompleteViewModelEvent.RegisterRegionAndLibrary
                                )
                            }
                        )
                    }

//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(50.dp)
//                            .noRippleClickable {
//                                regionSelectionCompleteViewModelEvent.invoke(
//                                    RegionSelectionCompleteViewModelEvent.RegisterRegionAndLibrary
//                                )
//                            },
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//
//                        Text(
//                            text = "책 찾으러 가기",
//                            modifier = Modifier
//                        )
//                    }
//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(50.dp)
//                            .noRippleClickable {
//                                onBackPressed.invoke()
//                            },
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//
//                        Text(
//                            text = "다른 지역 선택",
//                            modifier = Modifier
//                        )
//                    }
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