package com.my.book.library.feature.select_region.selection.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.CommonActionBar
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.common.util.SystemBarConfig
import com.my.book.library.core.common.util.SystemBarController
import com.my.book.library.core.resource.Black
import com.my.book.library.core.resource.LibraryData
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
                        isShowBackButton = true,
                        onBackClick = {
                            onBackPressed.invoke()
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .width(120.dp)
                                .fillMaxHeight()
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
                                                .noRippleClickable {
                                                    regionSelectionViewModelEvent.invoke(
                                                        RegionSelectionViewModelEvent.SetSelectionRegion(item)
                                                    )
                                                    regionSelectionViewModelEvent.invoke(
                                                        RegionSelectionViewModelEvent.SetSelectionDetailRegion(detailRegion = null)
                                                    )
                                                }
                                                .padding(horizontal = 10.dp, vertical = 20.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Text(
                                                text = stringResource(id = item.nameRes)
                                            )
                                        }
                                    }
                                }
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
                                    text = "지역을 선택해주세요",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .wrapContentSize(Alignment.Center)
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
                                                    .noRippleClickable {
                                                        regionSelectionViewModelEvent.invoke(RegionSelectionViewModelEvent.SetSelectionDetailRegion(item))
                                                    }
                                                    .padding(horizontal = 10.dp, vertical = 20.dp),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.Start
                                            ) {
                                                Text(
                                                    text = stringResource(id = item.districtNameRes)
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }

                    val region = if(regionSelectionUiState.value.region?.nameRes != null) {
                        stringResource(regionSelectionUiState.value.region!!.nameRes)
                    } else {
                        ""
                    }
                    val detailRegion = if(regionSelectionUiState.value.detailRegion?.districtNameRes != null) {
                        stringResource(regionSelectionUiState.value.detailRegion!!.districtNameRes)
                    } else {
                        ""
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .noRippleClickable {
                                if(region.isNotEmpty() && detailRegion.isNotEmpty() && regionSelectionUiState.value.detailRegion != null) {
                                    onMoveToRegionSelectionComplete.invoke(regionSelectionUiState.value.detailRegion!!)
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${region} ${detailRegion}",
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
//                    region = LibraryData.Region(code = 11, nameRes = R.string.region_seoul)
                )
            )
        }
    )
}