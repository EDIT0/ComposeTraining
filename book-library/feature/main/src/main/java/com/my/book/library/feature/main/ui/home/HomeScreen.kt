package com.my.book.library.feature.main.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.resource.Gray300
import com.my.book.library.core.resource.Green500
import com.my.book.library.core.resource.R
import com.my.book.library.feature.main.ui.component.FakeSearchBar
import com.my.book.library.feature.main.viewmodel.MainViewModel
import com.my.book.library.feature.main.viewmodel.home.HomeViewModel

@Composable
fun HomeScreen(
    onMoveToSearchLibrary: () -> Unit,
    commonViewModel: CommonViewModel,
    mainViewModel: MainViewModel
) {

    val localContext = LocalContext.current

    val commonViewModel = commonViewModel
    val mainViewModel = mainViewModel
    val homeViewModel = hiltViewModel<HomeViewModel>()

    HomeContent(
        localContext = localContext,
        onMoveToSearchLibrary = onMoveToSearchLibrary
    )
}

@Composable
fun HomeContent(
    localContext: Context,
    onMoveToSearchLibrary: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            FakeSearchBar(
                modifier = Modifier,
                backgroundColor = Green500,
                backgroundShape = RoundedCornerShape(10.dp),
                text = localContext.getString(R.string.search_library_info),
                textColor = Gray300,
                onClick = {
                    Log.i("MYTAG", "Fake SearchBar Clicked")
                    onMoveToSearchLibrary.invoke()
                }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeUIPreview() {
    HomeContent(
        localContext = LocalContext.current,
        onMoveToSearchLibrary = {}
    )
}