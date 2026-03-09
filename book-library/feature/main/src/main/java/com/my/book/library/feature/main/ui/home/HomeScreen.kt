package com.my.book.library.feature.main.ui.home

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.my.book.library.core.common.CommonViewModel
import com.my.book.library.core.common.component.LifecycleListener
import com.my.book.library.core.common.component.LifecycleResult
import com.my.book.library.core.common.dpToSp
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
    val lifecycleOwner = LocalLifecycleOwner.current

    val commonViewModel = commonViewModel
    val mainViewModel = mainViewModel
    val homeViewModel = hiltViewModel<HomeViewModel>()

    val testState = homeViewModel.testState.collectAsStateWithLifecycle()

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

    HomeContent(
        localContext = localContext,
        onMoveToSearchLibrary = onMoveToSearchLibrary,
        changeTestState = {
            homeViewModel.changeTestState(str = it)
        },
        testState = testState,
    )

    LifecycleListener(
        lifecycleOwner = lifecycleOwner,
        screenName = object {}.javaClass.enclosingClass?.simpleName ?: "HomeScreen",
        lifecycleResult = lifecycleResult
    )
}

@Composable
fun HomeContent(
    localContext: Context,
    onMoveToSearchLibrary: () -> Unit,
    changeTestState: (String) -> Unit,
    testState: State<String>
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            FakeSearchBar(
                modifier = Modifier,
                backgroundColor = Green500,
                backgroundShape = RoundedCornerShape(10.dp),
                text = localContext.getString(R.string.search_library_info),
                textColor = Gray300,
                onClick = {
                    onMoveToSearchLibrary.invoke()
                }
            )

            Column() {
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)))
                Text(text = "${testState.value}", style = TextStyle(fontSize = dpToSp(dp = 50.dp)), modifier = Modifier.clickable {
                    if(testState.value == "TEST") {
                        changeTestState.invoke("zzz")
                    } else {
                        changeTestState.invoke("TEST")
                    }
                })
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeUIPreview() {
    HomeContent(
        localContext = LocalContext.current,
        onMoveToSearchLibrary = {},
        changeTestState = {},
        testState = remember { mutableStateOf("") },
    )
}