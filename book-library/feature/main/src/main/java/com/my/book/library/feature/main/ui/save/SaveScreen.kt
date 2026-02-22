package com.my.book.library.feature.main.ui.save

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.resource.R
import com.my.book.library.feature.main.viewmodel.save.SaveViewModel

@Composable
fun SaveScreen(
    commonViewModel: CommonViewModel,
    mainViewModel: ViewModel
) {

    val localContext = LocalContext.current

    val commonViewModel = commonViewModel
    val mainViewModel = mainViewModel
    val saveViewModel = hiltViewModel<SaveViewModel>()

    SaveContent (
        localContext = localContext
    )
}

@Composable
fun SaveContent(
    localContext: Context,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.home_bottom_navigation_bar_menu_save)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SaveUIPreview() {
    SaveContent(
        localContext = LocalContext.current
    )
}