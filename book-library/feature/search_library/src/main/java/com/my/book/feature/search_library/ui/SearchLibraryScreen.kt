package com.my.book.feature.search_library.ui

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.my.book.feature.search_library.intent.SearchLibraryViewModelEvent
import com.my.book.feature.search_library.state.SearchLibraryUiState
import com.my.book.feature.search_library.viewmodel.SearchLibraryViewModel
import com.my.book.library.core.common.component.CommonActionBar
import com.my.book.library.core.common.component.MenuBoxView
import com.my.book.library.core.common.component.SelectionChipView
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.core.resource.R

@Composable
fun SearchLibraryScreen(
    commonMainViewModel: ViewModel,
    onBackPressed: () -> Unit,
    modifier: Modifier
) {
    val localContext = LocalContext.current

    val commonMainViewModel = commonMainViewModel
    val searchLibraryViewModel = hiltViewModel<SearchLibraryViewModel>()

    val searchLibraryUiState = searchLibraryViewModel.searchLibraryUiState.collectAsStateWithLifecycle()
    
    SearchLibraryContent(
        localContext = localContext,
        onBackPressed = onBackPressed,
        modifier = Modifier,
        searchLibraryViewModelEvent = {
            when(it) {
                is SearchLibraryViewModelEvent.RequestGtSearchRegionBookLibrary -> {
                    searchLibraryViewModel.intentAction(searchLibraryViewModelEvent = it)
                }
                is SearchLibraryViewModelEvent.SetIsFilterOpen -> {
                    searchLibraryViewModel.intentAction(searchLibraryViewModelEvent = it)
                }
                is SearchLibraryViewModelEvent.SetSelectionRegion -> {
                    searchLibraryViewModel.intentAction(searchLibraryViewModelEvent = it)
                }
            }
        },
        searchLibraryUiState = searchLibraryUiState
    )
}

@Composable
fun SearchLibraryContent(
    localContext: Context,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    searchLibraryViewModelEvent: (SearchLibraryViewModelEvent) -> Unit,
    searchLibraryUiState: State<SearchLibraryUiState>
) {

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
        ) {
            CommonActionBar(
                context = localContext,
                modifier = Modifier,
                actionBarTitle = localContext.getString(R.string.search_library_action_bar_title),
                onBackClick = {
                    onBackPressed.invoke()
                }
            )

            MenuBoxView(
                modifier = Modifier,
                title = localContext.getString(com.my.book.library.core.resource.R.string.search_library_filter_title),
                isFilterOpen = searchLibraryUiState.value.isFilterOpen,
                onClick = {
                    searchLibraryViewModelEvent.invoke(SearchLibraryViewModelEvent.SetIsFilterOpen(isFilterOpen = !searchLibraryUiState.value.isFilterOpen))
                }
            )

            if(searchLibraryUiState.value.isFilterOpen) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = localContext.getString(R.string.search_library_selection_city_title)
                    )

                    SelectionChipView(
                        options = LibraryData.regionList,
                        selected = searchLibraryUiState.value.selectionRegion,
                        labelMapper = {
                            it.name
                        },
                        onSelectedChange = {
                            searchLibraryViewModelEvent.invoke(SearchLibraryViewModelEvent.SetSelectionRegion(selectionRegion = it))
                            LogUtil.d_dev("Selected region: ${it}")
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchLibraryUIPreview() {
    SearchLibraryContent(
        localContext = LocalContext.current,
        onBackPressed = {},
        modifier = Modifier,
        searchLibraryViewModelEvent = {},
        searchLibraryUiState = remember { mutableStateOf(SearchLibraryUiState()) }
    )
}