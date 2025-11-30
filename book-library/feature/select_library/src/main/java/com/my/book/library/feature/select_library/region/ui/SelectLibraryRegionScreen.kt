package com.my.book.library.feature.select_library.region.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel

@Composable
fun SelectLibraryRegionScreen(
    commonMainViewModel: ViewModel,
    onBackPressed: () -> Unit,
    modifier: Modifier
) {
    Text("SelectLibraryRegionScreen")
}