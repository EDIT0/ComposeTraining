package com.my.book.library.feature.main.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.my.book.library.core.resource.Gray300
import com.my.book.library.core.resource.Gray600
import com.my.book.library.core.resource.Green500
import com.my.book.library.core.resource.Purple300
import com.my.book.library.feature.main.ui.component.FakeSearchBar
import com.my.book.library.feature.main.viewmodel.MainViewModel

@Composable
fun MainScreen(
    commonMainViewModel: ViewModel,
    modifier: Modifier
) {

    val localContext = LocalContext.current

    val commonMainViewModel = commonMainViewModel
    val mainViewModel = hiltViewModel<MainViewModel>()

    MainContent(
        localContext = localContext,
        modifier = Modifier
    )
}

@Composable
fun MainContent(
    localContext: Context,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            FakeSearchBar(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .background(
                        color = Green500,
                        shape = RoundedCornerShape(size = 10.dp)
                    ),
                text = localContext.getString(com.my.book.library.core.resource.R.string.search_library_info),
                textColor = Gray300,
                onClick = {
                    Log.i("MYTAG", "Fake SearchBar Clicked")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainUIPreview() {
    MainContent(
        localContext = LocalContext.current,
        modifier = Modifier
    )
}