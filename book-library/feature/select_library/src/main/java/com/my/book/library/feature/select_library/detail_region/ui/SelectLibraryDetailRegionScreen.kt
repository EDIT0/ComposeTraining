package com.my.book.library.feature.select_library.detail_region.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.CommonActionBar
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.core.resource.R
import com.my.book.library.feature.select_library.detail_region.intent.SelectLibraryDetailRegionViewModelEvent
import com.my.book.library.feature.select_library.detail_region.state.SelectLibraryDetailRegionUiState
import com.my.book.library.feature.select_library.detail_region.viewmodel.SelectLibraryDetailRegionViewModel

@Composable
fun SelectLibraryDetailRegionScreen(
    commonViewModel: CommonViewModel,
    onMoveToLibrary: (LibraryData.DetailRegion) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    region: LibraryData.Region
) {

    LogUtil.d_dev("받은 데이터: ${region}")
    val context = LocalContext.current

    val commonViewModel = commonViewModel
    val selectLibraryDetailRegionViewModel = hiltViewModel<SelectLibraryDetailRegionViewModel>()

    val selectLibraryDetailRegionUiState = selectLibraryDetailRegionViewModel.selectLibraryDetailRegionUiState.collectAsStateWithLifecycle()

    // Region 저장 - region이 변경될 때만 실행
    LaunchedEffect(region) {
        LogUtil.d_dev("LaunchedEffect 실행 - Region 저장: $region")
        selectLibraryDetailRegionViewModel.intentAction(
            SelectLibraryDetailRegionViewModelEvent.UpdateRegion(region = region)
        )
    }

    LogUtil.d_dev("SelectLibraryDetailRegionUiState: ${selectLibraryDetailRegionUiState.value}")

    SelectLibraryDetailRegionContent(
        localContext = context,
        onMoveToLibrary = onMoveToLibrary,
        onBackPressed = {
            onBackPressed.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        selectLibraryDetailRegionUiState = selectLibraryDetailRegionUiState
    )

}

@Composable
fun SelectLibraryDetailRegionContent(
    localContext: Context,
    onMoveToLibrary: (LibraryData.DetailRegion) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    selectLibraryDetailRegionUiState: State<SelectLibraryDetailRegionUiState>
) {

    Scaffold() { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CommonActionBar(
                    context = localContext,
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    actionBarTitle = localContext.getString(R.string.select_library_detail_region_title),
                    isShowBackButton = true,
                    onBackClick = {
                        onBackPressed.invoke()
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val filteredItems = LibraryData.allDetailRegions.filter { it -> it.regionCode == selectLibraryDetailRegionUiState.value.region?.code }
                    LogUtil.d_dev("필터된 세부지역 개수: ${filteredItems.size}, regionCode: ${selectLibraryDetailRegionUiState.value.region?.code}")

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
                                            onMoveToLibrary.invoke(item)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 20.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = item.districtName
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectLibraryDetailRegionUiPreview() {
    SelectLibraryDetailRegionContent(
        localContext = LocalContext.current,
        onMoveToLibrary = {},
        onBackPressed = {},
        modifier = Modifier,
        selectLibraryDetailRegionUiState = remember {
            mutableStateOf(
                SelectLibraryDetailRegionUiState(region = LibraryData.Region(code = 11, name = "서울"))
            )
        }
    )
}