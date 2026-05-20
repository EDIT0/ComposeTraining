package com.my.book.library.feature.select_region.selection.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.core.resource.NotoSansKR
import com.my.book.library.core.resource.R
import com.my.book.library.feature.select_region.selection.intent.RegionSelectionViewModelEvent
import com.my.book.library.feature.select_region.selection.state.RegionSelectionUiState
import com.my.book.library.feature.select_region.selection.viewmodel.RegionSelectionViewModel

@Composable
fun RegionSelectionScreen(
    commonViewModel: CommonViewModel,
    onBackPressed: () -> Unit,
    onMoveToRegionSelectionComplete: (LibraryData.DetailRegion) -> Unit,
    modifier: Modifier
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val commonViewModel = commonViewModel
    val regionSelectionViewModel = hiltViewModel<RegionSelectionViewModel>()

    val regionSelectionUiState = regionSelectionViewModel.regionSelectionUiState.collectAsStateWithLifecycle()

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

    RegionSelectionContent(
        localContext = context,
        onMoveToRegionSelectionComplete = onMoveToRegionSelectionComplete,
        onBackPressed = {
            onBackPressed.invoke()
        },
        modifier = Modifier,
        regionSelectionViewModelEvent = {
            regionSelectionViewModel.intentAction(regionSelectionViewModelEvent = it)
        },
        regionSelectionUiState = regionSelectionUiState
    )

    LifecycleListener(
        lifecycleOwner = lifecycleOwner,
        screenName = object {}.javaClass.enclosingClass?.simpleName ?: "RegionSelectGuideScreen",
        lifecycleResult = lifecycleResult
    )

    BackHandler(
        enabled = true,
        onBack = {
            LogUtil.i_dev("${object {}.javaClass.enclosingClass?.simpleName} BackHandler")
            onBackPressed.invoke()
        }
    )
}

