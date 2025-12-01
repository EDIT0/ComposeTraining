package com.my.book.library.feature.select_library.region.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.my.book.library.core.common.component.CommonActionBar
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.core.resource.R
import com.my.book.library.feature.select_library.region.intent.SelectLibraryRegionViewModelEvent
import com.my.book.library.feature.select_library.region.viewmodel.SelectLibraryRegionViewModel

@Composable
fun SelectLibraryRegionScreen(
    commonMainViewModel: ViewModel,
    onMoveToDetailRegion: (LibraryData.Region) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current

    val commonMainViewModel = commonMainViewModel
    val selectLibraryRegionViewModel = hiltViewModel<SelectLibraryRegionViewModel>()

    SelectLibraryRegionContent(
        localContext = context,
        onMoveToDetailRegion = onMoveToDetailRegion,
        onBackPressed = {
            onBackPressed.invoke()
        },
        modifier = Modifier,
        selectLibraryRegionViewModelEvent = {
            when(it) {
                else -> {}
            }
        }
    )
}

@Composable
fun SelectLibraryRegionContent(
    localContext: Context,
    onMoveToDetailRegion: (LibraryData.Region) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    selectLibraryRegionViewModelEvent: (SelectLibraryRegionViewModelEvent) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CommonActionBar(
                context = localContext,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
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
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    content = {
                        itemsIndexed(
                            items = LibraryData.regionList,
                        ) { index, item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .noRippleClickable {
                                        onMoveToDetailRegion.invoke(item)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 20.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = item.name
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectLibraryRegionUIPreview() {
    SelectLibraryRegionContent(
        localContext = LocalContext.current,
        onMoveToDetailRegion = {},
        onBackPressed = {},
        modifier = Modifier,
        selectLibraryRegionViewModelEvent = {},
    )
}