package com.my.book.library.feature.main.ui

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.feature.main.ui.home.HomeScreen
import com.my.book.library.feature.main.ui.save.SaveScreen
import com.my.book.library.feature.main.viewmodel.MainViewModel

@Composable
fun MainScreen(
    commonViewModel: CommonViewModel,
    onMoveToSearchLibrary: () -> Unit,
    modifier: Modifier
) {

    val localContext = LocalContext.current

    val commonViewModel = commonViewModel
    val mainViewModel = hiltViewModel<MainViewModel>()

    MainContent(
        localContext = localContext,
        onMoveToSearchLibrary = onMoveToSearchLibrary,
        modifier = modifier,
        commonViewModel = commonViewModel,
        mainViewModel = mainViewModel
    )
}

@Composable
fun MainContent(
    localContext: Context,
    onMoveToSearchLibrary: () -> Unit,
    modifier: Modifier,
    commonViewModel: CommonViewModel,
    mainViewModel: MainViewModel
) {
    val startDestination = MainDestination.HOME
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                MainDestination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = stringResource(destination.contentDescriptionResId)
                            )
                        },
                        label = {
                            Text(stringResource(destination.labelResId))
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedDestination) {
                MainDestination.HOME.ordinal -> {
                    HomeScreen(
                        onMoveToSearchLibrary = onMoveToSearchLibrary,
                        commonViewModel = commonViewModel,
                        mainViewModel = mainViewModel
                    )
                }
                MainDestination.SAVE.ordinal -> {
                    SaveScreen(
                        commonViewModel = commonViewModel,
                        mainViewModel = mainViewModel
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MainUIPreview() {
//    MainContent(
//        localContext = LocalContext.current,
//        onMoveToSearchLibrary = {},
//        modifier = Modifier
//    )
//}