@Composable
fun RegionSelectionContent(
    localContext: Context,
    onMoveToRegionSelectionComplete: (LibraryData.DetailRegion) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    regionSelectionViewModelEvent: (RegionSelectionViewModelEvent) -> Unit,
    regionSelectionUiState: State<RegionSelectionUiState>
) {

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
                modifier = modifier
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(R.color.color_FFFFFFFF))
                ) {
                    CommonActionBar(
                        context = localContext,
                        modifier = Modifier,
                        actionBarTitle = localContext.getString(R.string.select_region_selection_title),
                        isShowBackButton = true,
                        onBackClick = {
                            onBackPressed.invoke()
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.select_region_selection_subtitle),
                            style = TextStyle(
                                color = colorResource(R.color.color_191F28),
                                fontSize = dpToSp(16.dp),
                                lineHeight = dpToSp(24.dp),
                                fontFamily = NotoSansKR,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            color = colorResource(R.color.color_E5E8EB)
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .width(128.dp)
                                .fillMaxHeight()
                                .background(
                                    color = colorResource(R.color.color_F9FAFB)
                                )
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                content = {
                                    itemsIndexed(
                                        items = LibraryData.regionList,
                                    ) { index, item ->
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(56.dp)
                                                .noRippleClickable {
                                                    regionSelectionViewModelEvent.invoke(
                                                        RegionSelectionViewModelEvent.SetSelectionRegion(item)
                                                    )
                                                    regionSelectionViewModelEvent.invoke(
                                                        RegionSelectionViewModelEvent.SetSelectionDetailRegion(detailRegion = null)
                                                    )
                                                }
                                                .padding(horizontal = 16.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Text(
                                                text = stringResource(id = item.nameRes),
                                                style = TextStyle(
                                                    color = colorResource(
                                                        if (item == regionSelectionUiState.value.region) {
                                                            R.color.color_3182F6
                                                        }
                                                        else {
                                                            R.color.color_6B7684
                                                        }
                                                    ),
                                                    fontSize = dpToSp(16.dp),
                                                    lineHeight = dpToSp(24.dp),
                                                    fontFamily = NotoSansKR,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            )
                                        }
                                    }
                                }
                            )
                        }

                        Column(
                            modifier = Modifier
                        ) {
                            Spacer(modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .background(
                                    color = colorResource(R.color.color_E5E8EB)
                                )
                            )
                        }

                        val filteredItems = LibraryData.allDetailRegions.filter { it -> it.regionCode == regionSelectionUiState.value.region?.code }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            if(regionSelectionUiState.value.region == null) {
                                Text(
                                    text = stringResource(R.string.select_region_selection_city_not_selected_notice),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .wrapContentSize(Alignment.Center),
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        lineHeight = dpToSp(25.dp),
                                        fontSize = dpToSp(16.dp)
                                    )
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .weight(1f),
                                    content = {
                                        itemsIndexed(
                                            items = filteredItems,
                                        ) { index, item ->
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(56.dp)
                                                    .noRippleClickable {
                                                        regionSelectionViewModelEvent.invoke(RegionSelectionViewModelEvent.SetSelectionDetailRegion(item))
                                                    }
                                                    .padding(horizontal = 20.dp, vertical = 16.dp),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.Start
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .fillMaxHeight(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = stringResource(id = item.districtNameRes),
                                                        style = TextStyle(
                                                            color = colorResource(
                                                                if (item == regionSelectionUiState.value.detailRegion) {
                                                                    R.color.color_3182F6
                                                                }
                                                                else {
                                                                    R.color.color_6B7684
                                                                }
                                                            ),
                                                            fontSize = dpToSp(16.dp),
                                                            lineHeight = dpToSp(24.dp),
                                                            fontFamily = NotoSansKR,
                                                            fontWeight = FontWeight.Medium,
                                                            textAlign = TextAlign.Start
                                                        ),
                                                        modifier = Modifier
                                                            .weight(1f)
                                                    )

                                                    if(item == regionSelectionUiState.value.detailRegion) {
                                                        Image(
                                                            painter = painterResource(R.drawable.ic_check_blue_17x13),
                                                            contentDescription = null,
                                                        )
                                                    }
                                                }
                                            }
                                            if (index < filteredItems.lastIndex) {
                                                Spacer(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(1.dp)
                                                        .padding(horizontal = 20.dp)
                                                        .background(color = colorResource(R.color.color_F2F4F6)),
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }

                    // 선택된 도시
                    val region = if(regionSelectionUiState.value.region?.nameRes != null) {
                        stringResource(regionSelectionUiState.value.region!!.nameRes)
                    } else {
                        ""
                    }
                    // 선택된 구
                    val detailRegion = regionSelectionUiState.value.detailRegion?.let { region ->
                        stringResource(region.regionNameRes)
                    } ?: ""
                    // 모두 선택되었는지 확인
                    val isNextStepAvailable = if(region.isNotBlank() && detailRegion.isNotBlank()) {
                        true
                    } else {
                        false
                    }

                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                    ) {
                        CommonButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 24.dp),
                            text = stringResource(R.string.common_next),
                            textColorRes = if(isNextStepAvailable) {
                                R.color.color_FFFFFFFF
                            } else {
                                R.color.color_191F28
                            },
                            backgroundColorRes = if(isNextStepAvailable) {
                                R.color.color_3182F6
                            } else {
                                R.color.color_6B7684
                            },
                            cornerRadius = 12.dp,
                            isBorderEnabled = false,
                            isEnabled = isNextStepAvailable,
                            textSize = 16.dp,
                            onClick = {
                                if(region.isNotEmpty() && detailRegion.isNotEmpty() && regionSelectionUiState.value.detailRegion != null) {
                                    onMoveToRegionSelectionComplete.invoke(regionSelectionUiState.value.detailRegion!!)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun RegionSelectionUIPreview() {
    RegionSelectionContent(
        localContext = LocalContext.current,
        onMoveToRegionSelectionComplete = {},
        onBackPressed = {},
        modifier = Modifier,
        regionSelectionViewModelEvent = {},
        regionSelectionUiState = remember {
            mutableStateOf(
                RegionSelectionUiState(
                    region = LibraryData.Region(code = 11, nameRes = R.string.region_seoul),
                    detailRegion = LibraryData.DetailRegion(11010, 11, R.string.region_full_seoul, R.string.district_11010)
                )
            )
        }
    )
}